package org.cotrix.web.importwizard.client.step.preview;

import java.util.List;

import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent.CodeListTypeUpdatedHandler;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationEditedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent.CsvParserConfigurationUpdatedHandler;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.PreviewDataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.PreviewDataUpdatedEvent.PreviewDataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListPreviewData;
import org.cotrix.web.importwizard.shared.CodeListType;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewStepPresenterImpl extends AbstractWizardStep implements CsvPreviewStepPresenter, PreviewDataUpdatedHandler, CodeListTypeUpdatedHandler, CsvParserConfigurationUpdatedHandler {

	private final CsvPreviewStepView view;
	protected EventBus importEventBus;
	protected boolean headerRequired = false;
	
	@Inject
	public CsvPreviewStepPresenterImpl(CsvPreviewStepView view, @ImportBus EventBus importEventBus) {
		super("csv-preview", "Preview", "CodeList Preview", DEFAULT_BACKWARD, DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(PreviewDataUpdatedEvent.TYPE, this);
		importEventBus.addHandler(CodeListTypeUpdatedEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public void setPreviewData(List<String> header, int numColumns, List<List<String>> data)
	{
		view.cleanPreviewGrid();
		if (header!=null) {
			view.setupStaticHeader(header);
			headerRequired = false;
		} else {
			view.setupEditableHeader(numColumns);
			headerRequired = true;
		}
		view.setData(data);
	}

	public boolean isComplete() {
		if (headerRequired && !areHeadersValid()) {
			view.alert("All header fields should be filled");
			return false;
		}
		return true;
	}
	
	protected boolean areHeadersValid()
	{
		for (String header:view.getEditedHeaders()) if (header == null || header.isEmpty()) return false;
		return true;
	}

	@Override
	public void onShowCsvConfigurationButtonClicked() {
		view.showCsvConfigurationDialog();
	}

	@Override
	public void onPreviewDataUpdated(PreviewDataUpdatedEvent event) {
		CodeListPreviewData previewData = event.getPreviewData();
		setPreviewData(previewData.getHeader(), previewData.getColumnsCount(), previewData.getData());
	}

	@Override
	public void onCodeListTypeUpdated(CodeListTypeUpdatedEvent event) {
		if (event.getCodeListType() == CodeListType.CSV) view.showCsvConfigurationButton();
		else view.hideCsvConfigurationButton();
	}

	@Override
	public void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {		
		view.setCsvParserConfiguration(event.getConfiguration());		
	}

	@Override
	public void onCsvConfigurationEdited(CsvParserConfiguration configuration) {
		importEventBus.fireEvent(new CsvParserConfigurationEditedEvent(configuration));		
	}
}
