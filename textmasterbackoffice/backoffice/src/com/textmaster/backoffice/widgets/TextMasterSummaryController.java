package com.textmaster.backoffice.widgets;

import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.backoffice.constants.TextmasterbackofficeConstants;
import com.textmaster.core.constants.TextmastercoreConstants;
import com.textmaster.core.dtos.TextMasterApiTemplateDto;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterLanguageModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.model.TextMasterProjectTaskModel;
import com.textmaster.core.services.TextMasterLanguageService;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tabbox;

import java.util.*;
import java.util.stream.Collectors;


public class TextMasterSummaryController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterSummaryController.class);

	// Component
	private Label accountLabel;
	private Label nameLabel;
	private Label templatesLabel;
	private Label typeLabel;
	private Label attributesLabel;
	private Button sendButton;
	private Label counterLabel;
	private Tabbox summaryTabs;
	private Label noAccountSelectedLabel;
	private Grid summaryGrid;

	// Local data
	private TextMasterAccountModel account;
	private String name;
	private Collection<TextMasterApiTemplateDto> templates = new ArrayList<>();
	private ComposedTypeModel type;
	private List<AttributeDescriptorModel> attributes = new ArrayList<>();
	private List<ItemModel> items = new ArrayList<>();

	// Spring
	@WireVariable
	private TextMasterProjectService textMasterProjectService;

	@WireVariable
	private ConfigurationService configurationService;

	@WireVariable
	private I18NService i18NService;

	@WireVariable
	private ModelService modelService;

	@WireVariable
	private TaskService taskService;

	@WireVariable
	private CommonI18NService commonI18NService;

	@WireVariable
	private TextMasterLanguageService textMasterLanguageService;

	@WireVariable
	private NotificationService notificationService;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
	}

	@SocketEvent(socketId = "init")
	public void storeInitData(final Map<String, Object> data)
	{
		account = (TextMasterAccountModel) data.get(TextmasterbackofficeConstants.ProjectParameters.ACCOUNT);
		name = (String) data.get(TextmasterbackofficeConstants.ProjectParameters.NAME);
		type = (ComposedTypeModel) data.get(TextmasterbackofficeConstants.ProjectParameters.COMPOSED_TYPE);
		attributes = (List<AttributeDescriptorModel>) data.get(TextmasterbackofficeConstants.ProjectParameters.ATTRIBUTES);
		templates = (Collection) data.get(TextmasterbackofficeConstants.ProjectParameters.TEMPLATES);

		this.refreshDisplay();

		// Display form
		summaryGrid.setVisible(true);
		noAccountSelectedLabel.setVisible(false);
	}

	@SocketEvent(socketId = "items")
	public void storeItems(final List<ItemModel> itemsSelected)
	{
		// If no items selected, it could be the case of first document list loading.
		// In all cases, do nothing.
		if (CollectionUtils.isEmpty(itemsSelected)) {
			return;
		}

		Integer maxItems = configurationService.getConfiguration().getInt(TextmastercoreConstants.Documents.SELECTION_MAX);

		// Get locale source to send correct content
		Collection<Locale> locales = this.templates
				.stream()
				.map(t -> textMasterLanguageService.getForIsocode(t.getLanguageFrom()))
				.map(l -> getCommonI18NService().getLocaleForLanguage(l.getLanguage()))
				.collect(Collectors.toList());

		// Filter documents to keep only documents with at least 1 attribute which have a value available for source language
		List<ItemModel> itemsToAdd = itemsSelected
				.stream()
				.filter(item -> attributes
						.stream()
						.anyMatch(a -> filter(item, a, locales)))
				.collect(Collectors.toList());

		if (maxItems > 0 && itemsSelected.size() > maxItems)
		{
			getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.WARNING, getLabel("documentsmaxreached", new Object[] { String.valueOf(maxItems) }));
			return;
		}

		// Prevent added empty documents
		if (itemsToAdd.size() == 0)
		{
			getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.FAILURE, getLabel("documentsallempty"));
			return;
		}

		// Display notification to user
		getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.SUCCESS, getLabel("documentsaddedtoproject", new Object[] { String.valueOf(itemsToAdd.size()) }));

		int numberOfDocumentsNotAdded = itemsSelected.size() - itemsToAdd.size();
		if (numberOfDocumentsNotAdded > 0)
		{
			getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.INFO, getLabel("documentsnotaddedtoproject", new Object[] { String.valueOf(numberOfDocumentsNotAdded) }));
		}

		// Store data
		items = itemsToAdd;

		this.refreshDisplay();
	}

	/**
	 * Filter item to check if a value exists for attribute and locale
	 *
	 * @param item
	 * @param attribute
	 * @param locales
	 * @return
	 */
	protected boolean filter(ItemModel item, AttributeDescriptorModel attribute, Collection<Locale> locales)
	{
		return locales
				.stream()
				.anyMatch(l -> {
					Object value = getModelService().getAttributeValue(item, attribute.getQualifier(), l);
					return StringUtils.isNotBlank((String) value);
				});
	}

	/**
	 * Display project summary.
	 */
	protected void refreshDisplay()
	{
		accountLabel.setValue(this.account.getName());
		nameLabel.setValue(this.name);
		templatesLabel.setValue(this.templates.stream().map(t -> t.getName()).collect(Collectors.joining(", ")));
		typeLabel.setValue(this.type.getName());
		attributesLabel.setValue(this.attributes.stream()
				.map(a -> a.getQualifier())
				.collect(Collectors.joining(","))
		);
		counterLabel.setValue("" + items.size());

		// Active button
		if (items.size() > 0 && CollectionUtils.isNotEmpty(this.templates) && StringUtils.isNotBlank(account.getApiKey()))
		{
			sendButton.setDisabled(false);
		}
		else
		{
			sendButton.setDisabled(true);
		}
	}

	@ViewEvent(componentID = "sendButton", eventName = Events.ON_CLICK)
	public void createProjects()
	{
		for (TextMasterApiTemplateDto template : this.templates)
		{
			try
			{
				final TextMasterLanguageModel sourceLanguage = textMasterLanguageService.getForIsocode(template.getLanguageFrom());
				final TextMasterLanguageModel targetLanguage = textMasterLanguageService.getForIsocode(template.getLanguageTo());

				// Create project on Hybris and TextMaster platform
				TextMasterProjectModel project = textMasterProjectService
						.createAndPushProject(name, template, sourceLanguage, targetLanguage, account, type, attributes, items);

				// Schedule the task to send all documents and finalize project
				final TextMasterProjectTaskModel task = getModelService().create(TextMasterProjectTaskModel.class);
				task.setRunnerBean("textMasterProjectPushContentTaskRunner");
				task.setExecutionDate(new Date());
				task.setTextMasterProject(project);
				getModelService().save(task);
				getTaskService().scheduleTask(task);
			}
			catch (Exception e)
			{
				LOG.error("Impossible to create project: " + e.getMessage(), e);
				getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.WARNING, getLabel("projectnotcreated"));
			}
		}

		// Display confirmation message
		getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.SUCCESS, getLabel("projectcreated"));

		// Reset other components
		getModelService().refresh(account);
		sendOutput("account", account);
		reset();
		sendOutput("reset", Collections.EMPTY_MAP);

		// Select automatically the dashboard
		SimpleNode node = new SimpleNode("textmaster-widget-dashboard-tab");
		sendOutput("selectPerspective", node);
	}

	/**
	 * Receive reset message
	 */
	@SocketEvent(socketId = "reset")
	public void receiveReset()
	{
		reset();
	}

	/**
	 * Reset widget
	 */
	protected void reset()
	{
		// Reset UI components
		accountLabel.setValue(StringUtils.EMPTY);
		nameLabel.setValue(StringUtils.EMPTY);
		templatesLabel.setValue(StringUtils.EMPTY);
		typeLabel.setValue(StringUtils.EMPTY);
		attributesLabel.setValue(StringUtils.EMPTY);
		counterLabel.setValue("0");
		sendButton.setDisabled(false);

		// Reset data stored
		account = null;
		name = StringUtils.EMPTY;
		templates = CollectionUtils.EMPTY_COLLECTION;
	}

	public TextMasterProjectService getTextMasterProjectService()
	{
		return textMasterProjectService;
	}

	public void setTextMasterProjectService(TextMasterProjectService textMasterProjectService)
	{
		this.textMasterProjectService = textMasterProjectService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected TaskService getTaskService()
	{
		return taskService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
