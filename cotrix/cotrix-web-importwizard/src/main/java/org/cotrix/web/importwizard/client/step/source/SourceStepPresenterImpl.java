package org.cotrix.web.importwizard.client.step.source;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class SourceStepPresenterImpl implements SourceStepPresenter {
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final SourceStepView view;
	private final CotrixImportModelController model;

	@Inject
	public SourceStepPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, SourceStepView view,CotrixImportModelController model) {
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

	public boolean isValid() {
		if(view.isValidated()){
			model.setMetadata(view.getMetadata());
		}
		return view.isValidated();
	}

	public void alert(String message) {
		view.alert(message);
	}	
	
}
