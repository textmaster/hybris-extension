package com.textmaster.core.services.impl;

import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.services.TextMasterCommerceCommonI18NService;
import com.textmaster.core.services.TextMasterLanguageService;
import de.hybris.platform.commerceservices.i18n.impl.DefaultCommerceCommonI18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultTextMasterCommerceCommonI18NService extends DefaultCommerceCommonI18NService implements TextMasterCommerceCommonI18NService {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultTextMasterCommerceCommonI18NService.class);

    private TextMasterLanguageService textMasterLanguageService;

    @Override
    public TextMasterLanguageModel getTextMasterLanguageForCommerceLanguage(String isocode) {

        List<TextMasterLanguageModel> languages = getTextMasterLanguageService().getAll();
        return languages.stream()
                .filter(l -> l.getLanguage().getIsocode().equals(isocode))
                .findFirst().orElse(null);
    }

    protected TextMasterLanguageService getTextMasterLanguageService() {
        return textMasterLanguageService;
    }

    @Required
    public void setTextMasterLanguageService(TextMasterLanguageService textMasterLanguageService) {
        this.textMasterLanguageService = textMasterLanguageService;
    }
}
