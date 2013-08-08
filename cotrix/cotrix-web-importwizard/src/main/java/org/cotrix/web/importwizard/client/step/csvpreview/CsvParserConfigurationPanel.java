/**
 * 
 */
package org.cotrix.web.importwizard.client.step.csvpreview;


import org.cotrix.web.importwizard.shared.CsvParserConfiguration;

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
		bind(separatorField, customSeparatorField);
		bind(quoteField, customQuoteField);

	}
	
	@UiHandler("refreshButton")
	protected void refreshButtonClicked(ClickEvent clickEvent)
	{
		saveHandler.onSave(getConfiguration());
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
		System.out.println("separator: "+configuration.getFieldSeparator());
		String separator = configuration.getFieldSeparator()=='\t'?TAB:String.valueOf(configuration.getFieldSeparator());
		updateListBox(separatorField, separator, customSeparatorField);
		updateListBox(quoteField, String.valueOf(configuration.getQuote()), customQuoteField);
		commentField.setValue(String.valueOf(configuration.getComment()));

		charsetField.clear();
		for (String charset:configuration.getAvailablesCharset()) charsetField.addItem(charset);
		selectValue(charsetField, configuration.getCharset());
		
	}
	
	protected void updateListBox(ListBox listBox, String value, TextBox textBox)
	{
		boolean listBoxUpdated = selectValue(listBox, value);
		if (!listBoxUpdated) {
			textBox.setValue(value);
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
		String separator = getValue(separatorField, customSeparatorField);
		char separatorChar = TAB.equals(separator)?'\t':separator.charAt(0);
		configuration.setFieldSeparator(separatorChar);
		configuration.setHasHeader(hasHeaderField.getValue());
		configuration.setQuote(getValue(quoteField, customQuoteField).charAt(0));
		return configuration;
	}
	
	protected String getValue(ListBox listBox, TextBox textBox)
	{
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex<0) throw new IllegalStateException("Invalid selected index "+selectedIndex);
		String value = listBox.getValue(selectedIndex);
		if (CUSTOM.equals(value)) return textBox.getValue();
		else return value;
	}
}
