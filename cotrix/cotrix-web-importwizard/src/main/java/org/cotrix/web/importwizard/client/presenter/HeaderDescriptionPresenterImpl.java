package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.FormWrapperView;
import org.cotrix.web.importwizard.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderDescriptionPresenterImpl implements HeaderDescriptionPresenter {
	
	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderDescriptionFormView view;
	private CotrixImportModel model;
	
	@Inject
	public HeaderDescriptionPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderDescriptionFormView view,CotrixImportModel model){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
		this.model.getCsvFile().addOnFilechangeHandler(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		view.initForm(model.getCsvFile().getHeaders());
	}

	public boolean isValidated() {
	    HashMap<String, String> descriptions = view.getHeaderDescription();
	    if(descriptions.size() == model.getCsvFile().getHeaders().length){
	    	model.setHeaderDescription(descriptions);
	    }else{
	    	view.alert("Please describe all headers");
	    }
		return (descriptions.size() == model.getCsvFile().getHeaders().length)?true:false;
	}

}
