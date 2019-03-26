package com.textmaster.core.services;

import com.textmaster.core.model.TextMasterLanguageModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.List;

/**
 * Extends default implementation.
 */
public interface TextMasterCommerceCommonI18NService extends CommerceCommonI18NService {

    /**
     * Retrieve the TextMasterLanguage from Hybris Language
     *
     * @param isocode
     * @return
     */
    public TextMasterLanguageModel getTextMasterLanguageForCommerceLanguage(String isocode);
}
