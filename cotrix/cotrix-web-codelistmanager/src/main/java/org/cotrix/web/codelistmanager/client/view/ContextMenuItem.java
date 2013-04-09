package org.cotrix.web.codelistmanager.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ContextMenuItem extends Composite implements HasClickHandlers {

	
	private static ContextMenuItemUiBinder uiBinder = GWT
			.create(ContextMenuItemUiBinder.class);

	interface ContextMenuItemUiBinder extends UiBinder<Widget, ContextMenuItem> {
	}

	@UiField Label itemLabel;
	@UiField FocusPanel item;
	
	public ContextMenuItem(String name) {
		initWidget(uiBinder.createAndBindUi(this));
		this.itemLabel.setText(name);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		this.item.addClickHandler(handler);
		return null;
	}
}
