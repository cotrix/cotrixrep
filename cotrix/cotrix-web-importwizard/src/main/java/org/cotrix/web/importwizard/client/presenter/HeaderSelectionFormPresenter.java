package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class HeaderSelectionFormPresenter implements Presenter<HeaderSelectionFormPresenter>, HeaderSelectionFormView.Presenter<HeaderSelectionFormPresenter>{

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderSelectionFormView<HeaderSelectionFormViewImpl> view;
	
	public HeaderSelectionFormPresenter(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderSelectionFormView<HeaderSelectionFormViewImpl> view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
