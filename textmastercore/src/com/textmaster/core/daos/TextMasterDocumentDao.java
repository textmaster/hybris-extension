package com.textmaster.core.daos;

import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.model.TextMasterDocumentModel;

import java.util.List;


/**
 * Access to document data.
 */
public interface TextMasterDocumentDao
{

	/**
	 * Find all documents for statuses.
	 *
	 * @param statuses
	 * @return
	 */
	public List<TextMasterDocumentModel> findDocumentsForStatuses(List<TextMasterDocumentStatusEnum> statuses);
}
