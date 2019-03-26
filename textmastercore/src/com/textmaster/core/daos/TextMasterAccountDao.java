package com.textmaster.core.daos;

import com.textmaster.core.model.TextMasterAccountModel;

import java.util.List;

/**
 * Access to account data.
 */
public interface TextMasterAccountDao {

    /**
     * Find a specific account.
     *
     * @param apiKey
     * @param apiSecret
     * @return
     */
    public List<TextMasterAccountModel> findAccount(String apiKey, String apiSecret);

    /**
     * Find a specific account.
     *
     * @param code
     * @return
     */
    public List<TextMasterAccountModel> findAccount(String code);

    /**
     * Find account list stored in hybris.
     *
     * @return
     */
    public List<TextMasterAccountModel> findAccounts();
}
