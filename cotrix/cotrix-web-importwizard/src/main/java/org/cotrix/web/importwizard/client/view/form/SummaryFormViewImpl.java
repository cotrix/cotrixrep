package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SummaryFormViewImpl extends Composite implements SummaryFormView<SummaryFormViewImpl>{

	@UiTemplate("SummaryForm.ui.xml")
	interface SummaryFormUiBinder extends UiBinder<Widget, SummaryFormViewImpl> {}
	private static SummaryFormUiBinder uiBinder = GWT.create(SummaryFormUiBinder.class);


	@UiField HTMLPanel panel;
	@UiField Style style;
	interface Style extends CssResource {
		String headerlabel();
		String valuelabel();
		String grid();
		String cell();
	}
	
	private Presenter<SummaryFormPresenterImpl> presenter;

	public SummaryFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initForm(String[] headers) {
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
	}
	
	public void setPresenter(SummaryFormPresenterImpl presenter) {
		this.presenter = presenter;
	}

}