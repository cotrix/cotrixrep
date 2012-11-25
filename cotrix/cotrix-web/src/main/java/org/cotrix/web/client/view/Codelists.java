package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class Codelists extends Composite {

	private static CodelistsUiBinder uiBinder = GWT
			.create(CodelistsUiBinder.class);

	interface CodelistsUiBinder extends UiBinder<Widget, Codelists> {
	}

	public Codelists() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField Tree codelistsTree;

}
