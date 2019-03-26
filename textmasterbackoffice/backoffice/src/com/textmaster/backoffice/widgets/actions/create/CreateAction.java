package com.textmaster.backoffice.widgets.actions.create;

import org.apache.commons.lang.StringUtils;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.create.CreateContext;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;


public class CreateAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionResult<Object> perform(final ActionContext<Object> ctx)
	{
		ActionResult result = null;
		if (ctx.getData() instanceof String)
		{
			if (StringUtils.isNotBlank(((String) ctx.getData())))
			{
				super.sendOutput("createContext", new CreateContext((String) ctx.getData()));
				result = new ActionResult("success");
			}
		}
		else
		{
			result = new ActionResult("error");
		}
		return result;
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
