package org.cotrix.web.importwizard.client.step.preview;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.step.preview.CsvParserConfigurationDialog.DialogSaveHandler;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewStepViewImpl extends Composite implements PreviewStepView, DialogSaveHandler {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("PreviewStep.ui.xml")
	interface PreviewStepUiBinder extends UiBinder<Widget, PreviewStepViewImpl> {}
	private static PreviewStepUiBinder uiBinder = GWT.create(PreviewStepUiBinder.class);
	
	interface Style extends CssResource {

		String textbox();

		String flextTableHeader(); 
	
	}

	@UiField FlexTable previewGrid;
	@UiField Button showCsvConfigurationButton;
	@UiField Style style;
	
	protected List<TextBox> headerFields = new ArrayList<TextBox>();

	private AlertDialog alertDialog;
	protected CsvParserConfigurationDialog configurationDialog;

	private Presenter presenter;
	
	public PreviewStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showCsvConfigurationButton")
	public void onClick(ClickEvent event) {
		presenter.onShowCsvConfigurationButtonClicked();
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
	@Override
	public void cleanPreviewGrid() {
		previewGrid.removeAllRows();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setupEditableHeader(int numColumns)
	{
		headerFields.clear();
		for (int i = 0; i < numColumns; i++) {
			TextBox headerField = new TextBox();
			headerField.setStyleName(style.textbox());
			headerFields.add(headerField);
			
			previewGrid.setWidget(HEADER_ROW, i, headerField);
			previewGrid.getCellFormatter().setStyleName(HEADER_ROW, i, style.flextTableHeader());
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void setupStaticHeader(List<String> headers)
	{
		headerFields.clear();
		for (int i = 0; i < headers.size(); i++) {
			String header = headers.get(i);
			
			HTML staticHeader = new HTML(header);
			previewGrid.setWidget(HEADER_ROW, i, staticHeader);
			previewGrid.getCellFormatter().setStyleName(HEADER_ROW, i, style.flextTableHeader());
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void setData(List<List<String>> rows) {
		for (List<String> row:rows) addDataRow(row);
	}
	
	protected void addDataRow(List<String> row)
	{
		int rowIndex = previewGrid.getRowCount();
		for (int i = 0; i < row.size(); i++) {
			String cell = row.get(i);
			previewGrid.setWidget(rowIndex, i, new HTML(cell));
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public List<String> getEditedHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		for (TextBox headerField:headerFields) headers.add(headerField.getText());
		return headers;
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
	public void showCsvConfigurationButton() {
		showCsvConfigurationButton.setVisible(true);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void hideCsvConfigurationButton() {
		showCsvConfigurationButton.setVisible(false);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCsvConfigurationDialog() {
		ensureInitializedConfigurationDialog();
		configurationDialog.center();
		
	}
	
	@Override
	public void setCsvParserConfiguration(CsvParserConfiguration configuration) {
		ensureInitializedConfigurationDialog();
		configurationDialog.setConfiguration(configuration);
	}

	@Override
	public void onSave(CsvParserConfiguration configuration) {
		presenter.onCsvConfigurationEdited(configuration);	
	}
	
	protected void ensureInitializedConfigurationDialog()
	{
		if (configurationDialog == null) configurationDialog = new CsvParserConfigurationDialog(this);
	}
}
