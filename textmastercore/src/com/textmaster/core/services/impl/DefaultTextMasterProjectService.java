package com.textmaster.core.services.impl;

import com.textmaster.core.constants.TextmastercoreConstants;
import com.textmaster.core.daos.TextMasterProjectDao;
import com.textmaster.core.dtos.*;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.exceptions.ItemDoesNotExistsException;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterApiService;
import com.textmaster.core.services.TextMasterDocumentService;
import com.textmaster.core.services.TextMasterProjectService;
import com.textmaster.core.strategies.TextMasterDocumentStatusReverseStrategy;
import com.textmaster.core.strategies.TextMasterProjectStatusReverseStrategy;
import com.textmaster.core.utils.TextMasterBatchIterator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class DefaultTextMasterProjectService implements TextMasterProjectService
{
	public static final Logger LOG = LoggerFactory.getLogger(DefaultTextMasterProjectService.class);

	private ModelService modelService;
	private TextMasterApiService textMasterApiService;
	private Converter<TextMasterProjectModel, TextMasterProjectDto> textMasterProjectConverter;
	private Converter<TextMasterDocumentModel, TextMasterDocumentDto> textMasterDocumentConverter;
	private CommonI18NService commonI18NService;
	private TextMasterProjectDao textMasterProjectDao;
	private TextMasterDocumentService textMasterDocumentService;
	private ConfigurationService configurationService;
	private TaskService taskService;
	private TextMasterProjectStatusReverseStrategy textMasterProjectStatusReverseStrategy;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TextMasterProjectModel> getProjects(final List<TextMasterProjectStatusEnum> statuses)
	{
		return getTextMasterProjectDao().findProjects(statuses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TextMasterProjectStatusEnum> getProjectAvailableStatuses()
	{
		return Arrays.asList(TextMasterProjectStatusEnum.IN_CREATION, TextMasterProjectStatusEnum.IN_PROGRESS,
				TextMasterProjectStatusEnum.IN_REVIEW);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TextMasterProjectModel createAndPushProject(final String name, final String templateId,
			final TextMasterLanguageModel sourceLanguage, final TextMasterLanguageModel targetLanguage,
			final TextMasterAccountModel account, final ComposedTypeModel type, final List<AttributeDescriptorModel> attributes,
			final List<ItemModel> items)
	{
		final TextMasterProjectModel project = this.createProject(name, templateId, sourceLanguage, targetLanguage, account,
				type, attributes, items);
		this.pushProject(project);
		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterProjectModel pushProject(final TextMasterProjectModel project)
	{
		// Convert project model to DTO
		final TextMasterProjectDto projectDto = getTextMasterProjectConverter().convert(project);
		final TextMasterProjectRequestDto requestProjectDto = new TextMasterProjectRequestDto();
		requestProjectDto.setProject(projectDto);

		// Push on remote platform
		final TextMasterProjectResponseDto responseProjectDto = getTextMasterApiService()
				.createProject(project.getAccount().getApiKey(), project.getAccount().getApiSecret(), requestProjectDto);

		// Associate remote id to local item
		project.setRemoteId(responseProjectDto.getId());
		project.setReference(responseProjectDto.getReference());
		project.setAutolaunch(responseProjectDto.getAutoLaunch());
		getModelService().save(project);

		return project;
	}

	/**
	 * Send all documents to TextMaster, creating batches
	 *
	 * @param project
	 */
	@Override
	public void sendAndReceiveDocuments(TextMasterProjectModel project)
	{
		// Create documents via batchs
		int nbItemsInBatch = getConfigurationService().getConfiguration().getInt(TextmastercoreConstants.Documents.BATCH_SIZE);

		final List<TextMasterDocumentDto> docs = project.getDocuments().stream()
				.map(d -> getTextMasterDocumentConverter().convert(d))
				.collect(Collectors.toList());

		// Send and receive
		getDocumentsBatch(docs, nbItemsInBatch)
				.forEach(batch -> sendAndReceiveDocumentsBatch(project, batch));
	}

	/**
	 * Send document batch to TextMaster.
	 *
	 * @param project
	 * @param batch
	 */
	protected void sendAndReceiveDocumentsBatch(TextMasterProjectModel project, List<TextMasterDocumentDto> batch)
	{
		// Convert document models to DTO
		final TextMasterDocumentsRequestDto requestDocumentsDto = new TextMasterDocumentsRequestDto();
		requestDocumentsDto.setDocuments(batch);

		// Push on remote platform
		final List<TextMasterDocumentDto> responseDocumentsDto = getTextMasterApiService().createDocuments(
				project.getAccount().getApiKey(), project.getAccount().getApiSecret(), project.getRemoteId(), requestDocumentsDto);

		// Associate remote ids to local items
		final List<TextMasterDocumentModel> projectDocuments = responseDocumentsDto.stream()
				.map(dto -> {
					TextMasterDocumentModel doc = null;
					try
					{
						doc = project.getDocuments().stream()
								.filter(d -> d.getPk().getLongValueAsString().equals(dto.getCustomData().getExternalClientId()))
								.findFirst()
								.orElseThrow(() -> new ItemDoesNotExistsException(""));
						doc.setRemoteId(dto.getId());
					}
					catch (final ItemDoesNotExistsException e)
					{
						LOG.error("Impossible to find item with PK [{}]", dto.getCustomData().getExternalClientId());
					}
					return doc;
				})
				.collect(Collectors.toList());
		getModelService().saveAll(projectDocuments);
	}

	/**
	 * Create batch of documents.
	 *
	 * @param documents
	 * @param size
	 * @return
	 */
	protected Stream<List<TextMasterDocumentDto>> getDocumentsBatch(Collection<TextMasterDocumentDto> documents, int size)
	{
		TextMasterBatchIterator batchIterator = new TextMasterBatchIterator(documents.stream().iterator(), size);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(batchIterator, Spliterator.ORDERED), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TextMasterProjectModel createProject(final String name, final String templateId,
			final TextMasterLanguageModel sourceLanguage, final TextMasterLanguageModel targetLanguage,
			final TextMasterAccountModel account, final ComposedTypeModel type, final List<AttributeDescriptorModel> attributes,
			final List<ItemModel> items)
	{

		// Create on hybris
		final TextMasterProjectModel project = modelService.create(TextMasterProjectModel.class);
		project.setName(name);
		project.setAccount(account);
		project.setTranslatedItemType(type);
		project.setAttributes(attributes);
		project.setTemplateId(templateId);
		project.setStatus(TextMasterProjectStatusEnum.IN_CREATION);

		project.setLanguageSource(sourceLanguage);
		project.setLanguageTarget(targetLanguage);

		// Associate items
		final Collection<TextMasterDocumentModel> docs = items.stream()

				// Filter items which could not be translated (no attribute value to translate)
				.filter(i -> filter(i, project)).map(i -> getTextMasterDocumentService().createDocument(project, i))
				.collect(Collectors.toList());
		project.setDocuments(docs);

		getModelService().save(project);

		return project;
	}

	/**
	 * Filter an item checking if at least one attribute contains a value to translate. If it is not the case, the item must
	 * not be pushed to remote platform.
	 *
	 * @param item
	 * @param project
	 * @return
	 */
	protected boolean filter(final ItemModel item, final TextMasterProjectModel project)
	{

		// Get locale source to passe correct content
		final Locale locale = getCommonI18NService().getLocaleForLanguage(project.getLanguageSource().getLanguage());

		for (final AttributeDescriptorModel attribute : project.getAttributes())
		{
			final Object value = getModelService().getAttributeValue(item, attribute.getQualifier(), locale);

			if (value instanceof Map)
			{
				if (MapUtils.isNotEmpty((Map) value))
				{
					return true;
				}
			}
			else if (value instanceof Collection)
			{
				if (CollectionUtils.isNotEmpty((Collection) value))
				{
					return true;
				}
			}
			else if (value != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalize(TextMasterProjectModel project)
	{
		getTextMasterApiService()
				.finalize(project.getAccount().getApiKey(), project.getAccount().getApiSecret(), project.getRemoteId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch(TextMasterProjectModel project)
	{
		TextMasterProjectResponseDto
				response = getTextMasterApiService()
				.launch(project.getAccount().getApiKey(), project.getAccount().getApiSecret(), project.getRemoteId());

		// Update project status
		project.setStatus(getTextMasterProjectStatusReverseStrategy().translate(response.getStatus()));
		project.setReference(response.getReference());
		getModelService().save(project);

		// Update all documents to IN_PROGRESS status. No cronjob manage all document statuses to avoid bad performance
		List<TextMasterDocumentModel> documents = project.getDocuments()
				.stream()
				.map(d -> {
					d.setStatus(TextMasterDocumentStatusEnum.IN_PROGRESS);
					return d;
				})
				.collect(Collectors.toList());
		getModelService().saveAll(documents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void recoverDocuments(TextMasterProjectModel project)
	{
		// TODO: Found a solution to filter documents to not retrieve all documents each time.

		// Get all translations from remote platform
		List<TextMasterDocumentDto> remoteDocuments = getTextMasterDocumentService()
				.filterDocuments(project, Collections.EMPTY_MAP);

		// Update each document without remote id
		List<TextMasterDocumentModel> documentsToSave = project.getDocuments().stream()
				.filter(d -> StringUtils.isEmpty(d.getRemoteId()))
				.map(d -> {
					Optional<String> remoteId = remoteDocuments.stream()
							.filter(rd -> d.getPk().getLongValueAsString().equals(rd.getCustomData().getExternalClientId()))
							.map(rd -> rd.getId())
							.findFirst();
					if (remoteId.isPresent())
					{
						d.setRemoteId(remoteId.get());
					}
					return d;
				})
				.collect(Collectors.toList());
		getModelService().saveAll(documentsToSave);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TextMasterApiTemplateDto> getTemplates(final String apiKey, final String apiSecret)
	{
		List<TextMasterApiTemplateDto> fullList = new ArrayList<>();

		int page = 0;
		do
		{
			TextMasterApiTemplateResponseDto response = getTextMasterApiService().getTemplates(apiKey, apiSecret, page);

			if (CollectionUtils.isNotEmpty(response.getApiTemplates()))
			{
				fullList.addAll(response.getApiTemplates());
			}

			page = response.getTotalPages() - response.getPage();
		}
		while (page > 0);

		return fullList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterProjectResponseDto getProject(TextMasterProjectModel project)
	{
		return getTextMasterApiService()
				.getProject(project.getAccount().getApiKey(), project.getAccount().getApiSecret(), project.getRemoteId());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TextMasterApiService getTextMasterApiService()
	{
		return textMasterApiService;
	}

	@Required
	public void setTextMasterApiService(final TextMasterApiService textMasterApiService)
	{
		this.textMasterApiService = textMasterApiService;
	}

	protected Converter<TextMasterProjectModel, TextMasterProjectDto> getTextMasterProjectConverter()
	{
		return textMasterProjectConverter;
	}

	@Required
	public void setTextMasterProjectConverter(
			final Converter<TextMasterProjectModel, TextMasterProjectDto> textMasterProjectConverter)
	{
		this.textMasterProjectConverter = textMasterProjectConverter;
	}

	protected Converter<TextMasterDocumentModel, TextMasterDocumentDto> getTextMasterDocumentConverter()
	{
		return textMasterDocumentConverter;
	}

	@Required
	public void setTextMasterDocumentConverter(
			final Converter<TextMasterDocumentModel, TextMasterDocumentDto> textMasterDocumentConverter)
	{
		this.textMasterDocumentConverter = textMasterDocumentConverter;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected TextMasterProjectDao getTextMasterProjectDao()
	{
		return textMasterProjectDao;
	}

	@Required
	public void setTextMasterProjectDao(final TextMasterProjectDao textMasterProjectDao)
	{
		this.textMasterProjectDao = textMasterProjectDao;
	}

	protected TextMasterDocumentService getTextMasterDocumentService()
	{
		return textMasterDocumentService;
	}

	@Required
	public void setTextMasterDocumentService(final TextMasterDocumentService textMasterDocumentService)
	{
		this.textMasterDocumentService = textMasterDocumentService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected TaskService getTaskService()
	{
		return taskService;
	}

	@Required
	public void setTaskService(TaskService taskService)
	{
		this.taskService = taskService;
	}

	protected TextMasterProjectStatusReverseStrategy getTextMasterProjectStatusReverseStrategy()
	{
		return textMasterProjectStatusReverseStrategy;
	}

	@Required
	public void setTextMasterProjectStatusReverseStrategy(
			TextMasterProjectStatusReverseStrategy textMasterProjectStatusReverseStrategy)
	{
		this.textMasterProjectStatusReverseStrategy = textMasterProjectStatusReverseStrategy;
	}
}
