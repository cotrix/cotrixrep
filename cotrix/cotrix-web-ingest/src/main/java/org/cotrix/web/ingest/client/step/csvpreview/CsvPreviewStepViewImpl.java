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
	PreviewGridDefaultImpl preview;

	private Presenter presenter;
	
	@Inject
	AlertDialog alertDialog;
	
	@Inject
	private void init(PreviewGridDefaultImpl preview) {
		
		this.preview = preview;
		initWidget(uiBinder.createAndBindUi(this));
		configurationPanel.setRefreshHandler(this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) preview.resetScroll();
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
		alertDialog.center(message);
	}
	
	@Override
	public void setCsvParserConfiguration(CsvConfiguration configuration) {
		configurationPanel.setConfiguration(configuration);
		updatePreview(configuration);
	}

	@Override
	public void onRefresh(CsvConfiguration configuration) {
		if (preview.getConfiguration().equals(configuration)) return;
		updatePreview(configuration);
		presenter.onCsvConfigurationEdited(configuration);
	}
	
	
	protected void updatePreview(CsvConfiguration configuration)
	{
		preview.setConfiguration(configuration);
		preview.loadData();
	}
}
