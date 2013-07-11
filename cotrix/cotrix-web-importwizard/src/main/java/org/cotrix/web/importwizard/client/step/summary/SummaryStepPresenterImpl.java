package org.cotrix.web.importwizard.client.step.summary;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.CotrixImportModelController;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class SummaryStepPresenterImpl implements SummaryStepPresenter {

	private final ImportServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final SummaryStepView view;
	private CotrixImportModelController model;

	@Inject
	public SummaryStepPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, SummaryStepView view,CotrixImportModelController model) {
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
		//container.clear();
		container.add(view.asWidget());
	}

	public void onMetadataChange(Metadata metadata) {
		view.setMetadata(metadata);
	}

	private HashMap<String, HeaderType> toHashMap(ArrayList<HeaderType> types){
		HashMap<String, HeaderType> headerType = new HashMap<String, HeaderType>();
		for (HeaderType type : types) {
			headerType.put(type.getName(),type);
		}
		return headerType;
	}
	public void onTypeChange(ArrayList<HeaderType> headerType) {
		view.setHeaderType(toHashMap(headerType));
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isComplete() {
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WizardStepConfiguration getConfiguration() {
		NavigationButtonConfiguration saveButton = new NavigationButtonConfiguration("Save");
		return new WizardStepConfiguration("Summary", "Summary", NavigationButtonConfiguration.DEFAULT_BACKWARD, saveButton);
	}

}
