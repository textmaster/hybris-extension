package com.textmaster.backoffice.services.impl;

import com.textmaster.backoffice.services.TextMasterPreviewService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.components.liveedit.PreviewLoader;
import de.hybris.platform.cmscockpit.resolvers.PreviewUrlPageResolver;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Executions;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;


public class DefaultTextMasterPreviewService implements TextMasterPreviewService
{
	private final static Logger LOG = LoggerFactory.getLogger(DefaultTextMasterPreviewService.class);

	private CMSPreviewService cmsPreviewService;
	private PreviewLoader previewLoader;
	private PreviewUrlPageResolver textMasterPreviewUrlPageResolver;
	private CommonI18NService commonI18NService;
	private ModelService modelService;
	private CMSPageService cmsPageService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProductPreviewUrl(final CMSSiteModel site, final ProductModel product, final LanguageModel language, final
	Collection<CatalogVersionModel> catalogVersions, CatalogVersionModel activeCatalogVersion)
	{
		PreviewDataModel previewCtx = getModelService().create(PreviewDataModel.class);
		previewCtx.setActiveCatalogVersion(activeCatalogVersion);
		previewCtx.setActiveSite(site);
		previewCtx.setLanguage(language);
		previewCtx.setCatalogVersions(catalogVersions);

		try
		{
			AbstractPageModel page = getCmsPageService().getPageForProduct(product);

			previewCtx.setPage(page);
			previewCtx.setPreviewProduct(product);

			return this.computeFinalUrl(site, previewCtx);
		}
		catch (CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		return "/";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCategoryPreviewUrl(final CMSSiteModel site, final CategoryModel category, final LanguageModel language, final
	Collection<CatalogVersionModel> catalogVersions, CatalogVersionModel activeCatalogVersion)
	{
		PreviewDataModel previewCtx = getModelService().create(PreviewDataModel.class);
		previewCtx.setActiveCatalogVersion(activeCatalogVersion);
		previewCtx.setActiveSite(site);
		previewCtx.setLanguage(language);
		previewCtx.setCatalogVersions(catalogVersions);

		try
		{
			AbstractPageModel page = getCmsPageService().getPageForCategory(category);

			previewCtx.setPage(page);
			previewCtx.setPreviewCategory(category);

			return this.computeFinalUrl(site, previewCtx);
		}
		catch (CMSItemNotFoundException e)
		{
			e.printStackTrace();
		}
		return "/";
	}

	protected String computeFinalUrl(final CMSSiteModel site, final PreviewDataModel currentPreviewData)
	{
		String previewUrl = "";
		StringBuilder urlBuffer = new StringBuilder();
		if (site == null)
		{
			LOG.warn("Site can not be null!");
			return previewUrl;
		}
		else
		{
			final AbstractPageModel page = currentPreviewData.getPage();

			previewUrl = this.extractUrlFromRequest(site);
			PreviewDataModel previewContext = this.getCmsPreviewService().clonePreviewData(currentPreviewData);
			if (previewContext == null)
			{
				previewContext = new PreviewDataModel();
			}

			previewContext.setEditMode(false);

			this.getPreviewLoader()
					.loadValues(previewContext, page, Collections.singletonList(page.getCatalogVersion()), false,
							this.getCurrentDataLanguageModel(previewContext), previewUrl);

			this.getModelService().save(previewContext);
			CMSPreviewTicketModel ticket = this.getCmsPreviewService().createPreviewTicket(previewContext);
			String[] urlParts = previewUrl.split("\\?");

			urlBuffer.append(urlParts[0]);
			urlBuffer.append("/");
			urlBuffer.append("previewServlet");
			urlBuffer.append("?");
			urlBuffer.append("cmsTicketId");
			urlBuffer.append("=");
			urlBuffer.append(ticket.getId());
			if (urlParts.length > 1)
			{
				urlBuffer.append("&");
				urlBuffer.append(urlParts[1]);
			}

			return urlBuffer.toString();
		}
	}

	protected String extractUrlFromRequest(final CMSSiteModel site)
	{
		StringBuilder urlBuilder = new StringBuilder();
		if (site != null && StringUtils.isNotBlank(site.getPreviewURL()))
		{
			if (!site.getPreviewURL().matches("(http://|https://)(.*)"))
			{
				HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
				urlBuilder.append(request.getScheme());
				urlBuilder.append("://");
				urlBuilder.append(request.getServerName());
				urlBuilder.append(":");
				urlBuilder.append(request.getServerPort());
				urlBuilder.append(site.getPreviewURL());
			}
			else
			{
				urlBuilder.append(site.getPreviewURL());
			}
		}

		return urlBuilder.toString();
	}

	protected LanguageModel getCurrentDataLanguageModel(final PreviewDataModel currentPreviewData)
	{
		return currentPreviewData != null && currentPreviewData.getLanguage() != null ?
				currentPreviewData.getLanguage() :
				this.getCommonI18NService().getLanguage(UISessionUtils
						.getCurrentSession().getGlobalDataLanguageIso());
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}

	protected PreviewLoader getPreviewLoader()
	{
		return previewLoader;
	}

	@Required
	public void setPreviewLoader(PreviewLoader previewLoader)
	{
		this.previewLoader = previewLoader;
	}

	protected PreviewUrlPageResolver getTextMasterPreviewUrlPageResolver()
	{
		return textMasterPreviewUrlPageResolver;
	}

	@Required
	public void setTextMasterPreviewUrlPageResolver(
			PreviewUrlPageResolver textMasterPreviewUrlPageResolver)
	{
		this.textMasterPreviewUrlPageResolver = textMasterPreviewUrlPageResolver;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(CommonI18NService i18NService)
	{
		this.commonI18NService = i18NService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(
			SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}
}
