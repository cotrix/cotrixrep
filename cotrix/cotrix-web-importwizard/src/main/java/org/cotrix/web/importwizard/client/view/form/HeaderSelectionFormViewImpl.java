package org.cotrix.web.importwizard.client.view.form;

import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenterImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class HeaderSelectionFormViewImpl extends Composite implements  HeaderSelectionFormView<HeaderSelectionFormViewImpl>{
	
	@UiTemplate("HeaderSelectionForm.ui.xml")
	interface HeaderSelectionFormUiBinder extends UiBinder<Widget, HeaderSelectionFormViewImpl> {}
	private static HeaderSelectionFormUiBinder uiBinder = GWT.create(HeaderSelectionFormUiBinder.class);

	@UiField FlexTable flexTable;
	@UiField CheckBox checkbox;

//	@UiField Style style;
//	interface Style extends CssResource {
//		String flextTableHeader();
//		String textbox();
//	}
	
	private Presenter presenter;
	
	@UiHandler("checkbox")
	public void onChecked(ClickEvent event) {
		
	}
	
	public HeaderSelectionFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(HeaderSelectionFormPresenterImpl presenter) {
		this.presenter = presenter;
	}


}
