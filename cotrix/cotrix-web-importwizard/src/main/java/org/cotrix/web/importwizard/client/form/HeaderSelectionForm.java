package org.cotrix.web.importwizard.client.form;

import java.util.List;

import org.cotrix.web.importwizard.client.data.MockupTabularData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HeaderSelectionForm extends Composite {
	int columnCount;
	private static HeaderSelectionFormUiBinder uiBinder = GWT
			.create(HeaderSelectionFormUiBinder.class);

	interface HeaderSelectionFormUiBinder extends
			UiBinder<Widget, HeaderSelectionForm> {
	}

	@UiField
	FlexTable flexTable;
	
	@UiField
	Style style;
	interface Style extends CssResource {
		String flextTableHeader();
		String textbox();
		
	}
	
	@UiField
	CheckBox checkbox;
	@UiHandler("checkbox")
	void handleBackButtonClick(ClickEvent e) {
		showHeaderForm(!checkbox.getValue());
	}
	private void showHeaderForm(boolean show){
		if(show){
			for (int j = 0; j < columnCount; j++) {
				TextBox t = new TextBox();
				t.setStyleName(style.textbox());
				flexTable.setWidget(0, j,t);
				flexTable.getCellFormatter().setStyleName(0, j, style.flextTableHeader());
			}
			setDefaultSelected(false);
		}else{
			setDefaultSelected(true);
			flexTable.removeRow(0);
			flexTable.insertRow(0);
		}
	}
	private void setDefaultSelected(boolean show){
		for (int j = 0; j < columnCount; j++) {
			if(show){
				flexTable.getCellFormatter().setStyleName(1, j, style.flextTableHeader());
			}else{
				flexTable.getCellFormatter().removeStyleName(1, j, style.flextTableHeader());
			}
		}

	}
	public HeaderSelectionForm() {
		initWidget(uiBinder.createAndBindUi(this));
		initFlexTable(flexTable);
		
	}

	private void initFlexTable(FlexTable flexTable) {
		MockupTabularData mMockupTabularData = new MockupTabularData();
		List<String[]> table = mMockupTabularData.getTable();
		this.columnCount = table.get(0).length;
		
		int i = 1;
		for (String[] columns : table) {
			for (int j = 0; j < columns.length; j++) {
				flexTable.setWidget(i, j, new HTML(columns[j]));
				if (i == 1) {
					flexTable.getCellFormatter().setStyleName(i, j,
							style.flextTableHeader());
				}
			}
			i++;
		}
	}

}
