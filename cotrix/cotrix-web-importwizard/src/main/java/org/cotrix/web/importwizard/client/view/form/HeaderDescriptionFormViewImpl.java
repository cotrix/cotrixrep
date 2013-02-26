package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenterImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class HeaderDescriptionFormViewImpl extends Composite  implements HeaderDescriptionFormView<HeaderDescriptionFormViewImpl>{

	@UiTemplate("HeaderDescriptionForm.ui.xml")
	interface DescribeHeaderFormUiBinder extends UiBinder<Widget, HeaderDescriptionFormViewImpl> {}
	private static DescribeHeaderFormUiBinder uiBinder = GWT.create(DescribeHeaderFormUiBinder.class);
	
	@UiField HTMLPanel panel;

	@UiField Style style;
	interface Style extends CssResource {
		String textarea();
		String label();
		String grid();
		String cell();
	}
	
	private Presenter<HeaderDescriptionPresenterImpl> presenter;

	public HeaderDescriptionFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void initForm(String[] headers) {
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
		panel.add(grid);
	}

	public void setPresenter(HeaderDescriptionPresenterImpl presenter) {
		this.presenter  = presenter;
	}

}
