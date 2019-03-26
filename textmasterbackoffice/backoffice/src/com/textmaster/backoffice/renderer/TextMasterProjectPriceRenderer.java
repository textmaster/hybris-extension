package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;


public class TextMasterProjectPriceRenderer<T> implements WidgetComponentRenderer<Component, T, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectPriceRenderer.class);

	private PriceDataFactory priceDataFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Component parent, T configuration, Object data, DataType dataType,
			WidgetInstanceManager widgetInstanceManager)
	{
		// Get project
		TextMasterProjectModel project = (TextMasterProjectModel) data;

		Label priceLabel = new Label();

		String content = "-";
		if (StringUtils.isNotBlank(project.getCurrencyIsocode())) {
			PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, project.getPrice(), project.getCurrencyIsocode());
			content = priceData.getFormattedValue();
		}
		priceLabel.setValue(content);

		parent.appendChild(priceLabel);
	}

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}
}
