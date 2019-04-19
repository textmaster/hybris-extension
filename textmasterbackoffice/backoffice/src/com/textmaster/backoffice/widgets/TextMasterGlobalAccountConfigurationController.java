package com.textmaster.backoffice.widgets;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.backoffice.components.ComposedTypeTreeNode;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterConfigurationModel;
import com.textmaster.core.model.TextMasterItemTypeModel;
import com.textmaster.core.services.*;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;


public class TextMasterGlobalAccountConfigurationController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterGlobalAccountConfigurationController.class);

	private Combobox comboLanguageSource;
	private Combobox comboLanguageTarget;
	private Tree typesTree;
	private Listbox attributesListBox;
	private Listbox typesListBox;
	private Label typesListBoxErrorLabel;
	private Label attributesListBoxErrorLabel;
	private Label noAccountSelectedLabel;
	private Tabbox configurationTabs;

	// Spring Injection
	@WireVariable
	private TypeService typeService;

	@WireVariable
	private TextMasterAccountService textMasterAccountService;

	@WireVariable
	private TextMasterConfigurationService textMasterConfigurationService;

	@WireVariable
	private TextMasterProjectService textMasterProjectService;

	@WireVariable
	private TypeFacade typeFacade;

	@WireVariable
	private TextMasterCommerceCommonI18NService commerceCommonI18NService;

	@WireVariable
	private TextMasterDocumentService textMasterDocumentService;

	@WireVariable
	private NotificationService notificationService;

	private DefaultTreeModel<ComposedTypeModel> typesTreeModel;
	private TextMasterAccountModel textmasterAccount;
	private HashMap<ComposedTypeModel, Collection<AttributeDescriptorModel>> selectedAttributeMap;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		resetConfigurationWidget();
	}

	protected void resetConfigurationWidget()
	{
		// Reset selection
		selectedAttributeMap = new HashMap<ComposedTypeModel, Collection<AttributeDescriptorModel>>();

		// Laod all languages
		loadLanguageList(comboLanguageSource);
		loadLanguageList(comboLanguageTarget);

		// Load root types and its subtypes
		final ComposedTypeTreeNode rootNode = new ComposedTypeTreeNode(null);
		final List<ComposedTypeModel> composedTypes = textMasterDocumentService.getComposedTypes();
		composedTypes.stream().forEach(t -> {
			final ComposedTypeTreeNode node = buildComposedTypeTreeNode(t);
			rootNode.add(node);
		});

		// Create tree model
		typesTreeModel = new DefaultTreeModel<ComposedTypeModel>(rootNode);
		typesTreeModel.setMultiple(true);
		typesTree.setModel(typesTreeModel);

		// Clear attributes list box
		typesListBox.getItems().clear();
		attributesListBox.getItems().clear();
	}

	@SocketEvent(socketId = "accountSelected")
	public void accountSelected(final TextMasterAccountModel account)
	{
		// Clear the widget
		resetConfigurationWidget();

		// Get TextMaster account
		this.textmasterAccount = account;

		// Check if the account already have a configuration
		if (account.getConfiguration() != null)
		{
			// Select Source and Target languages
			selectLanguage(comboLanguageSource, account.getConfiguration().getDefaultSourceLanguage());
			selectLanguage(comboLanguageTarget, account.getConfiguration().getDefaultTargetLanguage());

			// Load Selected types and attributes
			loadTypesAndAttributes(account.getConfiguration());

			// Update selected types in tree
			selectDefaultTypeTreeNode();

			// Update selected types list box
			updateSelectedTypes();
		}

		// Display configuration form
		configurationTabs.setVisible(true);
		noAccountSelectedLabel.setVisible(false);
	}

	/**
	 * Create a tree node from composed type root element
	 *
	 * @param configuration
	 */
	protected void loadTypesAndAttributes(final TextMasterConfigurationModel configuration)
	{
		if (configuration != null)
		{
			// Clear the map
			selectedAttributeMap.clear();

			// Add all types and attributes
			for (final TextMasterItemTypeModel itemType : configuration.getTranslatableList())
			{
				selectedAttributeMap.put(itemType.getTranslatedItemType(), itemType.getPreselectedAttributes());
			}
		}
	}

	/**
	 * Create a tree node from composed type root element
	 *
	 * @param composedType
	 * @return ComposedTypeTreeNode
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

	/**
	 * Select all default elements
	 */
	protected void selectDefaultTypeTreeNode()
	{
		final List<ComposedTypeTreeNode> fullList = new ArrayList<>();

		final List<ComposedTypeTreeNode> nodes = typesTree.getItems()
				.stream()
				.map(item -> (ComposedTypeTreeNode) item.getValue())
				.filter(node -> selectedAttributeMap.containsKey(node.getData()))
				.collect(Collectors.toList());
		fullList.addAll(nodes);

		final LinkedList<List<ComposedTypeTreeNode>> queue = new LinkedList<>();
		queue.add(nodes);
		while (!queue.isEmpty())
		{
			final List<ComposedTypeTreeNode> nodeList = queue.remove();
			for (final ComposedTypeTreeNode node : nodeList)
			{
				if (CollectionUtils.isNotEmpty(node.getChildren()))
				{
					List<ComposedTypeTreeNode> composedTypeNodes = node.getChildren()
							.stream()
							.map(item -> (ComposedTypeTreeNode) item)
							.filter(composedTypeNode -> selectedAttributeMap.containsKey(composedTypeNode.getData()))
							.collect(Collectors.toList());
					fullList.addAll(composedTypeNodes);

					queue.add(composedTypeNodes);
				}
			}
		}

		typesTreeModel.setSelection(fullList);
	}

	/**
	 * Get all languages availables and add them to the comboBox
	 *
	 * @param comboBox
	 */
	protected void loadLanguageList(final Combobox comboBox)
	{
		final Collection<LanguageModel> languages = commerceCommonI18NService.getAllLanguages();

		languages.stream().forEach(l -> {
			final Comboitem item = new Comboitem(l.getName());
			item.setValue(l.getIsocode());
			comboBox.appendChild(item);
		});
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

	/**
	 * Update selected types depending on types tree
	 */
	@ViewEvent(componentID = "typesTree", eventName = Events.ON_SELECT)
	public void updateSelectedTypes()
	{
		// Get selected types
		final Set<Treeitem> selectedTypesItem = typesTree.getSelectedItems();

		// Update typesListbox with those values
		typesListBox.getItems().clear();
		attributesListBox.getItems().clear();
		selectedTypesItem
				.stream()
				.forEach(sti -> {
					ComposedTypeModel type = getComposedType(sti);
					typesListBox.appendItem(type.getName(), type.getCode());
					selectedAttributeMap.putIfAbsent(type, Collections.EMPTY_LIST);
				});

		// Remove type from attribute list
		if (MapUtils.isNotEmpty(selectedAttributeMap))
		{
			List<Map.Entry> entriesToRemove = selectedAttributeMap.entrySet()
					.stream()
					.filter(e ->
							selectedTypesItem
									.stream()
									.map(sti -> getComposedType(sti))
									.noneMatch(type -> ((ComposedTypeModel) e.getKey()).getCode().equals(type.getCode()))
					)
					.collect(Collectors.toList());

			entriesToRemove
					.stream()
					.forEach(e -> selectedAttributeMap.remove(e.getKey()));
		}

		if (CollectionUtils.isNotEmpty(typesListBox.getItems()))
		{
			typesListBox.setVisible(true);
			typesListBoxErrorLabel.setVisible(false);
			attributesListBox.setVisible(false);
			attributesListBoxErrorLabel.setVisible(true);
		}
		else
		{
			typesListBox.setVisible(false);
			attributesListBox.setVisible(false);
			typesListBoxErrorLabel.setVisible(true);
			attributesListBoxErrorLabel.setVisible(true);
		}
	}

	/**
	 * Retrieve composed type from TreeItem.
	 *
	 * @param sti
	 * @return
	 */
	protected ComposedTypeModel getComposedType(Treeitem sti)
	{
		final ComposedTypeTreeNode node = (ComposedTypeTreeNode) sti.getValue();
		final ComposedTypeModel model = node.getData();
		return typeService.getComposedTypeForCode(model.getCode());
	}

	/**
	 * Update selected attributes depending on selected type
	 */
	@ViewEvent(componentID = "typesListBox", eventName = Events.ON_SELECT)
	public void loadAttributes()
	{
		final Listitem selectedItem = typesListBox.getSelectedItem();
		final ComposedTypeModel type = typeService.getComposedTypeForCode(selectedItem.getValue());

		// Load attributes
		final Set<AttributeDescriptorModel> attributes = typeService.getAttributeDescriptorsForType(type);

		// Clear list box
		attributesListBox.getItems().clear();

		// Fill attributes list box
		attributes.stream().filter(a -> a.getLocalized()).forEach(a -> {
			attributesListBox.appendItem(a.getQualifier(), a.getQualifier());
		});

		// Preselect attributes items
		final Collection<AttributeDescriptorModel> selectedAttributes = selectedAttributeMap.get(type);

		for (final Listitem item : attributesListBox.getItems())
		{
			if (selectedAttributes != null && selectedAttributes.contains(typeService.getAttributeDescriptor(type, item.getValue())))
			{
				item.setSelected(Boolean.TRUE);
			}
		}

		if (CollectionUtils.isNotEmpty(attributesListBox.getItems()))
		{
			attributesListBox.setVisible(true);
			attributesListBoxErrorLabel.setVisible(false);
		}
		else
		{
			attributesListBox.setVisible(false);
			attributesListBoxErrorLabel.setVisible(true);
		}
	}

	/**
	 * Update selected attributes map depending on selected attributes listbox
	 */
	@ViewEvent(componentID = "attributesListBox", eventName = Events.ON_SELECT)
	public void selectAttribute()
	{
		final Listitem selectedType = typesListBox.getSelectedItem();
		final Set<Listitem> selectedItems = attributesListBox.getSelectedItems();
		final List<AttributeDescriptorModel> selectedAttributes = new ArrayList<AttributeDescriptorModel>();

		final ComposedTypeModel type = typeService.getComposedTypeForCode(typesListBox.getSelectedItem().getValue());

		selectedItems.stream().forEach(si -> {
			selectedAttributes.add(typeService.getAttributeDescriptor(type, si.getValue()));
		});

		selectedAttributeMap.put(type, selectedAttributes);
	}

	/**
	 * Save the TextMasterConfiguration
	 */
	@ViewEvent(componentID = "saveButton", eventName = Events.ON_CLICK)
	public void saveConfiguration()
	{
		String sourceLanguageIsoCode = null;
		if (comboLanguageSource.getSelectedItem() != null)
		{
			sourceLanguageIsoCode = comboLanguageSource.getSelectedItem().getValue();
		}

		String targetLanguageIsoCode = null;
		if (comboLanguageTarget.getSelectedItem() != null)
		{
			targetLanguageIsoCode = comboLanguageTarget.getSelectedItem().getValue();
		}

		try
		{
			getTextMasterConfigurationService()
					.save(textmasterAccount, sourceLanguageIsoCode, targetLanguageIsoCode, selectedAttributeMap);

			// Display confirmation message
			getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.SUCCESS, getLabel("configurationsavedsuccess"));

		}
		catch (Exception e)
		{
			// Display confirmation message
			getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.FAILURE, getLabel("configurationsavederror"));
		}

	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	protected TextMasterAccountService getTextMasterAccountService()
	{
		return textMasterAccountService;
	}

	protected TextMasterConfigurationService getTextMasterConfigurationService()
	{
		return textMasterConfigurationService;
	}

	protected TextMasterProjectService getTextMasterProjectService()
	{
		return textMasterProjectService;
	}

	protected TypeFacade getTypeFacade()
	{
		return typeFacade;
	}

	protected TextMasterCommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
