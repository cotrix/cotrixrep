package org.cotrix.web.importwizard.client.presenter;

import java.util.HashMap;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class SummaryFormPresenterImpl implements SummaryFormPresenter {

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final SummaryFormView view;
	private CotrixImportModelController model;

	@Inject
	public SummaryFormPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, SummaryFormView view,CotrixImportModelController model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		this.model.addOnFileChangeHandler(this);
		this.model.addOnDescriptionChangeHandler(this);
		this.model.addOnMetaDataChangeHandler(this);
		this.model.addOnTypeChangeHandler(this);
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void onMetadataChange(Metadata metadata) {
		view.setMetadata(metadata);
	}

	public void onTypeChange(HashMap<String, HeaderType> headerType) {
		view.setHeaderType(headerType);
	}

	public void onDescriptionChange(HashMap<String, String> headerDescription) {
		view.setDescription(headerDescription);
	}

	
	public void uploadCotrixModel(){
		view.alert("Your file have been Uploaded.");
	}

	public void onFileChange(CSVFile csvFile) {
		view.setHeader(csvFile.getHeader());
		
	}

}
