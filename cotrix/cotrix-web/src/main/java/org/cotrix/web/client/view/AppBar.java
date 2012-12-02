package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppBar extends Composite {

	private static AppBarUiBinder uiBinder = GWT.create(AppBarUiBinder.class);

	interface AppBarUiBinder extends UiBinder<Widget, AppBar> {
	}

	public AppBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
