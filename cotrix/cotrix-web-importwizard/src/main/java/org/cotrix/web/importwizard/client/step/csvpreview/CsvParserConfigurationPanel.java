/**
 * 
 */
package org.cotrix.web.importwizard.client.step.csvpreview;


import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvParserConfigurationPanel extends Composite {
	
	protected static final String CUSTOM = "custom";
	protected static final String TAB = "tab";
	
	protected interface Value {
		public String getLabel();
		public char getValue();
	}
	
	protected enum Separator implements Value {
		COMMA("comma",','),
		SEMICOLON("semicolon",';'),
		SPACE("space",' '),
		TAB("tab",'\t')
		;
		protected String label;
		protected char value;
		/**
		 * @param label
		 * @param value
		 */
		private Separator(String label, char value) {
			this.label = label;
			this.value = value;
		}
		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		/**
		 * @return the value
		 */
		public char getValue() {
			return value;
		}
	}
	
	protected enum Quote implements Value {
		DOUBLE("double quote",'"'),
		SINGLE("single quote",'\'')
		;
		protected String label;
		protected char value;
		/**
		 * @param label
		 * @param value
		 */
		private Quote(String label, char value) {
			this.label = label;
			this.value = value;
		}
		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		/**
		 * @return the value
		 */
		public char getValue() {
			return value;
		}
	}
	
	private static CsvParserConfigurationDialogUiBinder uiBinder = GWT.create(CsvParserConfigurationDialogUiBinder.class);

	interface CsvParserConfigurationDialogUiBinder extends
	UiBinder<Widget, CsvParserConfigurationPanel> {
	}
	
	interface DialogSaveHandler extends EventHandler {
		public void onSave(CsvParserConfiguration configuration);
	}
	
	@UiField ListBox charsetField;
	@UiField SimpleCheckBox hasHeaderField;
	@UiField ListBox separatorField;
	@UiField TextBox customSeparatorField;
	@UiField TextBox commentField;
	@UiField ListBox quoteField;
	@UiField TextBox customQuoteField;
	
	protected DialogSaveHandler saveHandler;

	public CsvParserConfigurationPanel() {


		initWidget(uiBinder.createAndBindUi(this));
		setup(separatorField, Separator.values());
		bind(separatorField, customSeparatorField);
		setup(quoteField, Quote.values());
		bind(quoteField, customQuoteField);
	}
	
	@UiHandler("refreshButton")
	protected void refreshButtonClicked(ClickEvent clickEvent)
	{
		saveHandler.onSave(getConfiguration());
	}
	
	protected void setup(final ListBox listBox, Value[] values)
	{
		for (Value value:values) {
			listBox.addItem(value.getLabel(), String.valueOf(value.getValue()));
		}
		listBox.addItem("other", CUSTOM);
	}
	
	protected void bind(final ListBox listBox, final TextBox textBox)
	{
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				int index = listBox.getSelectedIndex();
				if (index>=0) {
					String value = listBox.getValue(index);
					textBox.setEnabled(CUSTOM.equals(value));
				}
			}
		});
	}

	/**
	 * @param saveHandler the saveHandler to set
	 */
	public void setSaveHandler(DialogSaveHandler saveHandler) {
		this.saveHandler = saveHandler;
	}

	public void setConfiguration(CsvParserConfiguration configuration)
	{
	
		hasHeaderField.setValue(configuration.isHasHeader());
		Log.trace("separator: "+configuration.getFieldSeparator());
		updateListBox(separatorField, customSeparatorField, configuration.getFieldSeparator(), Separator.values());
		updateListBox(quoteField, customQuoteField, configuration.getQuote(), Quote.values());
		commentField.setValue(String.valueOf(configuration.getComment()));

		charsetField.clear();
		for (String charset:configuration.getAvailablesCharset()) charsetField.addItem(charset);
		selectValue(charsetField, configuration.getCharset());
		
	}
	
	protected void updateListBox(ListBox listBox, TextBox textBox, char fieldValue, Value[] values)
	{
		String listValue = null;
		for (Value value:values) if (value.getValue() == fieldValue) listValue = String.valueOf(value.getValue());
		if (listValue!=null) {
			selectValue(listBox, listValue);
			textBox.setVisible(false);
		} else {
			textBox.setValue(String.valueOf(fieldValue));
			textBox.setVisible(true);
			selectValue(listBox, CUSTOM);
		}
	}
	
	protected boolean selectValue(ListBox listBox, String value)
	{
		for (int i = 0; i<listBox.getItemCount(); i++) {
			String candidate = listBox.getValue(i);
			if (value.equals(candidate)) {
				listBox.setSelectedIndex(i);
				return true;
			}
		}
		return false;
	}
	
	public CsvParserConfiguration getConfiguration()
	{
		CsvParserConfiguration configuration = new CsvParserConfiguration();
		
		//TODO validation
		configuration.setCharset(charsetField.getValue(charsetField.getSelectedIndex()));
		configuration.setComment(commentField.getValue().charAt(0));
		char separatorChar =  getValue(separatorField, customSeparatorField);
		configuration.setFieldSeparator(separatorChar);
		configuration.setHasHeader(hasHeaderField.getValue());
		configuration.setQuote(getValue(quoteField, customQuoteField));
		return configuration;
	}
	
	protected char getValue(ListBox listBox, TextBox textBox)
	{
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex<0) throw new IllegalStateException("Invalid selected index "+selectedIndex);
		String value = listBox.getValue(selectedIndex);
		if (CUSTOM.equals(value)) return textBox.getValue().charAt(0);
		else return value.charAt(0);
	}
}
