package org.cotrix.web.importwizard.client.step.done;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DoneStepViewImpl extends Composite implements DoneStepView {

	private static DoneStepViewUiBinder uiBinder = GWT.create(DoneStepViewUiBinder.class);

	@UiTemplate("DoneStepView.ui.xml")
	interface DoneStepViewUiBinder extends UiBinder<Widget, DoneStepViewImpl> {
	}
	
	@UiField Label title;
	@UiField Label message;

	public DoneStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setMessage(String message) {
		this.message.setText(message);
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}
}
