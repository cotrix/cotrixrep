package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.MetadataFormView;
import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.importwizard.client.view.form.SummaryFormViewImpl;
import org.cotrix.web.importwizard.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class SummaryFormPresenterImpl implements SummaryFormPresenter {

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final SummaryFormView view;
	private CotrixImportModel model;

	@Inject
	public SummaryFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, SummaryFormView view,CotrixImportModel model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		
		String[] headers = new String[]{"header1","header2"};
		view.initForm(headers);
	}

}