package org.cotrix.web.ingest.client.step.repositorydetails;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.RepositoryDetailsEvent;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RepositoryDetailsStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, RepositoryDetailsStepView.Presenter {
	
	protected static interface RepositoryDetailsStepPresenterEventBinder extends EventBinder<RepositoryDetailsStepPresenter> {}

	protected final RepositoryDetailsStepView view;
	
	protected EventBus importBus;
	
	protected AssetInfo selectedAsset;
	
	@Inject
	public RepositoryDetailsStepPresenter(RepositoryDetailsStepView view) {
		super("repositoryDetails", TrackerLabels.ACQUIRE, "Repository Details", "", ImportWizardStepButtons.BACKWARD);
		this.view = view;
	}
	
	@Inject
	protected void bind(RepositoryDetailsStepPresenterEventBinder binder, @ImportBus EventBus importBus)
	{
		binder.bindEventHandlers(this, importBus);
		
		this.importBus = importBus;
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return false;
	}

	@EventHandler
	public void onRepositoryDetails(RepositoryDetailsEvent event) {
		Log.trace("onRepositoryDetails event: "+event);
		view.setRepository(event.getRepositoryDetails());
	}

}
