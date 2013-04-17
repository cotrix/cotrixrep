package org.cotrix.web.publish.client.view;

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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ChanelPropertyItem extends Composite implements HasClickHandlers{

	private static ChanelPropertyItemUiBinder uiBinder = GWT
			.create(ChanelPropertyItemUiBinder.class);

	interface ChanelPropertyItemUiBinder extends
			UiBinder<Widget, ChanelPropertyItem> {
	}

	@UiField Label chanelName;
	@UiField CheckBox checkbox;
	
	public ChanelPropertyItem() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	public ChanelPropertyItem(String name) {
		initWidget(uiBinder.createAndBindUi(this));
		this.chanelName.setText(name);
	}
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		this.checkbox.addClickHandler(handler);
		return null;
	}
	
	public boolean isChecked(){
		return checkbox.getValue();
	}
	
}
