package org.cotrix.web.importwizard.client.step.csvpreview;

import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.step.csvpreview.CsvParserConfigurationPanel.DialogSaveHandler;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.PreviewStyle;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvPreviewStepViewImpl extends Composite implements CsvPreviewStepView, DialogSaveHandler {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("CsvPreviewStep.ui.xml")
	interface PreviewStepUiBinder extends UiBinder<Widget, CsvPreviewStepViewImpl> {}
	private static PreviewStepUiBinder uiBinder = GWT.create(PreviewStepUiBinder.class);
	
	interface Style extends CssResource, PreviewStyle {

		String preview();
		String textbox();

		String header(); 
	
	}

	@UiField 
	CsvParserConfigurationPanel configurationPanel;
	
	@UiField (provided=true) 
	PreviewGrid preview;
	

	@UiField Style style;

	private AlertDialog alertDialog;
	

	private Presenter presenter;
	
	protected PreviewDataProvider dataProvider;
	
	@Inject
	public CsvPreviewStepViewImpl(PreviewDataProvider dataProvider) {
		
		this.dataProvider = dataProvider;
		preview = new PreviewGrid(dataProvider);
		initWidget(uiBinder.createAndBindUi(this));
		preview.setStyle(style);
		configurationPanel.setSaveHandler(this);
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
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCsvConfigurationDialog() {
		//configurationDialog.center();
	}
	
	@Override
	public void setCsvParserConfiguration(CsvParserConfiguration configuration) {
		configurationPanel.setConfiguration(configuration);
		updatePreview(configuration);
	}

	@Override
	public void onSave(CsvParserConfiguration configuration) {
		updatePreview(configuration);
		presenter.onCsvConfigurationEdited(configuration);
	}
	
	
	protected void updatePreview(CsvParserConfiguration configuration)
	{
		if (dataProvider.getConfiguration()==null || !dataProvider.getConfiguration().equals(configuration)) {
			dataProvider.setConfiguration(configuration);
			preview.loadData();
		}
	}
}
