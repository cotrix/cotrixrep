package org.cotrix.web.importwizard.client.step.metadata;

import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataStepPresenterImpl extends AbstractWizardStep implements MetadataStepPresenter {

	private final MetadataStepView view;

	@Inject
	public MetadataStepPresenterImpl(MetadataStepView view) {
		super("metadata", "Add Metadata", "Add Metadata", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		//container.clear();
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		/*if(view.isValidated()){
			model.setMetadata(view.getMetadata());
		}*/
		return view.isValidated();
	}

	public void alert(String message) {
		view.alert(message);
	}


	
}
