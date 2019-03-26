package com.textmaster.core.services;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterConfigurationModel;


/**
 * Manage account access.
 */
public interface TextMasterConfigurationService
{
	/**
	 * Get the configuration for a TextMaster account.if it does not exist, create a new one
	 *
	 * @param account
	 * @return TextMasterConfigurationModel
	 */
	public TextMasterConfigurationModel getConfiguration(final TextMasterAccountModel account);

	/**
	 * Save configuration.
	 *
	 * @param account
	 * @param sourceLanguageIsoCode
	 * @param targetLanguageIsoCode
	 * @param selectedItem
	 */
	public void save(final TextMasterAccountModel account, final String sourceLanguageIsoCode, final String targetLanguageIsoCode,
			final HashMap<ComposedTypeModel, Collection<AttributeDescriptorModel>> selectedItem);

	/**
	 * Update the preselected languages for a TextMaster account
	 *
	 * @param account
	 * @param type
	 * @return List
	 */
	public Collection<AttributeDescriptorModel> getPreselectedAttributesForType(final TextMasterAccountModel account,
			final ComposedTypeModel type);
}
