package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.WidgetContainer;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import com.textmaster.core.dtos.TextMasterApiTemplateDto;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.services.TextMasterCommerceCommonI18NService;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TextMasterProjectWizardController extends DefaultWidgetController
{

	public static final Logger LOG = Logger.getLogger(TextMasterProjectWizardController.class);

	private Div contentDiv;
	private Div breadcrumbDiv;
	private Div msgAreaDiv;

	private final Textbox txtProjectName = new Textbox();
	private final Combobox cbxLangFrom = new Combobox();
	private final Combobox cbxLangTo = new Combobox();
	private final Combobox cbxTemplate = new Combobox();
	private final Combobox cbxType = new Combobox();
	private TextMasterLanguageModel sourceLanguage;
	private TextMasterLanguageModel targetLanguage;

	private TextMasterAccountModel account;
	private List<TextMasterApiTemplateDto> templateList;

	@WireVariable
	private WidgetInstanceFacade widgetInstanceFacade;

	@WireVariable
	private TextMasterProjectService textMasterProjectService;

	@WireVariable
	private TypeService typeService;

	@WireVariable
	private TextMasterCommerceCommonI18NService commerceCommonI18NService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
	}

	@SocketEvent(socketId = "inputAccount")
	public void onInputAccount(final TextMasterAccountModel account)
	{
		this.account = account;
		templateList = textMasterProjectService.getTemplates(this.account.getApiKey(), this.account.getApiSecret());
		renderBreadcrumb();
		renderContent();
	}

	@SocketEvent(socketId = "open")
	public void onOpen()
	{
		renderBreadcrumb();
		renderContent();
	}

	@ViewEvent(componentID = "btCancel", eventName = Events.ON_CLICK)
	public void onCancel(final Event event)
	{
		this.closeWindowIfTemplate();
	}

	@ViewEvent(componentID = "btSave", eventName = Events.ON_CLICK)
	public void onSave(final Event event)
	{
		LOG.info("Save");
		final String projectName = this.txtProjectName.getValue();
		String templateId = null;
		if (this.cbxTemplate.getSelectedItem() != null)
		{
			final TextMasterApiTemplateDto tmpl = this.cbxTemplate.getSelectedItem().getValue();
			templateId = tmpl.getId();
		}

		textMasterProjectService.createProject(projectName, templateId, this.sourceLanguage, this.targetLanguage, this.account,
				this.cbxType.getSelectedItem().getValue(), Collections.EMPTY_LIST, Collections.EMPTY_LIST);

		this.closeWindowIfTemplate();
	}


	private void renderContent()
	{
		this.contentDiv.getChildren().clear();
		final Vlayout vlayout = new Vlayout();
		vlayout.setSclass("yw-wizard-layout");
		vlayout.setHeight("100%");
		vlayout.setSpacing("none");
		this.contentDiv.appendChild(vlayout);

		final Div stepContent = new Div();
		stepContent.setSclass("yw-wizard-content");
		stepContent.setVflex("1");
		stepContent.setHflex("1");
		vlayout.appendChild(stepContent);

		// Project Name
		final Div divProjectName = new Div();
		divProjectName.setWidth("100%");
		divProjectName.setStyle("margin-bottom:10px");
		final Div divNameLabel = new Div();
		final Label lblProjectName = new Label("Project Name");
		divNameLabel.getChildren().add(lblProjectName);
		divNameLabel.setWidth("100%");
		txtProjectName.setWidth("100%");

		divProjectName.getChildren().add(divNameLabel);
		divProjectName.getChildren().add(txtProjectName);


		// Language From
		final Div divLangFrom = new Div();
		divLangFrom.setWidth("100%");
		divLangFrom.setStyle("margin-bottom:10px");
		final Div divLangFromLbl = new Div();
		final Label lblFrom = new Label("From");
		divLangFromLbl.getChildren().add(lblFrom);
		divLangFromLbl.setWidth("100%");
		cbxLangFrom.setWidth("100%");
		cbxLangFrom.addEventListener(Events.ON_SELECT, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				refreshTemplateList();
			}
		});

		divLangFrom.getChildren().add(divLangFromLbl);
		divLangFrom.getChildren().add(cbxLangFrom);

		// Language To
		final Div divLangTo = new Div();
		divLangTo.setWidth("100%");
		divLangTo.setStyle("margin-bottom:10px");
		final Div divLangToLbl = new Div();
		final Label lblLangTo = new Label("To");
		divLangToLbl.getChildren().add(lblLangTo);
		divLangToLbl.setWidth("100%");
		cbxLangTo.setWidth("100%");
		cbxLangTo.addEventListener(Events.ON_SELECT, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				refreshTemplateList();
			}
		});
		divLangTo.getChildren().add(divLangToLbl);
		divLangTo.getChildren().add(cbxLangTo);

		final Collection<LanguageModel> languages = commerceCommonI18NService.getAllLanguages();
		languages.forEach(l -> {
			final Comboitem itemFrom = new Comboitem();
			itemFrom.setLabel(l.getName());
			itemFrom.setValue(l);
			cbxLangFrom.appendChild(itemFrom);
			final Comboitem itemTo = new Comboitem();
			itemTo.setLabel(l.getName());
			itemTo.setValue(l);
			cbxLangTo.appendChild(itemTo);
		});

		// Template ID
		final Div divTemplate = new Div();
		divTemplate.setWidth("100%");
		divTemplate.setStyle("margin-bottom:10px");
		final Div divTemplateLbl = new Div();
		final Label lblTemplate = new Label("Template");
		divTemplateLbl.getChildren().add(lblTemplate);
		divTemplateLbl.setWidth("100%");

		cbxTemplate.setWidth("100%");
		cbxTemplate.setDisabled(Boolean.TRUE);

		divTemplate.getChildren().add(divTemplateLbl);
		divTemplate.getChildren().add(cbxTemplate);

		// Type
		final Div divType = new Div();
		divType.setWidth("100%");
		divType.setStyle("margin-bottom:10px");
		final Div divTypeLbl = new Div();
		final Label lblType = new Label("Type");
		divTypeLbl.getChildren().add(lblType);
		divTypeLbl.setWidth("100%");

		cbxType.setReadonly(Boolean.TRUE);
		final Comboitem itemProduct = new Comboitem("Product");
		itemProduct.setValue(typeService.getComposedTypeForClass(ProductModel.class));
		final Comboitem itemCategory = new Comboitem("Category");
		itemCategory.setValue(typeService.getComposedTypeForClass(CategoryModel.class));
		final Comboitem itemCMS = new Comboitem("CMS");
		itemCategory.setValue(typeService.getComposedTypeForClass(CMSItemModel.class));

		cbxType.getChildren().add(itemProduct);
		cbxType.getChildren().add(itemCategory);
		cbxType.getChildren().add(itemCMS);

		cbxType.setWidth("100%");

		divType.getChildren().add(divTypeLbl);
		divType.getChildren().add(cbxType);

		stepContent.getChildren().add(divProjectName);
		stepContent.getChildren().add(divLangFrom);
		stepContent.getChildren().add(divLangTo);
		stepContent.getChildren().add(divTemplate);
		stepContent.getChildren().add(divType);
	}


	private void renderBreadcrumb()
	{
		// yw-wizard-breadcrumb yw-wizard-breadcrumb-before
		// yw-wizard-breadcrumb yw-wizard-breadcrumb-after
		this.breadcrumbDiv.getChildren().clear();
		final HtmlBasedComponent bc1 = createBreadcrumbEntry("Project Info", "Define project Global Information");
		bc1.setSclass("yw-wizard-breadcrumb yw-wizard-breadcrumb-active");
		bc1.setStyle("z-index: 4");
		this.breadcrumbDiv.appendChild(bc1);

		//		final HtmlBasedComponent bc2 = createBreadcrumbEntry("Attribute Selection", "Choose project type");
		//		bc2.setSclass("yw-wizard-breadcrumb yw-wizard-breadcrumb-active");
		//		bc2.setStyle("z-index: 3");
		//		this.breadcrumbDiv.appendChild(bc2);
		//
		//		final HtmlBasedComponent bc3 = createBreadcrumbEntry("Data Filtering", "");
		//		bc3.setSclass("yw-wizard-breadcrumb yw-wizard-breadcrumb-after");
		//		bc3.setStyle("z-index: 2");
		//		this.breadcrumbDiv.appendChild(bc3);
	}

	private HtmlBasedComponent createBreadcrumbEntry(final String title, final String subTitle)
	{
		final Div ret = new Div();
		final Div labelCnt = new Div();
		ret.appendChild(labelCnt);
		labelCnt.appendChild(new Label(title));
		labelCnt.setSclass("yw-wizard-breadcrumb-label");
		final Div sublabelCnt = new Div();
		ret.appendChild(sublabelCnt);
		sublabelCnt.appendChild(new Label(subTitle));
		sublabelCnt.setSclass("yw-wizard-breadcrumb-sublabel");
		final Div spacerDiv = new Div();
		spacerDiv.setSclass("yw-wizard-breadcrumb-spacer");
		ret.appendChild(spacerDiv);
		final Div spacerArrowDiv = new Div();
		spacerArrowDiv.setSclass("yw-wizard-breadcrumb-spacerarrow");
		ret.appendChild(spacerArrowDiv);
		final Div arrowDiv = new Div();
		arrowDiv.setSclass("yw-wizard-breadcrumb-arrow");
		ret.appendChild(arrowDiv);
		return ret;
	}

	private void refreshTemplateList()
	{
		LOG.info("Refresh Template List");
		// Refresh template list
		this.cbxTemplate.getItems().clear();

		// Check selected Language
		sourceLanguage = null;
		targetLanguage = null;
		if (cbxLangFrom.getSelectedItem() != null)
		{
			final LanguageModel l = cbxLangFrom.getSelectedItem().getValue();
			sourceLanguage = commerceCommonI18NService.getTextMasterLanguageForCommerceLanguage(l.getIsocode());
		}
		if (cbxLangTo.getSelectedItem() != null)
		{
			final LanguageModel l = cbxLangTo.getSelectedItem().getValue();
			targetLanguage = commerceCommonI18NService.getTextMasterLanguageForCommerceLanguage(l.getIsocode());
		}

		if (sourceLanguage != null && targetLanguage != null)
		{
			this.templateList.stream().forEach(t -> {
				final Comboitem item = new Comboitem(t.getName());
				item.setValue(t);
				this.cbxTemplate.getChildren().add(item);

			});
		}


		// Display
		if (cbxTemplate.getItems().size() == 0)
		{
			this.cbxTemplate.setDisabled(true);
		}
		else
		{
			this.cbxTemplate.setDisabled(false);
		}
	}

	private void closeWindowIfTemplate()
	{
		final WidgetInstance currentInstance = this.getWidgetslot().getWidgetInstance();
		if (currentInstance != null && currentInstance.getWidget().isTemplate())
		{
			final WidgetContainer parentWidgetContainer = WidgetTreeUIUtils.getParentWidgetContainer(this.getWidgetslot());
			this.widgetInstanceFacade.removeWidgetInstance(currentInstance);
			if (parentWidgetContainer != null)
			{
				parentWidgetContainer.updateView();
			}
		}

	}
}
