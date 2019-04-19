package com.textmaster.backoffice.widgets;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.create.CreateContext;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterAccountService;
import com.textmaster.core.services.TextMasterProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class TextMasterAccountToolbarController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterAccountToolbarController.class);

	private Combobox comboAccounts;
	private Button btCreate;

	private Comboitem selectedItem = null;

	// Spring Injection
	@WireVariable
	private TextMasterAccountService textMasterAccountService;

	@WireVariable
	private TextMasterProjectService textMasterProjectService;

	@WireVariable
	private TypeFacade typeFacade;

	@WireVariable
	private NotificationService notificationService;


	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		super.setValue("typeCode", TextMasterProjectModel._TYPECODE);
	}

	@ViewEvent(componentID = "comboAccounts", eventName = Events.ON_SELECT)
	public void onComboSelect(final SelectEvent<Comboitem, Object> event)
	{
		if (!event.getSelectedItems().isEmpty())
		{
			final Comboitem itemSelected = event.getSelectedItems().iterator().next();

			// If this is not the first account selected, show confirmation message
			if (selectedItem != null)
			{
				Messagebox
						.show(getLabel("changingaccountquestioncontent"),
								getLabel("changingaccountquestiontitle"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
								new EventListener<Event>()
								{
									@Override public void onEvent(Event event) throws Exception
									{
										if (event.getName().equals("onOK"))
										{
											// Reset all components which contains data relatives to old account selected
											sendReset();

											sendOutput("accountSelectedForExternalComponents", itemSelected.getValue());
											selectAccountInternal(itemSelected);
										}
										else
										{
											// Reset selected element
											comboAccounts.setSelectedItem(selectedItem);
										}
									}
								});
			}
			else
			{
				sendOutput("accountSelectedForExternalComponents", itemSelected.getValue());
				selectAccountInternal(itemSelected);
			}
		}
	}

	/**
	 * Select an account into the bar
	 *
	 * @param item
	 */
	protected void selectAccountInternal(Comboitem item)
	{
		selectedItem = item;
		comboAccounts.setSelectedItem(item);
		sendOutput("accountSelectedForLocalComponents", item.getValue());
	}

	/**
	 * Send a message to other components that they must reset themselves.
	 */
	protected void sendReset() {
		sendOutput("reset", Collections.EMPTY_MAP);
	}

	@SocketEvent(socketId = "selectAccount")
	public void selectAccount(final TextMasterAccountModel account)
	{
		Optional<Comboitem> optionalAccount = comboAccounts.getItems()
				.stream()
				.filter(i -> ((TextMasterAccountModel) i.getValue()).getCode().equals(account.getCode()))
				.findFirst();

		if (!optionalAccount.isPresent())
		{
			getNotificationService().notifyUser(this.getWidgetInstanceManager(), "TextMasterGeneral", NotificationEvent.Level.FAILURE, getLabel("accountsnomatchfound", new Object[] { account.getCode() }));
			return;
		}

		selectAccountInternal(optionalAccount.get());
	}

	@ViewEvent(componentID = "btCreate", eventName = Events.ON_CLICK)
	public void create(final Event event)
	{
		super.sendOutput("createContext", new CreateContext(TextMasterAccountModel._TYPECODE));
	}

	@SocketEvent(socketId = "accounts")
	public void refreshList(final List<TextMasterAccountModel> accounts)
	{
		this.comboAccounts.getChildren().clear();
		accounts.forEach(c -> {
			final Comboitem item = new Comboitem(c.getName());
			item.setValue(c);
			this.comboAccounts.appendChild(item);
		});
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
