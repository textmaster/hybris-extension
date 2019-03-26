package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.model.TextMasterAccountModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;


/**
 * Send projects in output of account in input
 */
public class TextMasterProjectsLoaderController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectsLoaderController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
	}

	@SocketEvent(socketId = "account")
	public void account(final TextMasterAccountModel account)
	{
		sendOutput("projects", account.getProjects());
	}
}
