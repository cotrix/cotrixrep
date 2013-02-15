package org.cotrix.web.importwizard.client.form;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.form.HeaderTypeForm.Style;
import org.cotrix.web.importwizard.shared.CSVFile.OnFile2ChangeHandler;
import org.cotrix.web.importwizard.shared.ImportWizardModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class DescribeHeaderForm extends Composite  implements CotrixForm,OnFile2ChangeHandler{

	private static DescribeHeaderFormUiBinder uiBinder = GWT
			.create(DescribeHeaderFormUiBinder.class);

	interface DescribeHeaderFormUiBinder extends
			UiBinder<Widget, DescribeHeaderForm> {
	}
	
	@UiField
	HTMLPanel panel;

	@UiField
	Style style;

	interface Style extends CssResource {
		String textarea();
		String label();
		String grid();
		String cell();
	}
	private ImportWizardModel importWizardModel;
	public DescribeHeaderForm(ImportWizardModel importWizardModel) {
		this.importWizardModel = importWizardModel;
		this.importWizardModel.getCsvFile().setOnFile2ChangeHandler(this);
		initWidget(uiBinder.createAndBindUi(this));
	}
	private Grid initGrid(String[] headers) {
		Grid grid = new Grid(headers.length, 2);

		for (int i = 0; i < headers.length; i++) {
			Label headerLabel = new Label(headers[i]);
			headerLabel.setStyleName(style.label());
			
			TextArea textArea = new TextArea();
			textArea.setStyleName(style.textarea());
			
			grid.getCellFormatter().setStyleName(i, 0, style.cell());
			grid.setWidget(i, 0, headerLabel);
			grid.setWidget(i, 1, textArea);
		}
		return grid;
	}

	public boolean isValidate() {
		
		return true;
	}
	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		panel.clear();
		Grid grid = initGrid(headers);
		panel.add(grid);
	}

}
