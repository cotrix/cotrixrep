package org.cotrix.web.importwizard.client.view.form;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenterImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HeaderSelectionFormViewImpl extends Composite implements  HeaderSelectionFormView<HeaderSelectionFormViewImpl>{

	@UiTemplate("HeaderSelectionForm.ui.xml")
	interface HeaderSelectionFormUiBinder extends UiBinder<Widget, HeaderSelectionFormViewImpl> {}
	private static HeaderSelectionFormUiBinder uiBinder = GWT.create(HeaderSelectionFormUiBinder.class);

	@UiField FlexTable flexTable;
	@UiField CheckBox checkbox;
	@UiField Style style;
	interface Style extends CssResource {
		String flextTableHeader();
		String textbox();
	}
	private int columnCount = 0;
	private AlertDialog alertDialog;

	private Presenter presenter;
	public void setPresenter(HeaderSelectionFormPresenterImpl presenter) {
		this.presenter = presenter;
	}

	@UiHandler("checkbox")
	public void onChecked(ClickEvent event) {
		presenter.onCheckBoxChecked(checkbox.getValue());
	}

	public HeaderSelectionFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void showHeaderForm(boolean show){
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

	public void setData(String[] headers, ArrayList<String[]> data) {
		this.columnCount = headers.length;
		for (int i = 0; i < headers.length; i++) {
			flexTable.setWidget(1, i, new HTML(headers[i]));
			flexTable.getCellFormatter().setStyleName(1, i,style.flextTableHeader());
		}

		int rowCount = (data.size()<8)?data.size():8;
		for (int i = 1; i < rowCount; i++) {
			String[] columns = data.get(i);
			for (int j = 0; j < columns.length; j++) {
				flexTable.setWidget(i+1, j, new HTML(columns[j]));
			}
		}
	}

	public ArrayList<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		if(checkbox.getValue()){
			for (int i = 0; i < flexTable.getCellCount(1); i++) {
				HTML column = (HTML) flexTable.getWidget(1, i);
				headers.add(column.getText());
			}
		}else{
			for (int i = 0; i < flexTable.getCellCount(0); i++) {
				TextBox column = (TextBox) flexTable.getWidget(0, i);
				if(column.getText().length() > 0){
					headers.add(column.getText());
				}
			}
		}
		return headers;
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}


}
