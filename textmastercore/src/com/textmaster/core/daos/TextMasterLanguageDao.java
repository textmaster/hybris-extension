package com.textmaster.core.daos;

import com.textmaster.core.model.TextMasterLanguageModel;

import java.util.List;

/**
 * Access to languages data.
 */
public interface TextMasterLanguageDao {

    /**
     * Find all languages.
     *
     * @return
     */
    public List<TextMasterLanguageModel> findAll();
}
