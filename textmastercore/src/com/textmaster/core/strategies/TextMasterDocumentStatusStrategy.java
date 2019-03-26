package com.textmaster.core.strategies;

import com.textmaster.core.enums.TextMasterDocumentStatusEnum;

/**
 * Apply status conversion between datamodel statuses and TextMaster statuses
 */
public interface TextMasterDocumentStatusStrategy {

    /**
     * Convert status.
     *
     * @param status
     * @return
     */
    public String translate(TextMasterDocumentStatusEnum status);
}
