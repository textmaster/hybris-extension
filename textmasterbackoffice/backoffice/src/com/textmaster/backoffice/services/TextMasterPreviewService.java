package com.textmaster.backoffice.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;


public interface TextMasterPreviewService {

    /**
     * Get the URL of the preview page
     *
     * @param site
     * @param product
     * @param language
	  * @param catalogVersions
     * @return
     */
	 public String getProductPreviewUrl(final CMSSiteModel site, final ProductModel product, final LanguageModel language, final
	 Collection<CatalogVersionModel> catalogVersions, CatalogVersionModel activeCatalogVersion);

	/**
	 * Get the URL of the preview category
	 *
	 * @param site
	 * @param category
	 * @param language
	 * @param catalogVersions
	 * @return
	 */
	public String getCategoryPreviewUrl(CMSSiteModel site, CategoryModel category, LanguageModel language,
			Collection<CatalogVersionModel> catalogVersions, CatalogVersionModel activeCatalogVersion);
}
