package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.client.event.CsvHeadersEvent;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
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
public class CsvPreviewStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, CsvPreviewStepView.Presenter {

	protected static interface CsvPreviewStepPresenterEventBinder extends EventBinder<CsvPreviewStepPresenter> {}
	
	private final CsvPreviewStepView view;
	private EventBus importEventBus;
	private boolean headerRequired = false;

	@Inject
	public CsvPreviewStepPresenter(CsvPreviewStepView view, @ImportBus EventBus importEventBus) {
		super("csv-preview", TrackerLabels.PREVIEW, "Does it look right?", "Adjust the parameters until it does.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);

		this.importEventBus = importEventBus;
	}
	
	@Inject
	private void bind(CsvPreviewStepPresenterEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		List<String> headers = view.getHeaders();
		if (headerRequired && !areHeadersValid(headers)) {
			view.alert("All header fields should be filled");
			return false;
		}
		
		importEventBus.fireEvent(new CsvHeadersEvent(headers));
		return true;
	}

	private boolean areHeadersValid(List<String> headers)
	{
		for (String header:headers) if (header == null || header.isEmpty()) return false;
		return true;
	}

	@EventHandler
	void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		if (event.getSource()!=this) {
			Log.trace("csv parser configuration updated: "+event.getConfiguration());
			view.setCsvParserConfiguration(event.getConfiguration());
			updateHeaderRequired(event.getConfiguration());
		}
	}

	@Override
	public void onCsvConfigurationEdited(CsvConfiguration configuration) {
		updateHeaderRequired(configuration);
		importEventBus.fireEventFromSource(new CsvParserConfigurationUpdatedEvent(configuration), this);
	}
	
	private void updateHeaderRequired(CsvConfiguration configuration) {
		headerRequired = !configuration.isHasHeader();
	}
}
