/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import org.cotrix.web.common.client.widgets.button.ButtonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemToolbar extends Composite {

	public enum ItemButton {MINUS, PLUS};

	@UiTemplate("ItemToolbar.ui.xml")
	interface ItemToolbarUiBinder extends UiBinder<Widget, ItemToolbar> {}
	private static ItemToolbarUiBinder uiBinder = GWT.create(ItemToolbarUiBinder.class);
	
	@UiField PushButton plus;
	@UiField PushButton minus;

	public ItemToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("plus")
	protected void onPlusClick(ClickEvent event)
	{
		fireEvent(new ButtonClickedEvent(ItemButton.PLUS));
	}

	@UiHandler("minus")
	protected void onMinusClick(ClickEvent event)
	{
		fireEvent(new ButtonClickedEvent(ItemButton.MINUS));
	}
	
	public HandlerRegistration addButtonClickedHandler(ButtonClickedHandler handler)
	{
		return addHandler(handler, ButtonClickedEvent.getType());
	}
	
	public void setEnabled(ItemButton button, boolean visible, boolean animation)
	{
		setEnabled(button, visible);
	}

	public void setEnabled(ItemButton button, boolean visible)
	{
		switch (button) {
			case MINUS: minus.setEnabled(visible); break;
			case PLUS: plus.setEnabled(visible); break;
		}
	}
	
	public void setVisible(ItemButton button, boolean visible)
	{
		switch (button) {
			case MINUS: minus.setVisible(visible); break;
			case PLUS: plus.setVisible(visible); break;
		}
	}
	
	public void setButtonResource(ItemButton button, ButtonResources resources) {
		switch (button) {
			case MINUS: resources.apply(minus); break;
			case PLUS: resources.apply(plus); break;
		}
	}

	public interface ButtonClickedHandler extends EventHandler {
		void onButtonClicked(ButtonClickedEvent event);
	}

	public static class ButtonClickedEvent extends GwtEvent<ButtonClickedHandler> {

		public static Type<ButtonClickedHandler> TYPE = new Type<ButtonClickedHandler>();
		
		protected ItemButton button;

		public ButtonClickedEvent(ItemButton button) {
			this.button = button;
		}

		/**
		 * @return the button
		 */
		public ItemButton getButton() {
			return button;
		}

		@Override
		protected void dispatch(ButtonClickedHandler handler) {
			handler.onButtonClicked(this);
		}

		@Override
		public Type<ButtonClickedHandler> getAssociatedType() {
			return TYPE;
		}

		public static Type<ButtonClickedHandler> getType() {
			return TYPE;
		}
	}

}
