package com.textmaster.core.strategies.impl;

import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.strategies.TextMasterDocumentNameStrategy;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTextMasterDocumentProductNameStrategy implements TextMasterDocumentNameStrategy {

    private CommonI18NService commonI18NService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(TextMasterDocumentModel document) {
        ProductModel product = (ProductModel)document.getItem();
        LanguageModel language = document.getProject().getLanguageSource().getLanguage();

        commonI18NService = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService");

        return String.format("%s (%s)", product.getName(getCommonI18NService().getLocaleForLanguage(language)), product.getCode());
    }

    protected CommonI18NService getCommonI18NService() {
        return commonI18NService;
    }

    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }
}
