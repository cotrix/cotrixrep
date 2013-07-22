package org.cotrix.web.importwizard.client.step.metadata;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataStepPresenterImpl extends AbstractWizardStep implements MetadataUpdatedHandler, MetadataStepPresenter {

	protected MetadataStepView view;
	protected EventBus importEventBus;

	@Inject
	public MetadataStepPresenterImpl(MetadataStepView view, @ImportBus EventBus importEventBus) {
		super("metadata", "Add Metadata", "Add Metadata", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return validate();
	}

	public void alert(String message) {
		view.alert(message);
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) view.setMetadata(event.getMetadata());
	}
	
	public boolean validate() {
		ImportMetadata metadata = view.getMetadata();
		if (metadata.getName().isEmpty()) {
			view.alert("Name is required");
			return false;
		}
		
		if (metadata.getOwner().isEmpty()) {
			view.alert("Ower is required");
			return false;
		}
		
		importEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));
		
		return true;
	}
}
