package org.cotrix.web.importwizard.client.step.summary;

import java.util.ArrayList;
import java.util.HashMap;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.share.shared.CSVFile;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SummaryStepPresenterImpl extends AbstractWizardStep implements SummaryStepPresenter {

	protected SummaryStepView view;
	protected EventBus importEventBus;

	@Inject
	public SummaryStepPresenterImpl(SummaryStepView view, @ImportBus EventBus importEventBus) {
		super("summary", "Summary", "Summary", NavigationButtonConfiguration.DEFAULT_BACKWARD, new NavigationButtonConfiguration("Save"));
		this.view = view;
		
		this.importEventBus = importEventBus;
		//importEventBus.addHandler(MappingUpdatedEvent.TYPE, this);
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
}
