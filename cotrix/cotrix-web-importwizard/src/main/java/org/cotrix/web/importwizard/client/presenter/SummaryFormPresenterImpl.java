package org.cotrix.web.importwizard.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.form.MetadataFormView;
import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.importwizard.client.view.form.SummaryFormViewImpl;
import org.cotrix.web.importwizard.shared.CotrixImportModel;
import org.cotrix.web.importwizard.shared.HeaderType;
import org.cotrix.web.importwizard.shared.Metadata;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
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
		this.model.getCsvFile().addOnFilechangeHandler(this);
		this.model.setOnDescriptionChangeHandler(this);
		this.model.setOnMetadataChangeHandler(this);
		this.model.setOnTypeChangeHandler(this);
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

	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		view.setHeader(headers);
	}
	
	public void uploadCotrixModel(){
		view.alert("Your file have been Uploaded.");
	}

}
