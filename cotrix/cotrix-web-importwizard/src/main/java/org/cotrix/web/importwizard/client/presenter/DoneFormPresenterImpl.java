package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.DoneFormView;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class DoneFormPresenterImpl implements DoneFormPresenter {
	private ImportServiceAsync rpcService;
	private HandlerManager eventBus;
	private DoneFormView view;
	private CotrixImportModelController model;
	
	@Inject
	public DoneFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, DoneFormView view,CotrixImportModelController model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		this.model = model;
	}
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public boolean isValidated() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
