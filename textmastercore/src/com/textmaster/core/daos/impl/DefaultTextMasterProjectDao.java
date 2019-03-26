package com.textmaster.core.daos.impl;

import com.textmaster.core.daos.TextMasterProjectDao;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

public class DefaultTextMasterProjectDao extends AbstractItemDao implements TextMasterProjectDao {

    private static final String FIND_BY_STATUS = "SELECT {" + TextMasterProjectModel.PK + "} FROM {"
            + TextMasterProjectModel._TYPECODE + "} WHERE {" + TextMasterProjectModel.STATUS + "} IN (?statuses)";

    @Override
    public List<TextMasterProjectModel> findProjects(List<TextMasterProjectStatusEnum> statuses) {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_BY_STATUS);
        flexibleSearchQuery.addQueryParameter("statuses", statuses);

        final SearchResult<TextMasterProjectModel> results = search(flexibleSearchQuery);
        return results.getResult();
    }
}
