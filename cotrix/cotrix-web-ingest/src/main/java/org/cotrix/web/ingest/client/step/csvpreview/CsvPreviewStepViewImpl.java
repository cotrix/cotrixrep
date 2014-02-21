package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.client.widgets.CsvConfigurationPanel;
import org.cotrix.web.common.client.widgets.CsvConfigurationPanel.RefreshHandler;
import org.cotrix.web.common.shared.CsvConfiguration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewStepViewImpl extends ResizeComposite implements CsvPreviewStepView, RefreshHandler {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("CsvPreviewStep.ui.xml")
	interface PreviewStepUiBinder extends UiBinder<Widget, CsvPreviewStepViewImpl> {}
	private static PreviewStepUiBinder uiBinder = GWT.create(PreviewStepUiBinder.class);	

	@UiTemplate("CsvParserConfigurationPanel.ui.xml")
	interface CsvParserConfigurationPanelUiBinder extends UiBinder<Widget, CsvConfigurationPanel> {
	}
	
	private static CsvParserConfigurationPanelUiBinder configurationPanelBinder = GWT.create(CsvParserConfigurationPanelUiBinder.class);

	@UiField
	CsvConfigurationPanel configurationPanel;
	
	@UiField (provided=true) 
	PreviewGrid preview;

	private Presenter presenter;
	
	protected PreviewDataProvider dataProvider;
	
	@Inject
	public CsvPreviewStepViewImpl(PreviewDataProvider dataProvider) {
		
		this.dataProvider = dataProvider;
		preview = new PreviewGrid(dataProvider);
		initWidget(uiBinder.createAndBindUi(this));
		configurationPanel.setRefreshHandler(this);
	}
	
	@UiFactory
	public CsvConfigurationPanel createCsvParserConfigurationPanel()
	{
		return new CsvConfigurationPanel(configurationPanelBinder);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public List<String> getHeaders() {
		return preview.getHeaders();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void alert(String message) {
		AlertDialog.INSTANCE.center(message);
	}
	
	@Override
	public void setCsvParserConfiguration(CsvConfiguration configuration) {
		configurationPanel.setConfiguration(configuration);
		updatePreview(configuration);
	}

	@Override
	public void onRefresh(CsvConfiguration configuration) {
		if (dataProvider.getConfiguration().equals(configuration)) return;
		updatePreview(configuration);
		presenter.onCsvConfigurationEdited(configuration);
	}
	
	
	protected void updatePreview(CsvConfiguration configuration)
	{
		dataProvider.setConfiguration(configuration);
		preview.loadData();
	}
}
