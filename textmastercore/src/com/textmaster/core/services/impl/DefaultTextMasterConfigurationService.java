package com.textmaster.core.services.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterConfigurationModel;
import com.textmaster.core.model.TextMasterItemTypeModel;
import com.textmaster.core.services.TextMasterConfigurationService;


public class DefaultTextMasterConfigurationService implements TextMasterConfigurationService
{
	private final static Logger LOG = LoggerFactory.getLogger(DefaultTextMasterConfigurationService.class);

	private ModelService modelService;
	private CommonI18NService commonI18NService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterConfigurationModel getConfiguration(final TextMasterAccountModel account)
	{
		if (account != null)
		{
			// Get the configuration from account
			if (account.getConfiguration() != null)
			{
				return account.getConfiguration();
			}
			else
			{
				// Create a new one
				final TextMasterConfigurationModel configuration = modelService.create(TextMasterConfigurationModel.class);
				modelService.save(configuration);

				// Assign configuration to account
				account.setConfiguration(configuration);
				modelService.save(account);

				// Refresh
				modelService.refresh(configuration);

				return configuration;
			}
		}

		return null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(final TextMasterAccountModel account, final String sourceLanguageIsoCode, final String targetLanguageIsoCode,
			final HashMap<ComposedTypeModel, Collection<AttributeDescriptorModel>> selectedItem)
	{
		final TextMasterConfigurationModel configuration = this.getConfiguration(account);

		// Set source language
		LanguageModel sourceLanguage = null;
		if (StringUtils.isNotBlank(sourceLanguageIsoCode))
		{
			sourceLanguage = getCommonI18NService().getLanguage(sourceLanguageIsoCode);
		}
		configuration.setDefaultSourceLanguage(sourceLanguage);

		// Set target language
		LanguageModel targetLanguage = null;
		if (StringUtils.isNotBlank(targetLanguageIsoCode))
		{
			targetLanguage = getCommonI18NService().getLanguage(targetLanguageIsoCode);
		}
		configuration.setDefaultTargetLanguage(targetLanguage);

		// Set types and attributes
		final List<TextMasterItemTypeModel> newSelection = new ArrayList<>();
		for (final Map.Entry<ComposedTypeModel, Collection<AttributeDescriptorModel>> entry : selectedItem.entrySet())
		{
			final TextMasterItemTypeModel newITem = createItemType(entry);
			newSelection.add(newITem);
		}
		configuration.setTranslatableList(newSelection);

		getModelService().save(configuration);
	}

	/**
	 * Create TetMaster item type
	 *
	 * @param entry
	 * @return
	 */
	protected TextMasterItemTypeModel createItemType(Map.Entry<ComposedTypeModel, Collection<AttributeDescriptorModel>> entry)
	{
		final TextMasterItemTypeModel newITem = getModelService().create(TextMasterItemTypeModel.class);
		newITem.setTranslatedItemType(entry.getKey());
		newITem.setPreselectedAttributes(entry.getValue());
		return newITem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<AttributeDescriptorModel> getPreselectedAttributesForType(final TextMasterAccountModel account,
			final ComposedTypeModel type)
	{

		final TextMasterConfigurationModel configuration = getConfiguration(account);

		if (configuration != null)
		{
			for (final TextMasterItemTypeModel translatableItem : configuration.getTranslatableList())
			{
				if (translatableItem.getTranslatedItemType().equals(type))
				{
					return translatableItem.getPreselectedAttributes();
				}
			}
		}

		return null;
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

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
