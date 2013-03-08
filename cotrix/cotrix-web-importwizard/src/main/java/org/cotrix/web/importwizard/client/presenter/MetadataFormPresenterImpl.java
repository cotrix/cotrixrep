package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.MetadataFormView;
import org.cotrix.web.importwizard.shared.CotrixImportModel;
import org.cotrix.web.importwizard.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class MetadataFormPresenterImpl implements MetadataFormPresenter {
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final MetadataFormView view;
	private final CotrixImportModelController model;

	@Inject
	public MetadataFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, MetadataFormView view,CotrixImportModelController model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public boolean isValidated() {
		if(view.isValidated()){
			model.setMetadata(view.getMetadata());
		}
		return view.isValidated();
	}

	public void alert(String message) {
		view.alert(message);
	}	
	
}
