package org.cotrix.web.importwizard.client.step.mapping;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;
import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class MappingStepPresenterImpl implements MappingStepPresenter {

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final MappingStepFormView view;
	private  CotrixImportModelController model;
	
	@Inject
	public MappingStepPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, MappingStepFormView view,CotrixImportModelController model){
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
	
	public boolean isValid() {
		boolean validateResult = false;
		int counter = 0;
		ArrayList<HeaderType> types = view.getHeaderTypes();
		for (HeaderType type : types) {
			if(type.getValue()!= null && type.getValue().equals("Code")){
				counter++;
			}
		}
		if(counter == 1){
			validateResult = true;
			model.setType(types);
		}else{
			view.setStyleError();
			AlertDialog dialog = new AlertDialog();
			dialog.setMessage("You can assign only one code.");
			dialog.show();
		}
		return validateResult;
	}
	
	public void onFileChange(CSVFile csvFile) {
		view.setData(csvFile.getHeader());
	}
	

}
