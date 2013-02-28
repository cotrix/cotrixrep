package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormViewImpl;
import org.cotrix.web.importwizard.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class HeaderSelectionFormPresenterImpl implements HeaderSelectionFormPresenter {

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final HeaderSelectionFormView view;
	private  CotrixImportModel model;
	
	@Inject
	public HeaderSelectionFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus,HeaderSelectionFormView view,CotrixImportModel model){
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

	public boolean isValidated() {
		ArrayList<String> headers = view.getHeaders();
		
		if(headers.size() != model.getCsvFile().getHeaders().length){
			view.alert("Please define all header");
		}else{
			model.getCsvFile().setHeaders(headers.toArray(new String[0]));
		}
		return (headers.size() == model.getCsvFile().getHeaders().length)?true:false;
	}

	public void onCheckBoxChecked(boolean isChecked) {
		view.showHeaderForm(!isChecked);
	}

	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		view.setData(headers, data);
	}
}
