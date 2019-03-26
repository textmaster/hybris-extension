package com.textmaster.core.strategies.impl;

import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.strategies.TextMasterDocumentStatusStrategy;
import org.apache.commons.lang.StringUtils;

public class DefaultTextMasterDocumentStatusStrategy implements TextMasterDocumentStatusStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(TextMasterDocumentStatusEnum status) {

        // Simply put code into lower case
        return StringUtils.lowerCase(status.getCode());
    }
}
