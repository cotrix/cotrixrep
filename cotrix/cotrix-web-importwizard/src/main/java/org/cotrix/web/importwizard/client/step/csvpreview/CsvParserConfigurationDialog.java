/**
 * 
 */
package org.cotrix.web.importwizard.client.step.csvpreview;


import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration.NewLine;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationDialog extends DialogBox {

	private static CsvParserConfigurationDialogUiBinder uiBinder = GWT.create(CsvParserConfigurationDialogUiBinder.class);

	interface CsvParserConfigurationDialogUiBinder extends
	UiBinder<Widget, CsvParserConfigurationDialog> {
	}

	interface Style extends CssResource {
		String headerlabel();
		String valuelabel();
	}
	
	interface DialogSaveHandler extends EventHandler {
		public void onSave(CsvParserConfiguration configuration);
	}

	@UiField Button saveButton;
	@UiField Button cancelButton;
	@UiField Style style;
	
	@UiField ListBox charsetField;
	@UiField CheckBox hasHeaderField;
	@UiField RadioButton commaSeparator;
	@UiField RadioButton tabSeparator;
	@UiField RadioButton customSeparator;
	@UiField TextBox customSeparatorField;
	@UiField TextBox commentField;
	@UiField ListBox newLineField;
	@UiField TextBox quoteField;
	
	protected DialogSaveHandler saveHandler;

	public CsvParserConfigurationDialog(final DialogSaveHandler saveHandler) {
		this.saveHandler = saveHandler;
		
		setModal(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setWidth("500px");

		setWidget(uiBinder.createAndBindUi(this));
				
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveHandler.onSave(getConfiguration());
				hide();
			}
		});
		
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		//NewLine
		for (NewLine newLine:NewLine.values()) newLineField.addItem(newLine.toString());

	}
	
	public void setConfiguration(CsvParserConfiguration configuration)
	{
		//charset
		charsetField.clear();
		for (String charset:configuration.getAvailablesCharset()) charsetField.addItem(charset);
		for (int i = 0; i < charsetField.getItemCount(); i++) if (charsetField.getItemText(i).equals(configuration.getCharset())) charsetField.setItemSelected(i, true);
		
		commentField.setValue(String.valueOf(configuration.getComment()));
		setSeparator(configuration.getFieldSeparator());
		hasHeaderField.setValue(configuration.isHasHeader());
		
		for (int i = 0; i < newLineField.getItemCount(); i++) if (newLineField.getItemText(i).equals(configuration.getLineSeparator())) newLineField.setItemSelected(i, true);
		
		quoteField.setValue(String.valueOf(configuration.getQuote()));
		
	}
	
	public CsvParserConfiguration getConfiguration()
	{
		CsvParserConfiguration configuration = new CsvParserConfiguration();
		
		//TODO validation
		configuration.setCharset(charsetField.getValue(charsetField.getSelectedIndex()));
		configuration.setComment(commentField.getValue().charAt(0));
		configuration.setFieldSeparator(getSeparator());
		configuration.setHasHeader(hasHeaderField.getValue());
		configuration.setLineSeparator(NewLine.valueOf(newLineField.getValue(newLineField.getSelectedIndex())));
		configuration.setQuote(quoteField.getValue().charAt(0));
		return configuration;
	}
	
	protected void setSeparator(char separator)
	{
		switch (separator) {
			case ',': commaSeparator.setValue(true); break;
			case '\t': tabSeparator.setValue(true); break;
			default: {
				customSeparator.setValue(true);
				customSeparatorField.setValue(String.valueOf(separator));
			} break;
		}
	}
	
	protected char getSeparator()
	{
		if (commaSeparator.getValue()) return ',';
		if (tabSeparator.getValue()) return '\t';
		//TODO validation, index check
		if (customSeparator.getValue()) return customSeparatorField.getValue().charAt(0);
		
		Log.error("No separator set");
		return ' ';
	}
}
