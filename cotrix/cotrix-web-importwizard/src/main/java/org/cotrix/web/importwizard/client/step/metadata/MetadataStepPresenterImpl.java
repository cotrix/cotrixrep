package org.cotrix.web.importwizard.client.step.metadata;

import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class MetadataStepPresenterImpl implements MetadataStepPresenter {

	private final MetadataStepView view;
	private final CotrixImportModelController model;

	@Inject
	public MetadataStepPresenterImpl(MetadataStepView view, CotrixImportModelController model) {
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WizardStepConfiguration getConfiguration() {
		return new WizardStepConfiguration("Add Metadata", "Add Metadata", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
	}

	public void go(HasWidgets container) {
		//container.clear();
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		if(view.isValidated()){
			model.setMetadata(view.getMetadata());
		}
		return view.isValidated();
	}

	public void alert(String message) {
		view.alert(message);
	}


	
}
