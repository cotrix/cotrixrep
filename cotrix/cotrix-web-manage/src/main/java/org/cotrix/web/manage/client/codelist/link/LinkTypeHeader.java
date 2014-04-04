/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import org.cotrix.web.common.client.util.FadeAnimation;
import org.cotrix.web.common.client.util.FadeAnimation.Speed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeHeader extends Composite implements HasClickHandlers {

	private static LinkTypeHeaderUiBinder uiBinder = GWT
			.create(LinkTypeHeaderUiBinder.class);

	interface LinkTypeHeaderUiBinder extends UiBinder<Widget, LinkTypeHeader> {
	}
	
	interface Style extends CssResource {
		String headerSelected();
	}
	
	public enum Button {EDIT, SAVE, REVERT;}
	
	public interface HeaderListener {
		public void onButtonClicked(Button button);
	}

	@UiField
	FocusPanel headerBox;
	
	@UiField
	InlineLabel headerLabel;
	
	@UiField
	DeckPanel controls;
	
	@UiField
	FlowPanel completeControls;
	FadeAnimation completeControlsAnimation;
	
	@UiField
	PushButton edit;
	FadeAnimation editAnimation;
	
	@UiField
	PushButton save;
	FadeAnimation saveAnimation;
	
	@UiField
	PushButton revert;
	FadeAnimation revertAnimation;
	
	@UiField
	Style style;
	
	private HeaderListener listener; 
	
	public LinkTypeHeader() {
		initWidget(uiBinder.createAndBindUi(this));
		
		editAnimation = new FadeAnimation(edit.getElement());
		
		completeControlsAnimation = new FadeAnimation(completeControls.getElement());
		saveAnimation = new FadeAnimation(save.getElement());
		revertAnimation = new FadeAnimation(revert.getElement());
	}

	public void setListener(HeaderListener listener) {
		this.listener = listener;
	}
	
	public void setHeaderLabel(String label) {
		this.headerLabel.setText(label);
	}
	
	public void setHeaderSelected(boolean selected) {
		this.headerLabel.setStyleName(style.headerSelected(), selected);
	}
	
	@UiHandler("edit")
	void onEdit(ClickEvent event) {
		fireOnClick(Button.EDIT);
	}
	
	@UiHandler("save")
	void onSave(ClickEvent event) {
		fireOnClick(Button.SAVE);
	}
	
	@UiHandler("revert")
	void onRevert(ClickEvent event) {
		fireOnClick(Button.REVERT);
	}
	
	private void fireOnClick(Button button) {
		if (listener!=null) listener.onButtonClicked(button);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return headerBox.addClickHandler(handler);
	}
	
	public void setSaveVisible(boolean visible) {
		saveAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	public void setRevertVisible(boolean visible) {
		revertAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	public void setEditVisible(boolean visible) {
		if (visible) controls.showWidget(controls.getWidgetIndex(edit));
		editAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	public void setControlsVisible(boolean visible) {
		if (visible) controls.showWidget(controls.getWidgetIndex(completeControls));
		completeControlsAnimation.setVisibility(visible, Speed.VERY_FAST);
	}

}
