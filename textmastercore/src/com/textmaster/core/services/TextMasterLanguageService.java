package com.textmaster.core.services;

import com.textmaster.core.model.TextMasterLanguageModel;

import java.util.List;

/**
 * Manage languages data.
 */
public interface TextMasterLanguageService {

    /**
     * Get all languages.
     *
     * @return
     */
    public List<TextMasterLanguageModel> getAll();

    /**
     * Get language for Isocode.
     *
     * @return
     */
    public TextMasterLanguageModel getForIsocode(String isocode);
}
