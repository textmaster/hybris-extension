package com.textmaster.core.services;

import com.textmaster.core.dtos.TextMasterApiTemplateDto;
import com.textmaster.core.dtos.TextMasterProjectResponseDto;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.List;


/**
 * Manage project data.
 */
public interface TextMasterProjectService
{

	/**
	 * Create a projet on the hybris platform and push it on TextMaster platform
	 *
	 * @param name
	 * @param templateId
	 * @param account
	 * @param type
	 * @param attributes
	 * @return
	 */
	public TextMasterProjectModel createAndPushProject(String name, String templateId, TextMasterLanguageModel sourceLanguage,
			TextMasterLanguageModel targetLanguage, TextMasterAccountModel account, ComposedTypeModel type,
			List<AttributeDescriptorModel> attributes, List<ItemModel> items);

	/**
	 * Create a projet on the hybris platform
	 *
	 * @param name
	 * @param templateId
	 * @param account
	 * @param type
	 * @param attributes
	 * @return
	 */
	public TextMasterProjectModel createProject(String name, String templateId, TextMasterLanguageModel sourceLanguage,
			TextMasterLanguageModel targetLanguage, TextMasterAccountModel account, ComposedTypeModel type,
			List<AttributeDescriptorModel> attributes, List<ItemModel> items);

	/**
	 * Prepare data and push project onto remote platform.
	 *
	 * @param project
	 * @return
	 */
	public TextMasterProjectModel pushProject(TextMasterProjectModel project);

	/**
	 * Find a all projects for statuses.
	 *
	 * @param statuses
	 * @return
	 */
	public List<TextMasterProjectModel> getProjects(List<TextMasterProjectStatusEnum> statuses);

	/**
	 * Get project available statuses.
	 *
	 * @return
	 */
	public List<TextMasterProjectStatusEnum> getProjectAvailableStatuses();

	/**
	 * Finalize project, scheduling task.
	 *
	 * @param project
	 */
	public void finalize(TextMasterProjectModel project);

	/**
	 * Launch project.
	 *
	 * @param project
	 */
	public void launch(TextMasterProjectModel project);

	/**
	 * Recover documents retrieving remote ID if they are not filled.
	 *
	 * @param project
	 */
	public void recoverDocuments(TextMasterProjectModel project);

	/**
	 * Send all documents to TextMaster using batches
	 *
	 * @param project
	 */
	public void sendAndReceiveDocuments(TextMasterProjectModel project);

	/**
	 * Get templates from remote platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	public List<TextMasterApiTemplateDto> getTemplates(String apiKey, String apiSecret);

	/**
	 * Get a project.
	 *
	 * @param project
	 * @return
	 */
	public TextMasterProjectResponseDto getProject(TextMasterProjectModel project);
}
