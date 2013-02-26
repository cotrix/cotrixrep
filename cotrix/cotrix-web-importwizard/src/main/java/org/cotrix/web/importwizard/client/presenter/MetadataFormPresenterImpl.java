package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.MetadataFormView;
import org.cotrix.web.importwizard.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class MetadataFormPresenterImpl implements MetadataFormPresenter {
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final MetadataFormView view;
	private final CotrixImportModel model;

	@Inject
	public MetadataFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, MetadataFormView view,CotrixImportModel model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}	
	
}
