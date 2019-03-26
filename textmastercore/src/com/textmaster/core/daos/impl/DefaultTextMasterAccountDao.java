package com.textmaster.core.daos.impl;

import com.textmaster.core.daos.TextMasterAccountDao;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.List;

public class DefaultTextMasterAccountDao extends AbstractItemDao implements TextMasterAccountDao {

    private static final String FIND_BY_KEY_AND_SECRET = "SELECT {" + TextMasterAccountModel.PK + "} FROM {"
            + TextMasterAccountModel._TYPECODE + "} WHERE {" + TextMasterAccountModel.APIKEY + "} = ?apiKey AND {"
            + TextMasterAccountModel.APISECRET + "} = ?apiSecret";

    private static final String FIND_BY_CODE = "SELECT {" + TextMasterAccountModel.PK + "} FROM {"
            + TextMasterAccountModel._TYPECODE + "} WHERE {" + TextMasterAccountModel.CODE + "} = ?code";

    private static final String FIND_ALL = "SELECT {" + TextMasterAccountModel.PK + "} FROM {"
            + TextMasterAccountModel._TYPECODE + "}";

    @Override
    public List<TextMasterAccountModel> findAccount(String apiKey, String apiSecret) {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_BY_KEY_AND_SECRET);
        flexibleSearchQuery.addQueryParameter("apiKey", apiKey);
        flexibleSearchQuery.addQueryParameter("apiSecret", apiSecret);

        final SearchResult<TextMasterAccountModel> results = search(flexibleSearchQuery);
        return results.getResult();
    }

    @Override
    public List<TextMasterAccountModel> findAccount(String code) {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_BY_CODE);
        flexibleSearchQuery.addQueryParameter("code", code);

        final SearchResult<TextMasterAccountModel> results = search(flexibleSearchQuery);
        return results.getResult();
    }

    @Override
    public List<TextMasterAccountModel> findAccounts() {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_ALL);

        final SearchResult<TextMasterAccountModel> results = search(flexibleSearchQuery);
        return results.getResult();
    }
}
