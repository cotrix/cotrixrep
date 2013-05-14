package org.cotrix.web.importwizard.client.view.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DoneFormViewImpl extends Composite implements DoneFormView {

	private static DoneFormViewUiBinder uiBinder = GWT
			.create(DoneFormViewUiBinder.class);

	@UiTemplate("DoneFormView.ui.xml")
	interface DoneFormViewUiBinder extends UiBinder<Widget, DoneFormViewImpl> {
	}
	
	@UiField Label title;
	@UiField Label message;
	
	private Presenter presenter;
	public DoneFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setWarningMessage(String message) {
		this.message.setText(message);
	}

	public void setDoneTitle(String title) {
		this.title.setText(title);
	}


}
