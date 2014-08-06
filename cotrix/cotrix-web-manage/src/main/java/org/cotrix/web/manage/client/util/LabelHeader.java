/**
 * 
 */
package org.cotrix.web.manage.client.util;

import org.cotrix.web.common.client.util.FadeAnimation;
import org.cotrix.web.common.client.util.FadeAnimation.Speed;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelHeader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LabelHeader extends Composite implements ItemPanelHeader {

	private static LinkTypeHeaderUiBinder uiBinder = GWT
			.create(LinkTypeHeaderUiBinder.class);

	interface LinkTypeHeaderUiBinder extends UiBinder<Widget, LabelHeader> {
	}
	
	interface Style extends CssResource {
		String headerSelected();
	}

	@UiField
	HTMLPanel header;
	
	@UiField
	FocusPanel headerBox;
	
	@UiField
	TableCellElement images;
	
	@UiField 
	DeckPanel labelControls;
	@UiField
	Image bullet;
	
	@UiField
	InlineLabel headerLabel;
	
	@UiField
	InlineLabel headerLabelValue;
	FadeAnimation headerLabelValueAnimation;
	
	@UiField
	TableCellElement controlsCell;
	
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
	
	public LabelHeader() {
		initWidget(uiBinder.createAndBindUi(this));
		
		editAnimation = new FadeAnimation(edit.getElement());
		
		completeControlsAnimation = new FadeAnimation(completeControls.getElement());
		/*completeControlsAnimation.setListener(new AnimationListener() {
			
			@Override
			public void onComplete() {
				controlsCell.getStyle().setProperty("display", completeControls.isVisible()?"table-cell":"none");
			}
		});*/
		
		saveAnimation = new FadeAnimation(save.getElement(), FadeAnimation.VISIBLE_OPACITY, 0.2);
		revertAnimation = new FadeAnimation(revert.getElement());
		setSwitchVisible(false);
		
		headerLabelValueAnimation = new FadeAnimation(headerLabelValue.getElement(), true);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void setSwitchVisible(boolean visible) {
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setSwitchDown(boolean down) {
	}
	
	public void setBulletImage(ImageResource image) {
		bullet.setResource(image);
		bullet.setWidth(image.getWidth()+"px");
		bullet.setHeight(image.getHeight()+"px");
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setListener(HeaderListener listener) {
		this.listener = listener;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setHeaderTitle(String label) {
		this.headerLabel.setText(label);
		this.headerLabel.setTitle(label);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setHeaderSubtitle(String value) {
		this.headerLabelValue.setText(value);
		this.headerLabelValue.setTitle(value);
	}
	
	public void setHeaderValueVisible(boolean visible) {
		headerLabelValueAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	public void setEditTitle(String title) {
		edit.setTitle(title);
	}
	
	public void setSaveTitle(String title) {
		save.setTitle(title);
	}
	
	public void setRevertTitle(String title) {
		revert.setTitle(title);
	}
	
	public void setHeaderStyle(String style) {
		this.headerLabel.setStyleName(style);
	}
	
	public void addHeaderStyle(String style) {
		this.headerLabel.addStyleName(style);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setHeaderSelected(boolean selected) {
		header.setStyleName(style.headerSelected(), selected);
	}
	
	@UiHandler("edit")
	void onEdit(ClickEvent event) {
		fireOnClick(Button.EDIT);
	}
	
	@UiHandler("save")
	void onSave(ClickEvent event) {
		if (saveAnimation.isElementVisible()) fireOnClick(Button.SAVE);
	}
	
	@UiHandler("revert")
	void onRevert(ClickEvent event) {
		fireOnClick(Button.REVERT);
	}
	
	private void fireOnClick(Button button) {
		if (listener!=null) listener.onButtonClicked(button);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void addClickHandler(ClickHandler handler) {
		headerBox.addClickHandler(handler);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setSaveVisible(boolean visible) {
		saveAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setRevertVisible(boolean visible) {
		revertAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setEditVisible(boolean visible) {
		if (visible) controls.showWidget(controls.getWidgetIndex(edit));
		editAnimation.setVisibility(visible, Speed.VERY_FAST);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setControlsVisible(boolean visible) {
		if (visible) controls.showWidget(controls.getWidgetIndex(completeControls));
		completeControlsAnimation.setVisibility(visible, Speed.VERY_FAST);
	}

}
