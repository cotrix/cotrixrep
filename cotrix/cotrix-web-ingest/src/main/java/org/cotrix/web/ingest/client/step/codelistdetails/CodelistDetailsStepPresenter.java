package org.cotrix.web.ingest.client.step.codelistdetails;

import org.cotrix.web.ingest.client.event.AssetDetailsEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.RepositoryDetailsRequestEvent;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AssetDetails;
import org.cotrix.web.wizard.client.event.NavigationEvent;
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
public class CodelistDetailsStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, CodelistDetailsStepView.Presenter {
	
	protected static interface CodelistDetailsStepPresenterEventBinder extends EventBinder<CodelistDetailsStepPresenter> {}

	protected final CodelistDetailsStepView view;
	
	protected EventBus importBus;
	
	protected AssetDetails visualizedAsset;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetails;
	
	@Inject
	public CodelistDetailsStepPresenter(CodelistDetailsStepView view) {
		super("codelistDetails", TrackerLabels.ACQUIRE, "Codelist Details", "", ImportWizardStepButtons.BACKWARD);
		this.view = view;
		view.setPresenter(this);
	}
	
	
	@Inject
	protected void bind(CodelistDetailsStepPresenterEventBinder binder, @ImportBus EventBus importBus)
	{
		binder.bindEventHandlers(this, importBus);
		
		this.importBus = importBus;
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}
	
	@EventHandler
	public void onAssetDetails(AssetDetailsEvent event) {
		Log.trace("onAssetDetails event: "+event);
		view.setAssetDetails(event.getAssetDetails());
		visualizedAsset = event.getAssetDetails();
	}

	@Override
	public void repositoryDetails() {
		Log.trace("repositoryDetails");
		importBus.fireEvent(new RepositoryDetailsRequestEvent(visualizedAsset.getRepositoryId()));
		importBus.fireEvent(NavigationEvent.FORWARD);
	}
}
