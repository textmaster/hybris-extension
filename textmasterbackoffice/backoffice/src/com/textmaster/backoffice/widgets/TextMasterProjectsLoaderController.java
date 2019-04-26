package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.model.TextMasterAccountModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;


/**
 * Send projects in output of account in input
 */
public class TextMasterProjectsLoaderController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectsLoaderController.class);

	@Autowired
	private ModelService modelService;

	private TextMasterAccountModel account;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
	}

	@SocketEvent(socketId = "account")
	public void account(final TextMasterAccountModel account)
	{
		this.account = account;
		sendOutput("projects", account.getProjects());
	}

	@SocketEvent(socketId = "reset")
	public void reset(final Object data)
	{
		this.account = null;
	}

	@SocketEvent(socketId = "refresh")
	public void refresh()
	{
		getModelService().refresh(account);
		sendOutput("projects", this.account.getProjects());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
