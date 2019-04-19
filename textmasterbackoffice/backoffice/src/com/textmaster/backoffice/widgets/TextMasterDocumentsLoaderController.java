package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.model.TextMasterProjectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;


/**
 * Send documents in output of project in input
 */
public class TextMasterDocumentsLoaderController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterDocumentsLoaderController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
	}

	@SocketEvent(socketId = "project")
	public void project(final TextMasterProjectModel project)
	{
		if (project == null) {
			LOG.error("Impossible to load documents to project: no project provided in widget socket parameters");
			return;
		}
		sendOutput("documents", project.getDocuments());
	}
}
