package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.textmaster.backoffice.services.TextMasterPreviewService;
import com.textmaster.core.model.TextMasterDocumentModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Vlayout;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


public class TextMasterDocumentActionsRenderer<T> implements WidgetComponentRenderer<Component, T, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterDocumentActionsRenderer.class);

	private TextMasterPreviewService textMasterPreviewService;
	private CatalogVersionService catalogVersionService;
	private ImpersonationService impersonationService;
	private CMSAdminSiteService cmsAdminSiteService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Component parent, T configuration, Object data, DataType dataType,
			WidgetInstanceManager widgetInstanceManager)
	{
		// Get document
		TextMasterDocumentModel document = (TextMasterDocumentModel) data;

		if (!isDocumentValid(document))
		{
			return;
		}

		Vlayout container = new Vlayout();

		Button viewInContextButton = new Button();
		viewInContextButton.setLabel(Labels.getLabel("document.list.actions.viewincontext"));

		CMSSiteModel site = getSiteForItem(document.getItem());
		Collection<CatalogVersionModel> catalogVersions = getCatalogVersionsForItem(document.getItem());
		CatalogVersionModel contentCatalogVersion = getContentCatalogVersionForItem(document.getItem());

		final ImpersonationContext context = new ImpersonationContext();
		context.setSite(site);
		context.setLanguage(document.getProject().getLanguageTarget().getLanguage());
		context.setCatalogVersions(catalogVersions);

		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("**************************");
				LOG.debug("Impersonation Context Data");
				LOG.debug("Site: {}", site.getUid());
				LOG.debug("Document: {}", document.getCode());
				LOG.debug("Language: {}", document.getProject().getLanguageTarget().getLanguage().getIsocode());
				LOG.debug("CatalogVersion: {}",
						catalogVersions.stream().map(cv -> "Catalog " + cv.getCatalog().getId() + "." + cv.getVersion()).collect(
								Collectors.joining(" / ")));
				LOG.debug("**************************");
			}

			final String url = (String) getImpersonationService()
					.executeInContext(context, getUrl(document, site, contentCatalogVersion, catalogVersions));

			viewInContextButton.addEventListener(Events.ON_CLICK, new EventListener<Event>()
			{
				@Override
				public void onEvent(Event event) throws Exception
				{
					Executions.getCurrent().sendRedirect(url, "_new");
				}
			});

			container.appendChild(viewInContextButton);
		}
		catch (Throwable e)
		{
			LOG.error("Impossible to generate preview URL", e);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Impossible to generate preview URL", e);
			}
		}

		parent.appendChild(container);
	}

	/**
	 * Define is a document is valid to display action buttons.
	 *
	 * @param document
	 * @return
	 */
	protected boolean isDocumentValid(TextMasterDocumentModel document)
	{
		ItemModel item = document.getItem();

		if (item instanceof ProductModel)
		{
			ProductModel product = (ProductModel) item;

			if (product.getApprovalStatus() != ArticleApprovalStatus.APPROVED ||
					(product.getOfflineDate() != null && product.getOfflineDate().toInstant().isBefore(Instant.now())) ||
					(product.getOnlineDate() != null && product.getOnlineDate().toInstant().isAfter(Instant.now())))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Get all catalog versions used for item.
	 *
	 * @param item
	 * @return
	 */
	protected Collection<CatalogVersionModel> getCatalogVersionsForItem(final ItemModel item)
	{
		Collection<CatalogVersionModel> catalogVersions = new ArrayList<>();

		if (item instanceof ProductModel)
		{
			ProductModel product = (ProductModel) item;

			CatalogVersionModel productCatalogVersion = product.getCatalogVersion();
			catalogVersions.add(productCatalogVersion);

			// Add content catalog version
			CatalogVersionModel contentCatalogVersion = getContentCatalogVersionForItem(item);
			catalogVersions.add(contentCatalogVersion);
		}
		else if (item instanceof CategoryModel)
		{
			CategoryModel category = (CategoryModel) item;

			CatalogVersionModel productCatalogVersion = category.getCatalogVersion();
			catalogVersions.add(productCatalogVersion);

			// Add content catalog version
			CatalogVersionModel contentCatalogVersion = getContentCatalogVersionForItem(item);
			catalogVersions.add(contentCatalogVersion);
		}
		else
		{
			catalogVersions.add(getContentCatalogVersionForItem(item));
		}

		return catalogVersions;
	}

	/**
	 * Get content catalog version for item
	 *
	 * @param item
	 * @return
	 */
	protected CatalogVersionModel getContentCatalogVersionForItem(ItemModel item)
	{
		if (item instanceof ProductModel)
		{
			CMSSiteModel site = getSiteForItem(item);
			if (site != null)
			{
				if (CollectionUtils.isNotEmpty(site.getContentCatalogs()))
				{
					CatalogModel contentCatalog = site.getContentCatalogs().stream().findFirst().get();

					return contentCatalog.getCatalogVersions().stream()
							.filter(cv -> cv.getActive())
							.findFirst().get();
				}
			}
		}
		else if (item instanceof CategoryModel)
		{
			return ((CategoryModel) item).getCatalogVersion();
		}
		else if (item instanceof CMSItemModel)
		{
			throw new UnsupportedOperationException("Impossible to generate preview URL for CMS Item element");
		}

		return null;
	}

	/**
	 * Get CMS site for item.
	 *
	 * @param item
	 * @return
	 */
	protected CMSSiteModel getSiteForItem(final ItemModel item)
	{
		CatalogVersionModel catalogVersion = null;

		if (item instanceof ProductModel)
		{
			catalogVersion = ((ProductModel) item).getCatalogVersion();
		}
		else if (item instanceof CategoryModel)
		{
			catalogVersion = ((CategoryModel) item).getCatalogVersion();
		}
		else if (item instanceof CMSItemModel)
		{
			throw new UnsupportedOperationException("Impossible to generate preview URL for CMS Item element");
		}

		if (catalogVersion != null)
		{
			if (CollectionUtils.isNotEmpty(catalogVersion.getCatalog().getBaseStores())
					&& CollectionUtils
					.isNotEmpty(catalogVersion.getCatalog().getBaseStores().stream().findFirst().get().getCmsSites()))
			{
				return (CMSSiteModel) catalogVersion.getCatalog().getBaseStores().stream().findFirst().get().getCmsSites()
						.stream().findFirst().get();
			}
		}

		return null;
	}

	/**
	 * Get URL
	 *
	 * @param document
	 * @param site
	 * @param contentCatalogVersion
	 * @param catalogVersions
	 * @return
	 */
	protected ImpersonationService.Executor<Object, Throwable> getUrl(TextMasterDocumentModel document,
			CMSSiteModel site, CatalogVersionModel contentCatalogVersion,
			Collection<CatalogVersionModel> catalogVersions)
	{
		return new ImpersonationService.Executor<Object, Throwable>()
		{
			@Override
			public Object execute() throws Throwable
			{
				String url = null;
				ItemModel item = document.getItem();

				if (item instanceof ProductModel)
				{
					return getTextMasterPreviewService()
							.getProductPreviewUrl(site, (ProductModel) item, document.getProject().getLanguageTarget().getLanguage(),
									catalogVersions, contentCatalogVersion);
				}
				else if (item instanceof CategoryModel)
				{
					return getTextMasterPreviewService()
							.getCategoryPreviewUrl(site, (CategoryModel) item, document.getProject().getLanguageTarget().getLanguage(),
									catalogVersions, contentCatalogVersion);
				}
				else if (item instanceof CMSItemModel)
				{
					throw new UnsupportedOperationException(
							"Impossible to generate preview URL for CMS Item element [" + item.getPk() + "]");
				}
				return url;
			}
		};
	}

	protected TextMasterPreviewService getTextMasterPreviewService()
	{
		return textMasterPreviewService;
	}

	protected ImpersonationService getImpersonationService()
	{
		return impersonationService;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setTextMasterPreviewService(TextMasterPreviewService textMasterPreviewService)
	{
		this.textMasterPreviewService = textMasterPreviewService;
	}

	@Required
	public void setImpersonationService(ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}

	@Required
	public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}
}
