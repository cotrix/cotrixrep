package org.cotrix.web.publish.client.wizard.step.csvconfiguration;

import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.client.widgets.CsvConfigurationPanel;
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
public class CsvConfigurationStepViewImpl extends ResizeComposite implements CsvConfigurationStepView {
	
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
	
	@Inject
	AlertDialog alertDialog;
	
	@Inject
	public CsvConfigurationStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	public CsvConfigurationPanel createCsvParserConfigurationPanel()
	{
		return new CsvConfigurationPanel(configurationPanelBinder);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void alert(String message) {
		alertDialog.center(message);
	}
	
	@Override
	public void setCsvWriterConfiguration(CsvConfiguration configuration) {
		configurationPanel.setConfiguration(configuration);
	}

	@Override
	public CsvConfiguration getCsvWriterConfiguration() {
		return configurationPanel.getConfiguration();
	}
}
