package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;


public class TextMasterProjectNbDocumentsRenderer<T> implements WidgetComponentRenderer<Component, T, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectNbDocumentsRenderer.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Component parent, T configuration, Object data, DataType dataType,
			WidgetInstanceManager widgetInstanceManager)
	{
		// Get project
		TextMasterProjectModel project = (TextMasterProjectModel) data;

		Label nbDocumentsLabel = new Label();

		nbDocumentsLabel.setValue("" + project.getDocuments().size());
		parent.appendChild(nbDocumentsLabel);
	}
}
