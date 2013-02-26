package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderTypeFormPresenterImpl implements HeaderTypeFormPresenter{

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderTypeFormView view;
	
	@Inject
	public HeaderTypeFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderTypeFormView view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}
	
	public void go(HasWidgets container) {

	}
	

}
