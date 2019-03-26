package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.util.QualifierLabel;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Listcell;


public abstract class AbstractTextMasterRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractTextMasterRenderer.class);

	private WidgetRenderingUtils widgetRenderingUtils;
	private PropertyValueService propertyValueService;

	/**
	 * Return content to display.
	 *
	 * @param object
	 * @param value
	 * @param widgetInstanceManager
	 * @return
	 */
	protected abstract String getContent(Object object, Object value, WidgetInstanceManager widgetInstanceManager);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Listcell parent, ListColumn configuration, Object object, DataType dataType,
			WidgetInstanceManager widgetInstanceManager)
	{
		Object value = getPropertyValueService().readValue(object, configuration.getQualifier());
		String content = getContent(object, value, widgetInstanceManager);
		content = UITools.truncateText(content, widgetInstanceManager.getWidgetSettings().getInt("maxCharsInCell"));
		parent.setLabel(content);
	}

	protected WidgetRenderingUtils getWidgetRenderingUtils()
	{
		return widgetRenderingUtils;
	}

	@Required
	public void setWidgetRenderingUtils(WidgetRenderingUtils widgetRenderingUtils)
	{
		this.widgetRenderingUtils = widgetRenderingUtils;
	}

	protected PropertyValueService getPropertyValueService()
	{
		return propertyValueService;
	}

	@Required
	public void setPropertyValueService(PropertyValueService propertyValueService)
	{
		this.propertyValueService = propertyValueService;
	}
}
