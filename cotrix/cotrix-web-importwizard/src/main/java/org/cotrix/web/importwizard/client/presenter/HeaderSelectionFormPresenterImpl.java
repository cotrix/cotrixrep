package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderSelectionFormPresenterImpl implements HeaderSelectionFormPresenter {

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderSelectionFormView view;
	
	@Inject
	public HeaderSelectionFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderSelectionFormView view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
