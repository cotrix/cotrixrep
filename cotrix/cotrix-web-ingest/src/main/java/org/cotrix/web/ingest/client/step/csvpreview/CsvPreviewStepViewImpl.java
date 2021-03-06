package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.common.client.widgets.CsvConfigurationPanel;
import org.cotrix.web.common.client.widgets.CsvConfigurationPanel.RefreshHandler;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.client.util.PreviewDataGrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CsvPreviewStepViewImpl extends ResizeComposite implements CsvPreviewStepView, RefreshHandler {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("CsvPreviewStep.ui.xml")
	interface PreviewStepUiBinder extends UiBinder<Widget, CsvPreviewStepViewImpl> {}
	
	@Inject
	private PreviewStepUiBinder uiBinder;	

	@UiTemplate("CsvParserConfigurationPanel.ui.xml")
	interface CsvParserConfigurationPanelUiBinder extends UiBinder<Widget, CsvConfigurationPanel> {
	}
	
	private static CsvParserConfigurationPanelUiBinder configurationPanelBinder = GWT.create(CsvParserConfigurationPanelUiBinder.class);

	@UiField
	CsvConfigurationPanel configurationPanel;
	
	@UiField (provided=true) 
	PreviewDataGrid preview;
	
	@Inject
	private CsvPreviewDataProvider dataProvider;

	@Inject
	private void init() {
		
		this.preview = new PreviewDataGrid(dataProvider, 25);
		
		initWidget(uiBinder.createAndBindUi(this));
		configurationPanel.setRefreshHandler(this);
		configurationPanel.addValueChangeHandler(new ValueChangeHandler<CsvConfiguration>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<CsvConfiguration> event) {
				updatePreview(event.getValue());
			}
		});
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			preview.onResize();
			preview.resetScroll();
		}
	}
	
	@UiFactory
	public CsvConfigurationPanel createCsvParserConfigurationPanel()
	{
		return new CsvConfigurationPanel(configurationPanelBinder);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public List<String> getHeaders() {
		return preview.getHeaders();
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
	}
	
	private void updatePreview(CsvConfiguration configuration)
	{
		dataProvider.setConfiguration(configuration);
		preview.loadData();
	}

	@Override
	public CsvConfiguration getConfiguration() {
		return configurationPanel.getConfiguration();
	}
}
