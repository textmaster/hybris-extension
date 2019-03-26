package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterProjectService;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Vlayout;


public class TextMasterProjectActionsRenderer<T> implements WidgetComponentRenderer<Component, T, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectActionsRenderer.class);

	private TextMasterProjectService textMasterProjectService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Component parent, T configuration, Object data, DataType dataType,
			WidgetInstanceManager widgetInstanceManager)
	{
		// Get project
		TextMasterProjectModel project = (TextMasterProjectModel) data;

		// Check if the project must be started or not
		if (!TextMasterProjectStatusEnum.IN_CREATION.equals(project.getStatus()))
		{
			return;
		}

		Vlayout container = new Vlayout();

		if (!project.getAutolaunch())
		{
			Button startButton = new Button();

			// Display button only if the projet has been finalized
			if (BooleanUtils.isTrue(project.getFinalized()))
			{
				startButton.setLabel(Labels.getLabel("project.list.actions.startbutton"));
				startButton.addEventListener(Events.ON_CLICK, new EventListener<Event>()
				{
					@Override
					public void onEvent(Event event) throws Exception
					{
						// Start project
						getTextMasterProjectService().launch(project);

						// Refresh project list
						widgetInstanceManager.sendOutput("account", project.getAccount());
					}
				});
				container.appendChild(startButton);
			}
		}

		parent.appendChild(container);
	}

	protected TextMasterProjectService getTextMasterProjectService()
	{
		return textMasterProjectService;
	}

	@Required
	public void setTextMasterProjectService(TextMasterProjectService textMasterProjectService)
	{
		this.textMasterProjectService = textMasterProjectService;
	}
}
