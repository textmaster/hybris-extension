package com.textmaster.core.cronjobs;

import com.textmaster.core.constants.TextmastercoreConstants;
import com.textmaster.core.dtos.TextMasterDocumentDto;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterDocumentAttributeModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterDocumentService;
import com.textmaster.core.services.TextMasterProjectService;
import com.textmaster.core.strategies.TextMasterDocumentStatusReverseStrategy;
import com.textmaster.core.strategies.TextMasterDocumentStatusStrategy;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Get translations to import on hybris platform.
 */
public class TextMasterGetTranslationsPerformable extends AbstractJobPerformable<CronJobModel>
{

	private final static Logger LOG = LoggerFactory.getLogger(TextMasterGetTranslationsPerformable.class);

	private TextMasterProjectService textMasterProjectService;
	private TextMasterDocumentStatusStrategy textMasterDocumentStatusStrategy;
	private TextMasterDocumentStatusReverseStrategy textMasterDocumentStatusReverseStrategy;
	private CommonI18NService CommonI18NService;
	private ConfigurationService configurationService;
	private TextMasterDocumentService textMasterDocumentService;

	@Override
	public PerformResult perform(CronJobModel cronJobModel)
	{

		boolean mustUpdateCatalog = getConfigurationService().getConfiguration()
				.getBoolean(TextmastercoreConstants.MUST_UPDATE_CATALOG_PROPERTY_KEY);

		// Prepare project statuses
		List<TextMasterProjectStatusEnum> projectStatuses = getTextMasterProjectService().getProjectAvailableStatuses();

		// Prepare available locales
		Locale[] availableLocales = getAvailableLocales();

		// Take in account all projects open
		List<TextMasterProjectModel> projects = getTextMasterProjectService().getProjects(projectStatuses);

		// Find all documents filter by their status
		for (TextMasterProjectModel project : projects)
		{
			// Prepare target locale
			Locale targetLocale = getCommonI18NService().getLocaleForLanguage(project.getLanguageTarget().getLanguage());

			// Prepare filters
			Map<String, Object> documentFilters = new HashMap<>();
			documentFilters.put("status", getTextMasterDocumentStatusStrategy().translate(TextMasterDocumentStatusEnum.IN_REVIEW));

			// Get translations from remote platform
			List<TextMasterDocumentDto> documents = getTextMasterDocumentService().filterDocuments(project, documentFilters);

			// For each document, save translation on TextMasterDocumentAttributeItem
			Collection<TextMasterDocumentAttributeModel> documentAttributesToSave = new ArrayList<>();
			Collection<TextMasterDocumentModel> documentsToSave = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(documents))
			{
				documents.stream()
						.forEach(d -> {
							PK pk = PK.fromLong(Long.valueOf(d.getCustomData().getExternalClientId()));
							TextMasterDocumentModel documentModel = getModelService().get(pk);

							Map<String, String> authorTranslations = d.getAuthorWork();
							authorTranslations.forEach((attributeCode, translation) -> {

								// Find attribute attached to document
								TextMasterDocumentAttributeModel attribute = documentModel.getDocumentAttributes()
										.stream()
										.filter(a -> a.getAttribute().getQualifier().equals(attributeCode))
										.findFirst().get();

								// Update translation
								attribute.setTranslatedContent(translation);

								documentAttributesToSave.add(attribute);
							});

							// Update document
							documentModel.setStatus(getTextMasterDocumentStatusReverseStrategy().translate(d.getStatus()));
							documentModel.setReference(d.getReference());
							documentsToSave.add(documentModel);
						});

				getModelService().saveAll(documentAttributesToSave);
				getModelService().saveAll(documentsToSave);

				getModelService().refresh(project);

				if (project.getDocuments().stream().allMatch(d -> d.getStatus() == TextMasterDocumentStatusEnum.IN_REVIEW))
				{
					project.setStatus(TextMasterProjectStatusEnum.IN_REVIEW);
					getModelService().save(project);
					getModelService().refresh(project);
				}
			}


			Collection<ItemModel> itemsToSave = new ArrayList<>();

			// If required (according to configuration), update item itself
			if (mustUpdateCatalog)
			{
				// Update attributes on item associated to document
				documentAttributesToSave.stream()
						.forEach(a -> {

							// Keep existing content
							Map<Locale, Object> translations = new HashMap<>();
							translations.putAll(modelService.getAttributeValues(a, a.getAttribute().getQualifier(), availableLocales));
							translations.put(targetLocale, a.getTranslatedContent());

							// Add translation to catalog item
							ItemModel item = a.getDocument().getItem();
							modelService.setAttributeValue(a.getDocument().getItem(), a.getAttribute().getQualifier(), translations);
							itemsToSave.add(item);
						});
			}

			// Save all document attributes
			getModelService().saveAll(itemsToSave);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Return an array of locales to be used at lower model service level.
	 *
	 * @return
	 */
	protected Locale[] getAvailableLocales()
	{
		List<Locale> locales = getCommonI18NService().getAllLanguages()
				.stream()
				.map(l -> getCommonI18NService().getLocaleForLanguage(l))
				.collect(Collectors.toList());

		Object[] array = locales.toArray();
		return Arrays.copyOf(array, array.length, Locale[].class);
	}

	protected TextMasterProjectService getTextMasterProjectService()
	{
		return textMasterProjectService;
	}

	@Required
	public void setTextMasterProjectService(TextMasterProjectService textMasterProjectService)
	{
		this.textMasterProjectService = textMasterProjectService;
	}

	protected TextMasterDocumentStatusStrategy getTextMasterDocumentStatusStrategy()
	{
		return textMasterDocumentStatusStrategy;
	}

	@Required
	public void setTextMasterDocumentStatusStrategy(TextMasterDocumentStatusStrategy textMasterDocumentStatusStrategy)
	{
		this.textMasterDocumentStatusStrategy = textMasterDocumentStatusStrategy;
	}

	protected ModelService getModelService()
	{
		return modelService;
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

	protected de.hybris.platform.servicelayer.i18n.CommonI18NService getCommonI18NService()
	{
		return CommonI18NService;
	}

	@Required
	public void setCommonI18NService(de.hybris.platform.servicelayer.i18n.CommonI18NService commonI18NService)
	{
		CommonI18NService = commonI18NService;
	}

	protected TextMasterDocumentService getTextMasterDocumentService()
	{
		return textMasterDocumentService;
	}

	@Required
	public void setTextMasterDocumentService(TextMasterDocumentService textMasterDocumentService)
	{
		this.textMasterDocumentService = textMasterDocumentService;
	}

	protected TextMasterDocumentStatusReverseStrategy getTextMasterDocumentStatusReverseStrategy()
	{
		return textMasterDocumentStatusReverseStrategy;
	}

	@Required
	public void setTextMasterDocumentStatusReverseStrategy(
			TextMasterDocumentStatusReverseStrategy textMasterDocumentStatusReverseStrategy)
	{
		this.textMasterDocumentStatusReverseStrategy = textMasterDocumentStatusReverseStrategy;
	}
}
