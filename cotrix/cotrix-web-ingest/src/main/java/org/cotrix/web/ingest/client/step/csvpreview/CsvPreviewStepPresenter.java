package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.client.widgets.AlertDialog;
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
	
	@Inject
	private CsvPreviewStepView view;
	
	@Inject
	private AlertDialog alertDialog;
	
	@Inject @ImportBus 
	private EventBus importEventBus;
	private boolean headerRequired = false;

	@Inject
	public CsvPreviewStepPresenter() {
		super("csv-preview", TrackerLabels.PREVIEW, "Does it look right?", "Adjust the parameters until it does.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.FORWARD);
	}
	
	@Inject
	private void bind(CsvPreviewStepPresenterEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		CsvConfiguration configuration = view.getConfiguration();
		updateHeaderRequired(configuration);
		importEventBus.fireEventFromSource(new CsvParserConfigurationUpdatedEvent(configuration), this);
		
		List<String> headers = view.getHeaders();
		if (headerRequired && !areHeadersValid(headers)) {
			alert("All header fields should be filled");
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
	
	private void alert(String message) {
		alertDialog.center(message);
	}

	@EventHandler
	void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		if (event.getSource()!=this) {
			Log.trace("csv parser configuration updated: "+event.getConfiguration());
			view.setCsvParserConfiguration(event.getConfiguration());
			updateHeaderRequired(event.getConfiguration());
		}
	}
	
	private void updateHeaderRequired(CsvConfiguration configuration) {
		headerRequired = !configuration.isHasHeader();
	}
}
