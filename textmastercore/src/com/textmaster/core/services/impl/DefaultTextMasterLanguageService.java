package com.textmaster.core.services.impl;

import com.textmaster.core.daos.TextMasterLanguageDao;
import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.services.TextMasterLanguageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultTextMasterLanguageService implements TextMasterLanguageService {

    public static final Logger LOG = Logger.getLogger(DefaultTextMasterLanguageService.class);

    private TextMasterLanguageDao textMasterLanguageDao;

    @Override
    public List<TextMasterLanguageModel> getAll() {
        return getTextMasterLanguageDao().findAll();
    }

    @Override
    public TextMasterLanguageModel getForIsocode(String isocode) {
        return getAll().stream()
                .filter(l -> isocode.equalsIgnoreCase(l.getIsocode()))
                .findFirst()
                .orElse(null);
    }

    protected TextMasterLanguageDao getTextMasterLanguageDao() {
        return textMasterLanguageDao;
    }

    @Required
    public void setTextMasterLanguageDao(TextMasterLanguageDao textMasterLanguageDao) {
        this.textMasterLanguageDao = textMasterLanguageDao;
    }
}
