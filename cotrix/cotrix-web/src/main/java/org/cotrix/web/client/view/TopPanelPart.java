package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class TopPanelPart extends Composite implements HasText {

	private static TopPanelPartUiBinder uiBinder = GWT
			.create(TopPanelPartUiBinder.class);

	interface TopPanelPartUiBinder extends UiBinder<Widget, TopPanelPart> {
	}

	public TopPanelPart() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TopPanelPart(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		//button.setText(firstName);
	}

	public void setText(String text) {
		//button.setText(text);
	}

	public String getText() {
		//return button.getText();
		return "FixMe";
	}

}
