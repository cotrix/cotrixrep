package org.cotrix.web.ingest.client.step.preview;

import org.cotrix.web.ingest.client.event.AssetRetrievedEvent;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.UIAssetType;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

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
public class PreviewStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, PreviewStepView.Presenter {

	protected static interface PreviewStepPresenterEventBinder extends EventBinder<PreviewStepPresenter> {}
	
	private final PreviewStepView view;
	protected EventBus importEventBus;
	protected boolean headerRequired = false;

	@Inject
	public PreviewStepPresenter(PreviewStepView view, @ImportBus EventBus importEventBus) {
		super("asset-preview", TrackerLabels.CUSTOMIZE, "Asset sample", "", ImportWizardStepButtons.BACKWARD);
		this.view = view;
		this.view.setPresenter(this);

		this.importEventBus = importEventBus;
	}
	
	@Inject
	private void bind(PreviewStepPresenterEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}
	
	@EventHandler
	void onAssetRetrieved(AssetRetrievedEvent event) {
		if (event.getAsset().getAssetType()==UIAssetType.CSV) view.updatePreview();
	}
	
	@EventHandler
	void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		view.updatePreview();
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}
}
