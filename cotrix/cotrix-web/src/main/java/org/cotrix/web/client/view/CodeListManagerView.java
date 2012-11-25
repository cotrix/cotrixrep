package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CodeListManagerView extends Composite {

	private static CodeListManagerViewUiBinder uiBinder = GWT
			.create(CodeListManagerViewUiBinder.class);

	interface CodeListManagerViewUiBinder extends
			UiBinder<Widget, CodeListManagerView> {
	}

	public CodeListManagerView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField TopPanelPart topPanel;
	@UiField NavBarPart navBar;
	@UiField SimplePanel summary;
	@UiField SimplePanel main;

}
