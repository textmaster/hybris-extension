package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;


/**
 * Format a date value.
 */
public class TextMasterDateRenderer extends AbstractTextMasterRenderer
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterDateRenderer.class);

	@Override protected String getContent(Object object, Object value, WidgetInstanceManager widgetInstanceManager)
	{
		String content = "";
		if (value instanceof Date)
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locales.getCurrent());
			LocalDateTime date = LocalDateTime.ofInstant(((Date) value).toInstant(), ZoneOffset.UTC);
			content = date.format(formatter);
		}
		else
		{
			LOG.warn("Impossible to convert text {} to date", value);
		}
		return content;
	}
}
