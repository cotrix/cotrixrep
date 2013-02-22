package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.MetadataFormView;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class MetadataFormPresenter implements Presenter<MetadataFormPresenter>, MetadataFormView.Presenter<MetadataFormPresenter> {
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final MetadataFormView view;

	public MetadataFormPresenter(ImportServiceAsync rpcService, HandlerManager eventBus, MetadataFormView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}	
	
}
