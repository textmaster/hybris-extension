package com.textmaster.core.strategies;

import com.textmaster.core.model.TextMasterDocumentModel;

/**
 * Generate document name for TextMaster
 */
public interface TextMasterDocumentNameStrategy {

    /**
     * Convert status.
     *
     * @param document
     * @return
     */
    public String translate(TextMasterDocumentModel document);
}
