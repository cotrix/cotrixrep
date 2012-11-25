package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class NavBarPart extends Composite implements HasText {

	private static NavBarPartUiBinder uiBinder = GWT
			.create(NavBarPartUiBinder.class);

	interface NavBarPartUiBinder extends UiBinder<Widget, NavBarPart> {
	}

	public NavBarPart() {
		initWidget(uiBinder.createAndBindUi(this));
	}

//	@UiField
//	Button button;
	@UiField Codelists codelists;

	public NavBarPart(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
//		button.setText(firstName);
	}

//	@UiHandler("button")
//	void onClick(ClickEvent e) {
//		Window.alert("Hello!");
//	}

	public void setText(String text) {
//		button.setText(text);
	}

	public String getText() {
//		return button.getText();
		return "FixMe";
	}

}
