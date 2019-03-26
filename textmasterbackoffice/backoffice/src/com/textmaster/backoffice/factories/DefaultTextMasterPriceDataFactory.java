package com.textmaster.backoffice.factories;

import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import org.zkoss.util.Locales;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;


public class DefaultTextMasterPriceDataFactory extends DefaultPriceDataFactory
{
	@Override
	protected String formatPrice(final BigDecimal value, final CurrencyModel currency)
	{
		final NumberFormat currencyFormat = createCurrencyFormat(Locales.getCurrent(), currency);
		return currencyFormat.format(value);
	}
}
