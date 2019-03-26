package com.textmaster.core.strategies.impl;

import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.strategies.TextMasterDocumentStatusReverseStrategy;
import com.textmaster.core.strategies.TextMasterDocumentStatusStrategy;
import de.hybris.platform.enumeration.EnumerationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTextMasterDocumentStatusReverseStrategy implements TextMasterDocumentStatusReverseStrategy {

    private EnumerationService enumerationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMasterDocumentStatusEnum translate(String status) {
        return getEnumerationService().getEnumerationValue(TextMasterDocumentStatusEnum.class, StringUtils.upperCase(status));
    }

    protected EnumerationService getEnumerationService() {
        return enumerationService;
    }

    @Required
    public void setEnumerationService(EnumerationService enumerationService) {
        this.enumerationService = enumerationService;
    }
}
