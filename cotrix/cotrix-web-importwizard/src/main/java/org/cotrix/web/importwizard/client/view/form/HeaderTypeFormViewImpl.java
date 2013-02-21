package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderTypeFormPresenter;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView.Presenter;

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

public class HeaderTypeFormViewImpl extends Composite implements HeaderTypeFormView<HeaderTypeFormViewImpl> {

	@UiTemplate("HeaderTypeForm.ui.xml")
	interface HeaderTypeFormUiBinder extends UiBinder<Widget, HeaderTypeFormViewImpl> {}
	private static HeaderTypeFormUiBinder uiBinder = GWT.create(HeaderTypeFormUiBinder.class);

	@UiField HTMLPanel panel;
	@UiField Style style;
	interface Style extends CssResource {
		String headerlabel();
		String cell();
		String valuelabel();
	}
	
	private Presenter<HeaderTypeFormPresenter> presenter;
	public void setPresenter(Presenter<HeaderTypeFormPresenter> presenter) {
		this.presenter = presenter;
	}

	public HeaderTypeFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initForm(String[] headers) {
		Grid grid = new Grid(headers.length, 2);

		for (int i = 0; i < headers.length; i++) {
			Label headerLabel = new Label(headers[i]);
			headerLabel.setStyleName(style.headerlabel());

			HeaderTypePanel h = new HeaderTypePanel();

			grid.getCellFormatter().setStyleName(i, 0, style.cell());
			grid.setWidget(i, 0, headerLabel);
			grid.setWidget(i, 1, h);
		}
		panel.add(grid);
	}
}
