package com.textmaster.core.daos.impl;

import com.textmaster.core.daos.TextMasterDocumentDao;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.model.TextMasterDocumentModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


public class DefaultTextMasterDocumentDao extends AbstractItemDao implements TextMasterDocumentDao
{

	private static final String FIND_BY_STATUS = "SELECT {" + TextMasterDocumentModel.PK + "} FROM {"
			+ TextMasterDocumentModel._TYPECODE + "} WHERE {" + TextMasterDocumentModel.STATUS + "} IN (?statuses)";

	@Override
	public List<TextMasterDocumentModel> findDocumentsForStatuses(List<TextMasterDocumentStatusEnum> statuses)
	{
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_BY_STATUS);
		flexibleSearchQuery.addQueryParameter("statuses", statuses);

		final SearchResult<TextMasterDocumentModel> results = search(flexibleSearchQuery);
		return results.getResult();
	}
}
