package com.textmaster.core.services;

import com.textmaster.core.dtos.TextMasterDocumentDto;
import com.textmaster.core.dtos.TextMasterDocumentsResponseDto;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.exceptions.TextMasterException;
import com.textmaster.core.model.TextMasterDocumentAttributeModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Manage document data.
 */
public interface TextMasterDocumentService
{

	/**
	 * Create document from item model.
	 *
	 * @param project
	 * @param item
	 * @return
	 */
	public TextMasterDocumentModel createDocument(TextMasterProjectModel project, ItemModel item);

	/**
	 * Create attribute of a document.
	 *
	 * @param item
	 * @param locale
	 * @param a
	 * @return
	 */
	public TextMasterDocumentAttributeModel createDocumentAttribute(ItemModel item, Locale locale, AttributeDescriptorModel a);

	/**
	 * Get root composed type which can be translated.
	 *
	 * @return
	 */
	public List<ComposedTypeModel> getComposedTypes();

	/**
	 * Get root class from document.
	 *
	 * @param document
	 * @return
	 */
	public Class getRootClassFromDocument(TextMasterDocumentModel document);

	/**
	 * Find a all documents for statuses.
	 *
	 * @param statuses
	 * @return
	 */
	public List<TextMasterDocumentModel> getDocumentsForStatuses(List<TextMasterDocumentStatusEnum> statuses);

	/**
	 * Filter documents for project.
	 *
	 * @param project
	 * @param filters
	 * @return
	 */
	public List<TextMasterDocumentDto> filterDocuments(final TextMasterProjectModel project, final Map<String, Object> filters);

	/**
	 * Complete documents.
	 *
	 * @param project
	 * @param documents
	 */
	public void completeDocuments(final TextMasterProjectModel project, final List<TextMasterDocumentModel> documents) throws
			TextMasterException;
}
