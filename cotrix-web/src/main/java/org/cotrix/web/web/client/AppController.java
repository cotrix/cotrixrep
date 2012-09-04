package org.cotrix.web.web.client;

import org.cotrix.web.shared.ContactDetails;
import org.cotrix.web.web.client.common.CotrixColumnDefinitionsFactory;
import org.cotrix.web.web.client.event.AddContactEvent;
import org.cotrix.web.web.client.event.AddContactEventHandler;
import org.cotrix.web.web.client.event.ContactUpdatedEvent;
import org.cotrix.web.web.client.event.ContactUpdatedEventHandler;
import org.cotrix.web.web.client.event.EditContactCancelledEvent;
import org.cotrix.web.web.client.event.EditContactCancelledEventHandler;
import org.cotrix.web.web.client.event.EditContactEvent;
import org.cotrix.web.web.client.event.EditContactEventHandler;
import org.cotrix.web.web.client.presenter.CotrixPresenter;
import org.cotrix.web.web.client.presenter.EditContactPresenter;
import org.cotrix.web.web.client.presenter.Presenter;
import org.cotrix.web.web.client.view.CotrixViewImpl;
import org.cotrix.web.web.client.view.EditContactView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final CotrixServiceAsync rpcService;
	private HasWidgets container;
	private CotrixViewImpl<ContactDetails> contactsView = null;
	private EditContactView editContactView = null;

	public AppController(CotrixServiceAsync rpcService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(AddContactEvent.TYPE, new AddContactEventHandler() {
			public void onAddContact(AddContactEvent event) {
				doAddNewContact();
			}
		});

		eventBus.addHandler(EditContactEvent.TYPE, new EditContactEventHandler() {
			public void onEditContact(EditContactEvent event) {
				doEditContact(event.getId());
			}
		});

		eventBus.addHandler(EditContactCancelledEvent.TYPE, new EditContactCancelledEventHandler() {
			public void onEditContactCancelled(EditContactCancelledEvent event) {
				doEditContactCancelled();
			}
		});

		eventBus.addHandler(ContactUpdatedEvent.TYPE, new ContactUpdatedEventHandler() {
			public void onContactUpdated(ContactUpdatedEvent event) {
				doContactUpdated();
			}
		});
	}

	private void doAddNewContact() {
		History.newItem("add");
	}

	private void doEditContact(String id) {
		History.newItem("edit", false);
		Presenter presenter = new EditContactPresenter(rpcService, eventBus, new EditContactView(), id);
		presenter.go(container);
	}

	private void doEditContactCancelled() {
		History.newItem("list");
	}

	private void doContactUpdated() {
		History.newItem("list");
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("list");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();

		if (token != null) {
			if (token.equals("list")) {
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess() {
						// lazily initialize our views, and keep them around to
						// be reused
						//
						if (contactsView == null) {
							contactsView = new CotrixViewImpl<ContactDetails>();

						}
						new CotrixPresenter(rpcService, eventBus, contactsView, CotrixColumnDefinitionsFactory
								.getCotrixColumnDefinitions()).go(container);
					}
				});
			} else if (token.equals("add") || token.equals("edit")) {
				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess() {
						// lazily initialize our views, and keep them around to
						// be reused
						//
						if (editContactView == null) {
							editContactView = new EditContactView();

						}
						new EditContactPresenter(rpcService, eventBus, editContactView).go(container);
					}
				});
			}
		}
	}
}
