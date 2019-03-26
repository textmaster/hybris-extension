package com.textmaster.core.services.impl;

import com.textmaster.core.daos.TextMasterDocumentDao;
import com.textmaster.core.dtos.TextMasterDocumentDto;
import com.textmaster.core.dtos.TextMasterDocumentsCompleteRequestDto;
import com.textmaster.core.dtos.TextMasterDocumentsResponseDto;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.exceptions.TextMasterException;
import com.textmaster.core.model.TextMasterDocumentAttributeModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterApiService;
import com.textmaster.core.services.TextMasterDocumentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;
import java.util.stream.Collectors;


public class DefaultTextMasterDocumentService implements TextMasterDocumentService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTextMasterDocumentService.class);

	private ModelService modelService;
	private CommonI18NService commonI18NService;
	private TypeService typeService;
	private List<Class> textMasterRootClasses;
	private TextMasterDocumentDao textMasterDocumentDao;
	private TextMasterApiService textMasterApiService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ComposedTypeModel> getComposedTypes()
	{
		return getTextMasterRootClasses().stream()
				.map(c -> getTypeService().getComposedTypeForClass(c))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param document
	 * @return
	 */
	@Override
	public Class getRootClassFromDocument(TextMasterDocumentModel document)
	{
		List<Class> rootClass = getTextMasterRootClasses();

		for (Class type : rootClass)
		{
			if (type.isInstance(document.getItem()))
			{
				return type;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterDocumentModel createDocument(TextMasterProjectModel project, ItemModel item)
	{
		// Create document
		TextMasterDocumentModel doc = getModelService().create(TextMasterDocumentModel.class);
		doc.setItem(item);
		doc.setProject(project);
		doc.setStatus(TextMasterDocumentStatusEnum.IN_CREATION);

		// Get locale source to passe correct content
		Locale locale = getCommonI18NService().getLocaleForLanguage(project.getLanguageSource().getLanguage());

		// Associate to attribute
		Map<Object, Object> fields = new HashMap<>();
		doc.setDocumentAttributes(project.getAttributes().stream()
				.map(a -> createDocumentAttribute(item, locale, a))
				.filter(da -> da != null)
				.collect(Collectors.toList()));

		return doc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterDocumentAttributeModel createDocumentAttribute(ItemModel item, Locale locale, AttributeDescriptorModel a)
	{
		String value = getValueForAttribute(item, a, locale);
		TextMasterDocumentAttributeModel documentAttribute = null;
		if (value != null)
		{
			documentAttribute = getModelService().create(TextMasterDocumentAttributeModel.class);
			documentAttribute.setAttribute(a);
			documentAttribute.setOriginalContent(value);
		}
		return documentAttribute;
	}

	/**
	 * Get status which correspond to untranslated document
	 *
	 * @return
	 */
	protected List<TextMasterDocumentStatusEnum> getDocumentUntranslatedStatuses()
	{
		return Arrays.asList(
				TextMasterDocumentStatusEnum.IN_CREATION,
				TextMasterDocumentStatusEnum.WAITING_ASSIGNMENT,
				TextMasterDocumentStatusEnum.IN_PROGRESS,
				TextMasterDocumentStatusEnum.INCOMPLETE,
				TextMasterDocumentStatusEnum.COPYSCAPE,
				TextMasterDocumentStatusEnum.COUNTING_WORDS,
				TextMasterDocumentStatusEnum.QUALITY_CONTROL);
	}

	/**
	 * Filter attribute checking if a value exists. It returns an object if this is the case, null otherwise.
	 *
	 * @param item
	 * @param attribute
	 * @param locale
	 * @return
	 */
	protected String getValueForAttribute(ItemModel item, AttributeDescriptorModel attribute, Locale locale)
	{
		Object value = getModelService().getAttributeValue(item, attribute.getQualifier(), locale);

		if (value == null
				|| !(value instanceof String)
				|| ((value instanceof String) && StringUtils.isEmpty((String) value)))
		{
			return null;
		}

		return (String) value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TextMasterDocumentDto> filterDocuments(TextMasterProjectModel project,
			Map<String, Object> filters)
	{
		List<TextMasterDocumentDto> fullList = new ArrayList<>();

		int page = 1;
		do
		{
			TextMasterDocumentsResponseDto response = getTextMasterApiService()
					.filterDocuments(project.getAccount().getApiKey(), project.getAccount().getApiSecret(), project.getRemoteId(),
							filters, page);

			if (CollectionUtils.isNotEmpty(response.getDocuments()))
			{
				fullList.addAll(response.getDocuments());
			}

			// Calculate the next page
			page = response.getPage() + 1;
			if (page > response.getTotalPages())
			{
				page = 0;
			}
		}
		while (page != 0);

		return fullList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void completeDocuments(TextMasterProjectModel project, List<TextMasterDocumentModel> documents)
			throws TextMasterException
	{
		// Create the list to send
		List<String> documentsRemoteIds = documents
				.stream()
				.map(d -> d.getRemoteId())
				.collect(Collectors.toList());

		// Create DTO
		TextMasterDocumentsCompleteRequestDto requestDto = new TextMasterDocumentsCompleteRequestDto();
		requestDto.setDocuments(documentsRemoteIds);

		// Complete documents
		if (!getTextMasterApiService()
				.completeDocuments(project.getAccount().getApiKey(), project.getAccount().getApiSecret(), project.getRemoteId(),
						requestDto))
		{
			throw new TextMasterException("Impossible to complete all documents");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TextMasterDocumentModel> getDocumentsForStatuses(List<TextMasterDocumentStatusEnum> statuses)
	{
		return getTextMasterDocumentDao().findDocumentsForStatuses(statuses);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Required
	public void setTextMasterRootClasses(List<Class> textMasterRootClasses)
	{
		this.textMasterRootClasses = textMasterRootClasses;
	}

	protected List<Class> getTextMasterRootClasses()
	{
		return textMasterRootClasses;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected TextMasterDocumentDao getTextMasterDocumentDao()
	{
		return textMasterDocumentDao;
	}

	@Required
	public void setTextMasterDocumentDao(TextMasterDocumentDao textMasterDocumentDao)
	{
		this.textMasterDocumentDao = textMasterDocumentDao;
	}

	protected TextMasterApiService getTextMasterApiService()
	{
		return textMasterApiService;
	}

	@Required
	public void setTextMasterApiService(TextMasterApiService textMasterApiService)
	{
		this.textMasterApiService = textMasterApiService;
	}
}
