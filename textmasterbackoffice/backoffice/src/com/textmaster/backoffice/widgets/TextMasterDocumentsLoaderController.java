package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;


/**
 * Send documents in output of project in input
 */
public class TextMasterDocumentsLoaderController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterDocumentsLoaderController.class);

	@Autowired
	private ModelService modelService;

	private TextMasterProjectModel project;

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
		this.project = project;
		sendOutput("documents", project.getDocuments());
	}

	@SocketEvent(socketId = "reset")
	public void reset(final Object data)
	{
		this.project = null;
	}

	@SocketEvent(socketId = "refresh")
	public void refresh()
	{
		getModelService().refresh(project);
		sendOutput("documents", this.project.getDocuments());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
