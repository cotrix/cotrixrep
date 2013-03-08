package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormViewImpl;
import org.cotrix.web.importwizard.shared.CSVFile;
import org.cotrix.web.importwizard.shared.CotrixImportModel;
import org.cotrix.web.importwizard.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderTypeFormPresenterImpl implements HeaderTypeFormPresenter{

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderTypeFormView view;
	private  CotrixImportModelController model;
	
	@Inject
	public HeaderTypeFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderTypeFormView view,CotrixImportModelController model){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
		this.model.addOnFileChangeHandler(this);
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
	
	public boolean isValidated() {	
		model.setType(view.getHeaderTypes());
		return true;
	}
	
	public void onFileChange(CSVFile csvFile) {
		view.setData(csvFile.getHeader());
	}
	

}
