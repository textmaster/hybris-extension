package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;


/**
 * Format an enum name from value.
 */
public class TextMasterEnumRenderer extends AbstractTextMasterRenderer
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterEnumRenderer.class);

	private EnumerationService enumerationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getContent(Object object, Object value, WidgetInstanceManager widgetInstanceManager)
	{
		String content = "";

		if (value instanceof HybrisEnumValue)
		{
			HybrisEnumValue enumValue = (HybrisEnumValue) value;
			if (object instanceof TextMasterProjectModel)
			{
				String keyValue =
						"project.listview.column.status." + StringUtils.replace(StringUtils.lowerCase(enumValue.getCode()), "_", "");
				content = Labels.getLabel(keyValue);
			}
			else if (object instanceof TextMasterDocumentModel)
			{
				String keyValue =
						"document.listview.column.status." + StringUtils.replace(StringUtils.lowerCase(enumValue.getCode()), "_", "");
				content = Labels.getLabel(keyValue);
			}
		}
		else
		{
			LOG.warn("Impossible to convert text {} to enum", value);
		}
		return content;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
