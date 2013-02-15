package org.cotrix.web.importwizard.client.form;

import java.util.ArrayList;

import org.cotrix.web.importwizard.shared.ImportWizardModel;
import org.cotrix.web.importwizard.shared.CSVFile.OnFile1ChangeHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderTypeForm extends Composite implements CotrixForm,
		OnFile1ChangeHandler {

	private static HeaderTypeFormUiBinder uiBinder = GWT
			.create(HeaderTypeFormUiBinder.class);

	interface HeaderTypeFormUiBinder extends UiBinder<Widget, HeaderTypeForm> {

	}

	@UiField
	HTMLPanel panel;

	@UiField
	Style style;

	interface Style extends CssResource {
		String headerlabel();

		String cell();

		String valuelabel();
	}

	private ImportWizardModel importWizardModel;

	public HeaderTypeForm(ImportWizardModel importWizardModel) {
		this.importWizardModel = importWizardModel;
		this.importWizardModel.getCsvFile().setOnFile1ChangeHandler(this);
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Grid initGrid(String[] columns) {
		Grid grid = new Grid(columns.length, 2);

		for (int i = 0; i < columns.length; i++) {
			Label headerLabel = new Label(columns[i]);
			headerLabel.setStyleName(style.headerlabel());

			HeaderTypePanel h = new HeaderTypePanel();


			grid.getCellFormatter().setStyleName(i, 0, style.cell());
			grid.setWidget(i, 0, headerLabel);
			grid.setWidget(i, 1, h);
		}
		return grid;
	}

	public boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onFileChange(String[] headers, ArrayList<String[]> data) {
		panel.clear();
		Grid grid = initGrid(headers);
		panel.add(grid);
		
	}

}
