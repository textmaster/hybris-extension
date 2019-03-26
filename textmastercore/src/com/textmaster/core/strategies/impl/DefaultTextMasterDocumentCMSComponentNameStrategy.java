package com.textmaster.core.strategies.impl;

import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.strategies.TextMasterDocumentNameStrategy;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTextMasterDocumentCMSComponentNameStrategy implements TextMasterDocumentNameStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(TextMasterDocumentModel document) {
        CMSItemModel cmsItem = (CMSItemModel)document.getItem();
        LanguageModel language = document.getProject().getLanguageSource().getLanguage();

        return String.format("%s (%s)", cmsItem.getName(), cmsItem.getUid());
    }
}
