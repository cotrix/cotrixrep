package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.client.widgets.CsvConfigurationPanel;
import org.cotrix.web.share.client.widgets.CsvConfigurationPanel.DialogSaveHandler;
import org.cotrix.web.share.shared.CsvConfiguration;

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
public class CsvConfigurationStepViewImpl extends ResizeComposite implements CsvConfigurationStepView, DialogSaveHandler {
	
	protected static final int HEADER_ROW = 0;

	@UiTemplate("CsvConfigurationStep.ui.xml")
	interface CsvConfigurationStepUiBinder extends UiBinder<Widget, CsvConfigurationStepViewImpl> {}
	private static CsvConfigurationStepUiBinder uiBinder = GWT.create(CsvConfigurationStepUiBinder.class);
	
	@UiTemplate("CsvWriterConfigurationPanel.ui.xml")
	interface CsvParserConfigurationPanelUiBinder extends UiBinder<Widget, CsvConfigurationPanel> {
	}
	
	private static CsvParserConfigurationPanelUiBinder configurationPanelBinder = GWT.create(CsvParserConfigurationPanelUiBinder.class);

	@UiField
	CsvConfigurationPanel configurationPanel;

	private AlertDialog alertDialog;
	

	private Presenter presenter;
	
	@Inject
	public CsvConfigurationStepViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
		configurationPanel.setSaveHandler(this);
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
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog(false);
		}
		alertDialog.setMessage(message);
		alertDialog.center();
	}
	
	@Override
	public void setCsvParserConfiguration(CsvConfiguration configuration) {
		configurationPanel.setConfiguration(configuration);
	}

	@Override
	public void onSave(CsvConfiguration configuration) {
		presenter.onCsvConfigurationEdited(configuration);
	}

}
