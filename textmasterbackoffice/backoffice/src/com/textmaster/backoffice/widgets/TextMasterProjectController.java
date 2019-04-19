/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.textmaster.backoffice.widgets;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.backoffice.components.ComposedTypeTreeNode;
import com.textmaster.backoffice.constants.TextmasterbackofficeConstants;
import com.textmaster.backoffice.exceptions.ValidationException;
import com.textmaster.core.constants.TextmastercoreConstants;
import com.textmaster.core.dtos.TextMasterApiTemplateDto;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.services.*;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;


public class TextMasterProjectController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectController.class);

	// Components
	private Combobox sourceLanguageComboBox;
	private Listbox targetLanguagesListBox;
	private Label targetLanguagesErrorLabel;
	private Textbox nameTextBox;
	private Listbox attributesListBox;
	private Label attributesErrorLabel;
	private Label noAccountSelectedLabel;
	private Tabbox projectTabs;
	private Tree typesTree;
	private Grid templatesGrid;

	@WireVariable
	private TextMasterAccountService textMasterAccountService;

	@WireVariable
	private TypeService typeService;

	@WireVariable
	private TextMasterProjectService textMasterProjectService;

	@WireVariable
	private TextMasterDocumentService textMasterDocumentService;

	@WireVariable
	private TextMasterCommerceCommonI18NService commerceCommonI18NService;

	@WireVariable
	private TextMasterLanguageService textMasterLanguageService;

	@WireVariable
	private TextMasterConfigurationService textMasterConfigurationService;

	// Local storage
	private TextMasterAccountModel account;
	private List<TextMasterApiTemplateDto> templateList;
	private Map<String, String> templatesSelected = new HashMap<>();
	private TreeModel<TreeNode<ComposedTypeModel>> typesTreeModel;
	private String sourceLanguageSelected;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);

		// Load hybris languages
		final Collection<LanguageModel> languages = commerceCommonI18NService.getAllLanguages();
		languages
				.stream()
				.forEach(l -> {
					final Comboitem sourceItem = new Comboitem(l.getName());
					sourceItem.setValue(l.getIsocode());
					sourceLanguageComboBox.appendChild(sourceItem);
					targetLanguagesListBox.appendItem(l.getName(), l.getIsocode());
				});

		sourceLanguageComboBox.setSelectedIndex(0);
		sourceLanguageSelected = TextmasterbackofficeConstants.UI.NONE_VALUE;
		targetLanguagesListBox.setSelectedItems(SetUtils.EMPTY_SET);

		// Display
		if (targetLanguagesListBox.getItems().size() == 0)
		{
			targetLanguagesErrorLabel.setVisible(true);
			targetLanguagesListBox.setVisible(false);
		}
		else
		{
			targetLanguagesErrorLabel.setVisible(false);
			targetLanguagesListBox.setVisible(true);
		}

		final ComposedTypeTreeNode rootNode = new ComposedTypeTreeNode(null);
		final List<ComposedTypeModel> composedTypes = textMasterDocumentService.getComposedTypes();
		composedTypes
				.stream()
				.forEach(t -> {
					final ComposedTypeTreeNode productRootNode = buildComposedTypeTreeNode(t);
					rootNode.add(productRootNode);
				});

		// create tree model
		typesTreeModel = new DefaultTreeModel<ComposedTypeModel>(rootNode);
		typesTree.setModel(typesTreeModel);
	}

	@SocketEvent(socketId = "accountSelected")
	public void accountSelected(final TextMasterAccountModel account)
	{
		// Get account
		this.account = account;

		// Load templates available on Text Master platform
		templateList = textMasterProjectService.getTemplates(this.account.getApiKey(), this.account.getApiSecret());

		// Select prefered language
		if (account.getConfiguration() != null)
		{
			selectLanguage(sourceLanguageComboBox, account.getConfiguration().getDefaultSourceLanguage());
		}
		else
		{
			sourceLanguageComboBox.setSelectedItem(null);
		}

		// Open first tab by default
		projectTabs.setSelectedIndex(0);

		// Display form
		projectTabs.setVisible(true);
		noAccountSelectedLabel.setVisible(false);
	}

	/**
	 * Create a tree node from composed type root element
	 *
	 * @param composedType
	 * @return
	 */
	protected ComposedTypeTreeNode buildComposedTypeTreeNode(final ComposedTypeModel composedType)
	{
		final ComposedTypeTreeNode typeNode = new ComposedTypeTreeNode(composedType);
		final LinkedList<ComposedTypeTreeNode> queue = new LinkedList<ComposedTypeTreeNode>();
		queue.add(typeNode);
		while (!queue.isEmpty())
		{
			final ComposedTypeTreeNode node = queue.remove();
			for (final ComposedTypeModel childType : node.getData().getSubtypes())
			{
				final ComposedTypeTreeNode child = new ComposedTypeTreeNode(childType);
				node.add(child);
				queue.add(child);
			}
		}
		return typeNode;
	}

	@ViewEvent(componentID = "typesTree", eventName = Events.ON_SELECT)
	public void loadAttributes()
	{
		// TODO: Validate if any line is selected
		final TreeNode<ComposedTypeModel> selectedNode = ((DefaultTreeModel<ComposedTypeModel>) typesTreeModel).getSelection()
				.stream().findFirst().get();
		final ComposedTypeModel type = selectedNode.getData();

		// Load attributes
		final Set<AttributeDescriptorModel> attributes = typeService.getAttributeDescriptorsForType(type);

		// Clear list box
		attributesListBox.getItems().clear();
		attributes.stream().filter(a -> "localized:java.lang.String".equals(a.getAttributeType().getCode()) && a.getLocalized())
				.forEach(a -> {
					attributesListBox.appendItem(a.getQualifier(), a.getQualifier());
				});

		// Preselect attributes items
		final Collection<AttributeDescriptorModel> preselectedAttributes = textMasterConfigurationService
				.getPreselectedAttributesForType(account, type);
		for (final Listitem item : attributesListBox.getItems())
		{
			if (preselectedAttributes != null
					&& preselectedAttributes.contains(typeService.getAttributeDescriptor(type, item.getValue())))
			{
				item.setSelected(Boolean.TRUE);
			}
		}

		// Display
		if (attributesListBox.getItems().size() == 0)
		{
			attributesErrorLabel.setVisible(true);
			attributesListBox.setVisible(false);
		}
		else
		{
			attributesErrorLabel.setVisible(false);
			attributesListBox.setVisible(true);
		}
	}

	@ViewEvent(componentID = "sourceLanguageComboBox", eventName = Events.ON_SELECT)
	public void loadSourceLanguages(final SelectEvent<Comboitem, Object> event)
	{
		final Comboitem selectedItem = event.getSelectedItems().iterator().next();

		if (MapUtils.isNotEmpty(templatesSelected))
		{
			// Ask confirmation to user
			Messagebox
					.show(getLabel("changing.language.source.content"),
							getLabel("changing.language.source.title"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
							new EventListener<Event>()
							{
								@Override public void onEvent(Event event) throws Exception
								{
									if (event.getName().equals("onOK"))
									{
										processLoadSourceLanguages(selectedItem);
									}
									else
									{
										// Reset selected element
										sourceLanguageComboBox.setSelectedItem(sourceLanguageComboBox.getItems()
												.stream()
												.filter(i -> i.getValue().equals(sourceLanguageSelected))
												.findFirst()
												.get());
									}
								}
							});
		}
		else
		{
			processLoadSourceLanguages(selectedItem);
		}
	}

	/**
	 * Load data when source language change.
	 *
	 * @param selectedItem
	 */
	protected void processLoadSourceLanguages(final Comboitem selectedItem)
	{
		// Replace actual source language selected
		sourceLanguageSelected = selectedItem.getValue();

		// Clean templates previously selected
		templatesSelected = new HashMap<>();

		// Refresh templates for all target languages
		refreshTemplateList();
	}

	@ViewEvent(componentID = "targetLanguagesListBox", eventName = Events.ON_SELECT)
	public void selectTargetLanguages()
	{
		// Delete all template selected for target languages which are not checked
		final Collection<TextMasterLanguageModel> targetLanguages = convertLanguages(targetLanguagesListBox.getSelectedItems());
		Collection<String> isocodes = this.templatesSelected.keySet()
				.stream()
				.filter(templateIsocode -> targetLanguages
						.stream()
						.map(i -> i.getIsocode())
						.noneMatch( isocode -> isocode.equalsIgnoreCase(templateIsocode))
				)
				.collect(Collectors.toList());
		isocodes.forEach(isocode -> this.templatesSelected.remove(isocode));

		// Refresh templates for all target languages
		refreshTemplateList();
	}

	/**
	 * Refresh template list
	 */
	protected void refreshTemplateList()
	{
		final String sourceLanguageValue = (sourceLanguageComboBox.getSelectedItem() != null)
				? sourceLanguageComboBox.getSelectedItem().getValue()
				: TextmasterbackofficeConstants.UI.NONE_VALUE;

		final TextMasterLanguageModel sourceLanguage = commerceCommonI18NService
				.getTextMasterLanguageForCommerceLanguage(sourceLanguageValue);

		if (sourceLanguage == null) {
			return;
		}

		// Clear all rows
		templatesGrid.getRows().getChildren().clear();

		final Collection<TextMasterLanguageModel> targetLanguages = convertLanguages(targetLanguagesListBox.getSelectedItems());

		for (TextMasterLanguageModel language : targetLanguages)
		{
			// Add dynamically templates by target languages
			Row row = new Row();
			row.setStyle("vertical-align: top");
			Label labelTemplate = new Label(
					getLabel("templateForLanguage", new Object[] { String.valueOf(language.getLanguage().getName()) }));
			Vlayout vLayout = new Vlayout();
			Combobox combobox = new Combobox();
			combobox.setVisible(true);
			combobox.setReadonly(true);

			// Add default value for template list
			final Comboitem sourceItem = new Comboitem(getLabel("notemplate"));
			sourceItem.setValue(TextmasterbackofficeConstants.UI.NONE_VALUE);
			combobox.appendChild(sourceItem);

			// Add events
			combobox.addEventListener(Events.ON_SELECT, event -> {
				this.templatesSelected.put(language.getIsocode(), ((Combobox) event.getTarget()).getSelectedItem().getValue());
			});

			// Define template list
			templateList
					.stream()
					.filter(t -> isLanguageDefined(t.getLanguageFrom(), Collections.singletonList(sourceLanguage))
							&& isLanguageDefined(t.getLanguageTo(), targetLanguages)
							&& t.getLanguageTo().equalsIgnoreCase(language.getIsocode()))
					.forEach(t -> {
						final Comboitem item = new Comboitem(formatTemplateName(t));
						item.setValue(t.getId());
						combobox.appendChild(item);
					});

			Label labelError = new Label();
			labelError.setVisible(false);
			labelError.setValue(getLabel("notemplate"));

			// Display
			if (combobox.getItems()
					.stream()
					.filter(i -> !i.getValue().equals(TextmasterbackofficeConstants.UI.NONE_VALUE))
					.collect(Collectors.counting()) == 0)
			{
				if (TextmasterbackofficeConstants.UI.NONE_VALUE.equals(sourceLanguageValue)
						|| CollectionUtils.isEmpty(targetLanguagesListBox.getSelectedItems()))
				{
					labelError.setValue(getLabel("notemplate"));
				}
				else
				{
					labelError.setValue(getLabel("notemplatefound"));
				}

				labelError.setVisible(true);
				combobox.setVisible(false);
			}
			else
			{
				// Try to select template previously selected
				if (this.templatesSelected.containsKey(language.getIsocode()))
				{
					combobox.setSelectedItem(combobox.getItems()
							.stream()
							.filter(c -> !c.getValue().equals(TextmasterbackofficeConstants.UI.NONE_VALUE))
							.filter(c -> c.getValue().equals(
									this.templatesSelected.get(language.getIsocode())))
							.findFirst().get());
				}
				else
				{
					combobox.setSelectedIndex(0);
				}
				labelError.setVisible(false);
				combobox.setVisible(true);
			}

			vLayout.appendChild(combobox);
			vLayout.appendChild(labelError);
			row.appendChild(labelTemplate);
			row.appendChild(vLayout);

			templatesGrid.getRows().appendChild(row);
		}

		// Add dynamically the next button (as the last row)
		Row rowButton = new Row();
		Cell cell = new Cell();
		cell.setColspan(2);
		cell.setStyle("text-align:right");
		Button button = new Button();
		button.setLabel(getLabel("next"));
		button.addEventListener(Events.ON_CLICK, event -> {
			projectTabs.setSelectedIndex(2);
		});
		cell.appendChild(button);
		rowButton.appendChild(cell);

		templatesGrid.getRows().appendChild(rowButton);
	}

	/**
	 * Convert list item containing LanguageModel to collection containing TextMasterLanguageModel.
	 *
	 * @param items
	 * @return
	 */
	protected Collection<TextMasterLanguageModel> convertLanguages(Collection<Listitem> items)
	{
		return items
					.stream()
					.map(li -> commerceCommonI18NService.getTextMasterLanguageForCommerceLanguage(li.getValue()))
					.filter(l -> l != null)
					.collect(Collectors.toList());
	}

	/**
	 * Extract template from ID
	 *
	 * @param id
	 * @return
	 */
	protected TextMasterApiTemplateDto getTemplateFromId(String id)
	{
		return this.templateList
				.stream()
				.filter(t -> t.getId().equalsIgnoreCase(id))
				.findFirst().orElse(null);
	}

	/**
	 * Format display of one template
	 *
	 * @param template
	 * @return
	 */
	protected String formatTemplateName(final TextMasterApiTemplateDto template)
	{
		return String.format("%s", template.getName());
	}

	/**
	 * Verify if the template language exists in the languages list.
	 *
	 * @param templateLanguage
	 * @param languages
	 * @return
	 */
	protected boolean isLanguageDefined(final String templateLanguage, final Collection<TextMasterLanguageModel> languages)
	{
		if (CollectionUtils.isNotEmpty(languages) && languages.stream()
				.anyMatch(l -> templateLanguage.equalsIgnoreCase(l.getIsocode())))
		{
			return true;
		}

		return false;
	}

	@ViewEvent(componentID = "nextButtonTemplates", eventName = Events.ON_CLICK)
	public void nextTemplates()
	{
		projectTabs.setSelectedIndex(1);
	}

	@ViewEvent(componentID = "nextButtonDocuments", eventName = Events.ON_CLICK)
	public void nextDocuments()
	{
		projectTabs.setSelectedIndex(2);
	}

	@ViewEvent(componentID = "selectItemsButton", eventName = Events.ON_CLICK)
	public void save()
	{
		// Validate data (template, etc.)
		try
		{
			isFormValidated();
		}
		catch (ValidationException ve)
		{
			ve.getErrors()
					.stream()
					.forEach(e -> NotificationUtils.notifyUser(e, NotificationEvent.Type.FAILURE));
			return;
		}

		final TreeNode<ComposedTypeModel> selectedNode = ((DefaultTreeModel<ComposedTypeModel>) typesTreeModel).getSelection()
				.stream().findFirst().get();
		final ComposedTypeModel type = selectedNode.getData();

		final Set<Listitem> items = attributesListBox.getSelectedItems();
		final List<AttributeDescriptorModel> attributes = items.stream()
				.map(i -> typeService.getAttributeDescriptor(type, i.getValue())).collect(Collectors.toList());

		final String name = nameTextBox.getValue();

		final Map<String, Object> params = new HashMap<>();
		params.put(TextmasterbackofficeConstants.ProjectParameters.NAME, name);
		params.put(TextmasterbackofficeConstants.ProjectParameters.COMPOSED_TYPE, type);
		params.put(TextmasterbackofficeConstants.ProjectParameters.ACCOUNT, account);
		params.put(TextmasterbackofficeConstants.ProjectParameters.ATTRIBUTES, attributes);

		Collection<TextMasterApiTemplateDto> templates = templatesSelected.values()
				.stream()
				.map(id -> getTemplateFromId(id))
				.collect(Collectors.toList());

		params.put(TextmasterbackofficeConstants.ProjectParameters.TEMPLATES, templates);

		sendOutput("init", params);
		sendOutput("type", type.getCode());
	}

	/**
	 * Validate form.
	 *
	 * @throws ValidationException
	 */
	protected void isFormValidated() throws ValidationException
	{
		List<String> errors = new ArrayList<>();
		if (((DefaultTreeModel<ComposedTypeModel>) typesTreeModel).getSelection().size() == 0)
		{
			errors.add(getLabel("errors.types.notselected"));
		}
		if (attributesListBox.getSelectedItems().size() == 0)
		{
			errors.add(getLabel("errors.attributes.notselected"));
		}
		if (MapUtils.isEmpty(templatesSelected))
		{
			errors.add(getLabel("errors.templates.notselected"));
		}
		if (StringUtils.isBlank(nameTextBox.getValue()))
		{
			errors.add(getLabel("errors.names.empty"));
		}

		if (CollectionUtils.isNotEmpty(errors))
		{
			throw new ValidationException(errors);
		}
	}

	/**
	 * Reset widget
	 *
	 * @param data
	 */
	@SocketEvent(socketId = "reset")
	public void accountSelected(final Object data)
	{
		// Deselect UI elements
		templatesGrid.getRows().getChildren().clear();
		sourceLanguageComboBox.setSelectedIndex(0);
		targetLanguagesListBox.setSelectedItems(SetUtils.EMPTY_SET);
		nameTextBox.setValue(StringUtils.EMPTY);
		typesTree.clearSelection();
		attributesListBox.getItems().clear();
		attributesErrorLabel.setVisible(true);
		attributesListBox.setVisible(false);
		projectTabs.setSelectedIndex(0);
	}

	/**
	 * Select language depending on configuration
	 *
	 * @param comboBox
	 * @param language
	 */
	protected void selectLanguage(final Combobox comboBox, final LanguageModel language)
	{
		if (comboBox != null && language != null)
		{
			// Get language
			final List<Comboitem> itemList = comboBox.getItems();

			// Select laguage
			itemList.stream().forEach(i -> {

				if (language.getIsocode().equals(i.getValue()))
				{
					comboBox.setSelectedItem(i);
				}
			});
		}
	}
}
