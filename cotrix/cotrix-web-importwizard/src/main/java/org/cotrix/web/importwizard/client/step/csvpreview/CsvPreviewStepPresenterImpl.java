package org.cotrix.web.importwizard.client.step.csvpreview;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationEditedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent.CsvParserConfigurationUpdatedHandler;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewStepPresenterImpl extends AbstractWizardStep implements CsvPreviewStepPresenter, CsvParserConfigurationUpdatedHandler {

	private final CsvPreviewStepView view;
	protected EventBus importEventBus;
	protected boolean headerRequired = false;
	
	@Inject
	public CsvPreviewStepPresenterImpl(CsvPreviewStepView view, @ImportBus EventBus importEventBus) {
		super("csv-preview", TrackerLabels.PREVIEW, "Does it look right?", "Adjust the parameters until it does.", BACKWARD, FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(CsvParserConfigurationUpdatedEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		if (headerRequired && !areHeadersValid()) {
			view.alert("All header fields should be filled");
			return false;
		}
		return true;
	}
	
	protected boolean areHeadersValid()
	{
		for (String header:view.getHeaders()) if (header == null || header.isEmpty()) return false;
		return true;
	}

	@Override
	public void onShowCsvConfigurationButtonClicked() {
		view.showCsvConfigurationDialog();
	}

	@Override
	public void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		Log.trace("csv parser configuration updated: "+event.getConfiguration());
		view.setCsvParserConfiguration(event.getConfiguration());
	}

	@Override
	public void onCsvConfigurationEdited(CsvParserConfiguration configuration) {
		importEventBus.fireEvent(new CsvParserConfigurationEditedEvent(configuration));		
	}
}
