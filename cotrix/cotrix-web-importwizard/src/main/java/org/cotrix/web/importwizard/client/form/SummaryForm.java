package org.cotrix.web.importwizard.client.form;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.form.DescribeHeaderForm.Style;
import org.cotrix.web.importwizard.shared.CSVFile.OnFile4ChangeHandler;
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

public class SummaryForm extends Composite implements CotrixForm ,OnFile4ChangeHandler{

	private static SummaryFormUiBinder uiBinder = GWT
			.create(SummaryFormUiBinder.class);

	interface SummaryFormUiBinder extends UiBinder<Widget, SummaryForm> {
	}

	@UiField
	HTMLPanel panel;

	@UiField
	Style style;

	interface Style extends CssResource {
		String headerlabel();
		String valuelabel();
		String grid();
		String cell();
	}
	
	private ImportWizardModel importWizardModel;
	public SummaryForm(ImportWizardModel importWizardModel) {
		this.importWizardModel = importWizardModel;
		this.importWizardModel.getCsvFile().setOnFile4ChangeHandler(this);
		initWidget(uiBinder.createAndBindUi(this));
	}

	public boolean isValidate() {
		return true;
	}
	private Grid initGrid(String[] headers) {
		Grid grid = new Grid(headers.length, 2);

		for (int i = 0; i < headers.length; i++) {
			Label headerLabel = new Label(headers[i]);
			headerLabel.setStyleName(style.headerlabel());
		
			Label valueLabel = new Label("value"+i+1);
			valueLabel.setStyleName(style.valuelabel());
			
			grid.getCellFormatter().setStyleName(i, 0, style.cell());
			grid.setWidget(i, 0, headerLabel);
			grid.setWidget(i, 1, valueLabel);
		}
		return grid;
	}
	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		panel.clear();
		Grid grid = initGrid(headers);
		panel.add(grid);
	}


}
