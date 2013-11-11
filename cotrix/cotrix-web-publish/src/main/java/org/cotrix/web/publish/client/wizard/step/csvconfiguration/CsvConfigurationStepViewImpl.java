package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.publish.client.wizard.step.csvconfiguration.CsvWriterConfigurationPanel.DialogSaveHandler;
import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.shared.CsvParserConfiguration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvConfigurationStepViewImpl extends ResizeComposite implements CsvConfigurationStepView, DialogSaveHandler {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("CsvConfigurationStep.ui.xml")
	interface CsvConfigurationStepUiBinder extends UiBinder<Widget, CsvConfigurationStepViewImpl> {}
	private static CsvConfigurationStepUiBinder uiBinder = GWT.create(CsvConfigurationStepUiBinder.class);

	@UiField 
	CsvWriterConfigurationPanel configurationPanel;

	private AlertDialog alertDialog;
	

	private Presenter presenter;
	
	@Inject
	public CsvConfigurationStepViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
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
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog(false);
		}
		alertDialog.setMessage(message);
		alertDialog.center();
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
	}

	@Override
	public void onSave(CsvParserConfiguration configuration) {
		presenter.onCsvConfigurationEdited(configuration);
	}

}
