package com.textmaster.backoffice.widgets.actions.refresh;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;


public class RefreshAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(RefreshAction.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionResult<Object> perform(final ActionContext<Object> ctx)
	{
		ActionResult result = new ActionResult("success");

		sendOutput("refresh", new Object());

		return result;
	}

	@GlobalCockpitEvent(
			eventName = "objectsDeleted",
			scope = "session"
	)
	public void handleObjectDeleteEvent(CockpitEvent event)
	{
		sendOutput("refresh", new Object());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPerform(final ActionContext<Object> var1)
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfirmationMessage(final ActionContext<Object> var1)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needsConfirmation(final ActionContext<Object> var1)
	{
		return false;
	}
}
