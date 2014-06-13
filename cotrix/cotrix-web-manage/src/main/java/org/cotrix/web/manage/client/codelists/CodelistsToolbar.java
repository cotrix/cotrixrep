/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.util.FadeAnimation.Speed;

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
public class CodelistsToolbar extends Composite {

	public enum ToolBarButton {MINUS, VERSION, PLUS};

	@UiTemplate("CodelistsToolbar.ui.xml")
	interface CodelistsToolbarUiBinder extends UiBinder<Widget, CodelistsToolbar> {}
	private static CodelistsToolbarUiBinder uiBinder = GWT.create(CodelistsToolbarUiBinder.class);
	
	@UiField PushButton plus;
	@UiField PushButton version;
	@UiField PushButton minus;

	public CodelistsToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("plus")
	void onPlusClick(ClickEvent event)
	{
		fireEvent(new ButtonClickedEvent(ToolBarButton.PLUS));
	}
	
	@UiHandler("version")
	void onVersionClick(ClickEvent event)
	{
		fireEvent(new ButtonClickedEvent(ToolBarButton.VERSION));
	}

	@UiHandler("minus")
	void onMinusClick(ClickEvent event)
	{
		fireEvent(new ButtonClickedEvent(ToolBarButton.MINUS));
	}
	
	public HandlerRegistration addButtonClickedHandler(ButtonClickedHandler handler)
	{
		return addHandler(handler, ButtonClickedEvent.getType());
	}
	
	public void setVisible(ToolBarButton button, boolean visible, boolean animation)
	{
		setVisible(button, visible);
	}

	public void setVisible(ToolBarButton button, boolean visible, Speed speed)
	{
		setVisible(button, visible);
	}
	
	public void setVisible(ToolBarButton button, boolean visible)
	{
		switch (button) {
			case MINUS: minus.setEnabled(visible); break;
			case VERSION: version.setEnabled(visible); break;
			case PLUS: plus.setEnabled(visible); break;
		}
	}

	public interface ButtonClickedHandler extends EventHandler {
		void onButtonClicked(ButtonClickedEvent event);
	}

	public static class ButtonClickedEvent extends GwtEvent<ButtonClickedHandler> {

		public static Type<ButtonClickedHandler> TYPE = new Type<ButtonClickedHandler>();
		
		protected ToolBarButton button;

		public ButtonClickedEvent(ToolBarButton button) {
			this.button = button;
		}

		/**
		 * @return the button
		 */
		public ToolBarButton getButton() {
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
