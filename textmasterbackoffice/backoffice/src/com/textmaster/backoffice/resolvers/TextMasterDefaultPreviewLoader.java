package com.textmaster.backoffice.resolvers;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultPreviewLoader;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TextMasterDefaultPreviewLoader extends DefaultPreviewLoader
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterDefaultPreviewLoader.class);

	@Override
	protected CMSAdminRestrictionService getRestrictionService()
	{
		return (CMSAdminRestrictionService) Registry.getApplicationContext().getBean("cmsAdminRestrictionService");
	}

	protected CMSAdminSiteService getAdminSiteService()
	{
		return (CMSAdminSiteService) Registry.getApplicationContext().getBean("cmsAdminSiteService");
	}

	@Override
	public void loadValues(PreviewDataModel previewCtx, AbstractPageModel page, Collection<CatalogVersionModel> catVersions,
			boolean liveEdit, LanguageModel language, String resourcePath)
	{
		getAdminSiteService().setActiveCatalogVersion(previewCtx.getActiveCatalogVersion());
		getAdminSiteService().setActiveSite(previewCtx.getActiveSite());

		if (previewCtx == null) {
			throw new IllegalArgumentException("Preview context can not be null.");
		} else
		{
			if (CollectionUtils.isEmpty(previewCtx.getCatalogVersions()))
			{
				List<CatalogVersionModel> versions = new ArrayList(catVersions);
				previewCtx.setCatalogVersions(versions);
			}

			previewCtx.setLiveEdit(liveEdit);
			previewCtx.setResourcePath(resourcePath);
			previewCtx.setLanguage(language);
			previewCtx.setActiveSite(this.getAdminSiteService().getActiveSite());
			previewCtx.setActiveCatalogVersion(this.getAdminSiteService().getActiveCatalogVersion());
			if (page != null)
			{
				previewCtx.setPage(page);
			}

			// Note: Don't apply restriction to avoid redirection issues
		}
	}
}
