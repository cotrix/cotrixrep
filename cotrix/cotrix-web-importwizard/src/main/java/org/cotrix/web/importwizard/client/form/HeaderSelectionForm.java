package org.cotrix.web.importwizard.client.form;

import java.util.ArrayList;

import org.cotrix.web.importwizard.shared.CSVFile.OnFile3ChangeHandler;
import org.cotrix.web.importwizard.shared.ImportWizardModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HeaderSelectionForm extends Composite implements CotrixForm,OnFile3ChangeHandler{
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
	
	@UiField
	CheckBox checkbox;

	interface Style extends CssResource {
		String flextTableHeader();
		String textbox();
	}
	
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
	private ImportWizardModel importWizardModel;
	public HeaderSelectionForm(ImportWizardModel importWizardModel) {
		this.importWizardModel = importWizardModel;
		importWizardModel.getCsvFile().setOnFile3ChangeHandler(this);
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void initFlexTable(FlexTable flexTable,String[] headers, ArrayList<String[]> data) {
		this.columnCount = headers.length;
		
		int rowCount = (data.size()<8)?data.size():8;
		for (int i = 0; i < rowCount; i++) {
			String[] columns = data.get(i);
			for (int j = 0; j < columns.length; j++) {
				flexTable.setWidget(i+1, j, new HTML(columns[j]));
				if (i == 0) {
					flexTable.getCellFormatter().setStyleName(i+1, j,
							style.flextTableHeader());
				}
			}
		}
	}
	public boolean isValidate() {
		return true;
	}


	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		initFlexTable(flexTable, headers, data);
	}

}
