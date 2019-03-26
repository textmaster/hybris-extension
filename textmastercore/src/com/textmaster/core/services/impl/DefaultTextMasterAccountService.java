package com.textmaster.core.services.impl;

import com.textmaster.core.daos.TextMasterAccountDao;
import com.textmaster.core.exceptions.ItemAlreadyExistsException;
import com.textmaster.core.exceptions.ItemDoesNotExistsException;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterAccountService;
import com.textmaster.core.services.TextMasterApiService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Required;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;

import java.util.Collection;
import java.util.List;

public class DefaultTextMasterAccountService implements TextMasterAccountService {

    private TextMasterAccountDao textMasterAccountDao;
    private ModelService modelService;
    private TextMasterApiService textMasterApiService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkAuthentication(String apiKey, String apiSecret)
    {
        return getTextMasterApiService().checkAuthentication(apiKey, apiSecret);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMasterAccountModel getAccount(String apiKey, String apiSecret) {
        List<TextMasterAccountModel> accounts = getTextMasterAccountDao().findAccount(apiKey, apiSecret);

        validateIfSingleResult(accounts, String.format("Account with api key '%s' and apiSecret '%s' not found.", apiKey, apiSecret),
                String.format("Account with api key '%s' and apiSecret '%s' is not unique, %d accounts found.", apiKey, apiSecret, Integer.valueOf(accounts.size())));

        return accounts.stream().findFirst().get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMasterAccountModel getAccount(String code) {
        List<TextMasterAccountModel> accounts = getTextMasterAccountDao().findAccount(code);

        validateIfSingleResult(accounts, String.format("Account with code '%s' not found.", code),
                String.format("Account with code '%s' is not unique, %d accounts found.", code, Integer.valueOf(accounts.size())));

        return accounts.stream().findFirst().get();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<TextMasterAccountModel> getAccounts() {
        return getTextMasterAccountDao().findAccounts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMasterAccountModel create(String apiKey, String apiSecret) throws ItemAlreadyExistsException, ItemDoesNotExistsException {

        try {
            // Try to get hybris account
            this.getAccount(apiKey, apiSecret);

            throw new ItemAlreadyExistsException("Account already exist on hybris platform");
        }
        catch(UnknownIdentifierException uie) {

            // Check if the account is valid on TextMaster platform
            if (this.checkAuthentication(apiKey, apiSecret)) {

                // Create local account
                TextMasterAccountModel account = getModelService().create(TextMasterAccountModel.class);
                account.setApiKey(apiKey);
                account.setApiSecret(apiSecret);
                getModelService().save(account);

                return account;
            }
            else {
                throw new ItemDoesNotExistsException("Account does not exist on TextMaster platform");
            }
        }
    }

    protected TextMasterAccountDao getTextMasterAccountDao() {
        return textMasterAccountDao;
    }

    @Required
    public void setTextMasterAccountDao(TextMasterAccountDao textMasterAccountDao) {
        this.textMasterAccountDao = textMasterAccountDao;
    }

    protected ModelService getModelService() {
        return modelService;
    }

    @Required
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    protected TextMasterApiService getTextMasterApiService() {
        return textMasterApiService;
    }

    @Required
    public void setTextMasterApiService(TextMasterApiService textMasterApiService) {
        this.textMasterApiService = textMasterApiService;
    }
}
