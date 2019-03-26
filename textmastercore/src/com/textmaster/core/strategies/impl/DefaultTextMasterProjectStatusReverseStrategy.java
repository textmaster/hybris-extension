package com.textmaster.core.strategies.impl;

import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.strategies.TextMasterProjectStatusReverseStrategy;
import de.hybris.platform.enumeration.EnumerationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultTextMasterProjectStatusReverseStrategy implements TextMasterProjectStatusReverseStrategy
{
    private EnumerationService enumerationService;

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMasterProjectStatusEnum translate(String status) {
        return getEnumerationService().getEnumerationValue(TextMasterProjectStatusEnum.class, StringUtils.upperCase(status));
    }

    protected EnumerationService getEnumerationService() {
        return enumerationService;
    }

    @Required
    public void setEnumerationService(EnumerationService enumerationService) {
        this.enumerationService = enumerationService;
    }
}
