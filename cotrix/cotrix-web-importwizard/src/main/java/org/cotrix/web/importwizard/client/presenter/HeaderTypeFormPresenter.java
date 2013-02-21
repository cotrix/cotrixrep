package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class HeaderTypeFormPresenter implements Presenter<HeaderTypeFormPresenter>,HeaderTypeFormView.Presenter<HeaderTypeFormPresenter>{

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderTypeFormView view;
	
	public HeaderTypeFormPresenter(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderTypeFormView view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}
	
	public void go(HasWidgets container) {

	}
	

}
