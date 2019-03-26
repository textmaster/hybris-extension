package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.jalo.TextMasterAccount;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.services.TextMasterAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;


/**
 * Send accounts in output
 */
public class TextMasterAccountsLoaderController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterAccountsLoaderController.class);

	@WireVariable
	private TextMasterAccountService textMasterAccountService;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);

		// Dispatch accounts available
		sendAccounts();

		// Dispatch selected account stored in component by default
		TextMasterAccountModel account = getModel().getValue("account", TextMasterAccountModel.class);
		if (account != null)
		{
			refresh(account);
		}
	}

	@SocketEvent(socketId = "refresh")
	public void refresh(final Object object)
	{
		sendAccounts();
	}

	public void sendAccounts()
	{
		sendOutput("accounts", getTextMasterAccountService().getAccounts());
	}

	@SocketEvent(socketId = "selectedAccount")
	public void refresh(final TextMasterAccountModel account)
	{
		sendOutput("selectAccount", account);

		// Store account into model
		getModel().put("account", account);
	}

	protected TextMasterAccountService getTextMasterAccountService()
	{
		return textMasterAccountService;
	}

	@Required
	public void setTextMasterAccountService(TextMasterAccountService textMasterAccountService)
	{
		this.textMasterAccountService = textMasterAccountService;
	}
}
