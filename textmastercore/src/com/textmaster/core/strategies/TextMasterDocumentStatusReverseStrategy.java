package com.textmaster.core.strategies;

import com.textmaster.core.enums.TextMasterDocumentStatusEnum;

/**
 * Apply status conversion between datamodel statuses and TextMaster statuses
 */
public interface TextMasterDocumentStatusReverseStrategy {

    /**
     * Convert status.
     *
     * @param status
     * @return
     */
    public TextMasterDocumentStatusEnum translate(String status);
}
