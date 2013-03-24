package org.cotrix.web.importwizard.client.presenter;

import java.util.HashMap;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderDescriptionPresenterImpl implements HeaderDescriptionPresenter {
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderDescriptionFormView view;
	private CotrixImportModelController model;
	
	@Inject
	public HeaderDescriptionPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderDescriptionFormView view,CotrixImportModelController model){
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
	    HashMap<String, String> descriptions = view.getHeaderDescription();
	    int columnCount  = model.getCsvFile().getHeader().length;
	    
	    if(descriptions.size() == columnCount){
	    	model.setDescription(descriptions);
	    }else{
	    	view.alert("Please describe all headers");
	    }
		return (descriptions.size() == columnCount)?true:false;
	}

	public void onFileChange(CSVFile csvFile) {
		view.initForm(model.getCsvFile().getHeader());
	}

}
