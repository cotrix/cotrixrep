package org.cotrix.web.client.view;

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

public class Publish extends Composite  {

	private static PublishUiBinder uiBinder = GWT.create(PublishUiBinder.class);

	interface PublishUiBinder extends UiBinder<Widget, Publish> {
	}

	public Publish() {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
