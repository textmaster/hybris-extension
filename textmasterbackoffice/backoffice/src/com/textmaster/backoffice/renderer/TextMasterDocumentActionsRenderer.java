package com.textmaster.backoffice.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
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
}
