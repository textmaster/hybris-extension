package com.textmaster.core.services;

import com.textmaster.core.exceptions.ItemAlreadyExistsException;
import com.textmaster.core.exceptions.ItemDoesNotExistsException;
import com.textmaster.core.model.TextMasterAccountModel;

import java.util.Collection;
import java.util.List;


/**
 * Manage account access.
 */
public interface TextMasterAccountService
{
	/**
	 * Verify if API key and secret are consistent on TextMaster platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	public boolean checkAuthentication(String apiKey, String apiSecret);

	/**
	 * Search account in hybris by api key and api secret.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	public TextMasterAccountModel getAccount(String apiKey, String apiSecret);

	/**
	 * Get account by code.
	 *
	 * @return
	 */
	public TextMasterAccountModel getAccount(String code);

	/**
	 * Get account list stored in hybris.
	 *
	 * @return
	 */
	public List<TextMasterAccountModel> getAccounts();

	/**
	 * Verify if an account does not already exists, if it is valid on TextMaster platform and save it in hybris.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 * @Throws UnknownIdentifierException, AmbiguousIdentifierException, ItemAlreadyExistsException, ItemDoesNotExistsException
	 */
	public TextMasterAccountModel create(String apiKey, String apiSecret)
			throws ItemAlreadyExistsException, ItemDoesNotExistsException;
}
