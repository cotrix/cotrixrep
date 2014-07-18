/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerPanelHeader extends Composite implements HasClickHandlers {

	private static LinkTypeHeaderUiBinder uiBinder = GWT.create(LinkTypeHeaderUiBinder.class);

	interface LinkTypeHeaderUiBinder extends UiBinder<Widget, MarkerPanelHeader> {
	}
	
	public interface HeaderListener {
		public void onHeaderClicked();
		public void onSwitchChange(boolean isDown);
	}
	
	@UiField
	ToggleButton switchButton;
	
	@UiField
	InlineLabel headerLabel;
	
	@UiField
	ToggleButton expandButton;
	
	private HeaderListener listener; 
	
	public MarkerPanelHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setSwitchDown(boolean down) {
		switchButton.setDown(down);
	}
	
	public void setExpandDown(boolean down) {
		expandButton.setDown(down);
	}
	
	public void setExpandVisible(boolean visible) {
		expandButton.setVisible(visible);
	}
	
	public void setSwitchEnabled(boolean enabled) {
		switchButton.setEnabled(enabled);
	}

	public void setListener(HeaderListener listener) {
		this.listener = listener;
	}
	
	public void setHeaderLabel(String label) {
		this.headerLabel.setText(label);
		this.headerLabel.setTitle(label);
	}
	
	public void setBackgroundColor(String color) {
		getElement().getStyle().setBackgroundColor(color);
	}
	
	public void setLabelColor(String color) {
		headerLabel.getElement().getStyle().setColor(color);
	}
	
	public void setHeaderStyle(String style) {
		this.headerLabel.setStyleName(style);
	}
	
	public void addHeaderStyle(String style) {
		this.headerLabel.addStyleName(style);
	}

	@UiHandler("headerLabel")
	void onHeaderLabel(ClickEvent event) {
		if (listener!=null) listener.onHeaderClicked();
	}
	
	@UiHandler("switchButton")
	void onSwitch(ClickEvent event) {
		if (listener!=null) listener.onSwitchChange(switchButton.isDown());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return expandButton.addClickHandler(handler);
	}
}
