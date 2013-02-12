package org.cotrix.web.importwizard.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class HeaderTypeForm extends Composite  {

	private static HeaderTypeFormUiBinder uiBinder = GWT
			.create(HeaderTypeFormUiBinder.class);

	interface HeaderTypeFormUiBinder extends UiBinder<Widget, HeaderTypeForm> {
	
	}

	public HeaderTypeForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
