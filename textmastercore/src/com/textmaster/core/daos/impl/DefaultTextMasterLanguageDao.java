package com.textmaster.core.daos.impl;

import com.textmaster.core.daos.TextMasterLanguageDao;
import com.textmaster.core.model.TextMasterLanguageModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

public class DefaultTextMasterLanguageDao extends AbstractItemDao implements TextMasterLanguageDao {

    private static final String FIND_ALL = "SELECT {" + TextMasterLanguageModel.PK + "} FROM {"
            + TextMasterLanguageModel._TYPECODE + "}";

    @Override
    public List<TextMasterLanguageModel> findAll() {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_ALL);

        final SearchResult<TextMasterLanguageModel> results = search(flexibleSearchQuery);
        return results.getResult();
    }
}
