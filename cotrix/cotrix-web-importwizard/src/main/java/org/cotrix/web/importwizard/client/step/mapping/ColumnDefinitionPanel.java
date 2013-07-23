package org.cotrix.web.importwizard.client.step.mapping;

import java.util.Arrays;

import org.cotrix.web.importwizard.client.resources.ImportConstants;
import org.cotrix.web.importwizard.shared.ColumnType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ColumnDefinitionPanel extends Composite {

	private static ColumnDefinitionPanelUiBinder uiBinder = GWT.create(ColumnDefinitionPanelUiBinder.class);

	interface ColumnDefinitionPanelUiBinder extends UiBinder<Widget, ColumnDefinitionPanel> {
	}

	@UiField ListBox typeList;
	@UiField Label inLabel;
	@UiField ListBox languageList;

	@UiField
	Style style;

	interface Style extends CssResource {
		public String textPadding();
		String listBoxError();
		String listBox();
	}

	public ColumnDefinitionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		typeList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateLanguageList();
			}
		});
		
		String[] languages = ImportConstants.INSTANCE.languages();
		Arrays.sort(languages);
		for (String language:languages) languageList.addItem(language);
		
	}
	
	protected void updateLanguageList()
	{
		ColumnType columnType = getColumnType();
		if (columnType == null) setLanguageVisibile(false);
		else {
			switch (columnType) {
				case CODE: setLanguageVisibile(false); break;
				case DESCRIPTION: setLanguageVisibile(true); break;
			}
		}
	}
	
	protected void setLanguageVisibile(boolean visible)
	{
		languageList.setVisible(visible);
		inLabel.setVisible(visible);
	}
	
	public void setColumnType(ColumnType columnType)
	{
		if (columnType == null) typeList.setSelectedIndex(0);
		else {
			switch (columnType) {
				case CODE: typeList.setSelectedIndex(1); break;
				case DESCRIPTION: typeList.setSelectedIndex(2); break;
			}
		}
		updateLanguageList();
	}
	
	public void setLanguage(String language)
	{
		if (language == null) return;
		for (int i = 0; i < languageList.getItemCount(); i++) {
			if (languageList.getItemText(i).equals(language)) languageList.setSelectedIndex(i);
		}
	}
	
	public ColumnType getColumnType()
	{
		String typeValue = typeList.getValue(typeList.getSelectedIndex());
		int value = Integer.valueOf(typeValue);
		switch (value) {
			case 0: return null;
			case 1: return ColumnType.CODE;
			case 2: return ColumnType.DESCRIPTION;
			default: return null;
		}
	}
	
	public String getLanguage()
	{
		if (languageList.getSelectedIndex()<0) return null;
		return languageList.getValue(languageList.getSelectedIndex());
	}
	
	public void setErrorStyle(){
		typeList.setStyleName(style.listBoxError());
	}
	
	public void setNormalStyle(){
		typeList.setStyleName(style.listBox());
	}
}
