/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.util.FadeAnimation;
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
	
	private FadeAnimation plusAnimation;
	private FadeAnimation versionAnimation;
	private FadeAnimation minusAnimation;

	public CodelistsToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
		plusAnimation = new FadeAnimation(plus.getElement());
		versionAnimation = new FadeAnimation(version.getElement());
		minusAnimation = new FadeAnimation(minus.getElement());
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
		if (animation) setVisible(button, visible);
		else setVisible(button, visible, Speed.IMMEDIATE);
	}

	public void setVisible(ToolBarButton button, boolean visible)
	{
		setVisible(button, visible, Speed.FAST);
	}
	
	public void setVisible(ToolBarButton button, boolean visible, Speed speed)
	{
		switch (button) {
			case MINUS: minusAnimation.setVisibility(visible, speed); break;
			case VERSION: versionAnimation.setVisibility(visible, speed); break;
			case PLUS: plusAnimation.setVisibility(visible, speed); break;
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
