/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.header;

import org.cotrix.web.common.client.widgets.button.ButtonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class HeaderPanel extends Composite {

	private static HeaderPanelUiBinder uiBinder = GWT.create(HeaderPanelUiBinder.class);

	interface HeaderPanelUiBinder extends UiBinder<Widget, HeaderPanel> {}
	
	public enum BandDimension {SMALL, LARGE};
	private enum Alignment {TOP, MIDDLE};
	private enum TextSize {SMALL, LARGE};
	
	interface HeaderStyle extends CssResource {
		String iconCellAlignMiddle();
		String buttonCellAlignMiddle();
		String tableSelected();
		
		String titleSmall();
		
		String tableDisabled();
		String titleDisabled();
		String subtitleDisabled();
	}
	
	@UiField HeaderStyle style;
	
	@UiField TableElement table;
	
	@UiField TableCellElement iconCell;
	@UiField Image icon;
	
	@UiField FocusPanel titleFocus;
	@UiField Label title;
	
	@UiField TableCellElement primaryButtonCell;
	@UiField PushButton primaryButton;
	
	@UiField TableCellElement switchButtonCell;
	@UiField ToggleButton switchButton;
	
	@UiField TableCellElement firstButtonCell;
	@UiField PushButton firstButton;
	
	@UiField TableCellElement secondButtonCell;
	@UiField PushButton secondButton;
	
	@UiField FocusPanel subtitleFocus;
	@UiField Label subtitle;
	
	private boolean selected;
	private boolean disabled;
	private ImageResource iconResource;
	private ImageResource disabledIconResource;
	private String defaultBackgroundColor;

	public HeaderPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		defaultBackgroundColor = table.getStyle().getBackgroundColor();
		
		setPrimaryButtonVisible(false);
		setSwitchVisible(false);
		setSecondaryButtonsVisible(false);
		
		selected = false;
		disabled = false;
	}
	public void setIcon(ImageResource icon) {
		setIcon(icon, null);
	}
	
	public void setIcon(ImageResource icon, ImageResource disabledIcon) {
		this.icon.setResource(icon);
		iconResource = icon;
		disabledIconResource = disabledIcon;
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle.setText(subtitle);
	}
	
	public void setPrimaryButton(ButtonResources resources) {
		resources.apply(primaryButton); 
	}
	
	public void setSwitchButton(ButtonResources resources) {
		resources.apply(switchButton); 
	}
	
	public void setFirstButton(ButtonResources resources) {
		resources.apply(firstButton); 
	}
	
	public void setSecondButton(ButtonResources resources) {
		resources.apply(secondButton); 
	}
	
	public void setPrimaryButtonVisible(boolean visible) {
		UIObject.setVisible(primaryButtonCell, visible);
	}
	
	public void setSwitchVisible(boolean visible) {
		UIObject.setVisible(switchButtonCell, visible);
	}
	
	public void setSecondaryButtonsVisible(boolean visible) {
		UIObject.setVisible(firstButtonCell, visible);
		UIObject.setVisible(secondButtonCell, visible);
	}
	
	public void setFirstButtonsVisible(boolean visible) {
		UIObject.setVisible(firstButtonCell, visible);
	}
	
	public void setSecondButtonsVisible(boolean visible) {
		UIObject.setVisible(secondButtonCell, visible);
	}
	
	public void setSwitchDown(boolean down) {
		switchButton.setDown(down);
	}
	
	public boolean isSwitchDown() {
		return switchButton.isDown();
	}
	
	public void setBackgroundColor(String color) {
		table.getStyle().setBackgroundColor(color);
	}
	
	public void resetBackgroundColor() {
		table.getStyle().setBackgroundColor(defaultBackgroundColor);
	}
	
	public void setDimension(BandDimension dimension) {
		switch (dimension) {
			case LARGE: {
				alignElements(Alignment.TOP);
				setTextSize(TextSize.LARGE);
			} break;
			case SMALL: {
				alignElements(Alignment.MIDDLE);
				setTextSize(TextSize.SMALL);
			} break;
		}
	}
	
	private void alignElements(Alignment alignment) {
		switch (alignment) {
			case TOP: {
				iconCell.removeClassName(style.iconCellAlignMiddle());
				primaryButtonCell.removeClassName(style.buttonCellAlignMiddle());
				switchButtonCell.removeClassName(style.buttonCellAlignMiddle());
				firstButtonCell.removeClassName(style.buttonCellAlignMiddle());
				secondButtonCell.removeClassName(style.buttonCellAlignMiddle());
			} break;
			case MIDDLE: {
				iconCell.addClassName(style.iconCellAlignMiddle());
				primaryButtonCell.addClassName(style.buttonCellAlignMiddle());
				switchButtonCell.addClassName(style.buttonCellAlignMiddle());
				firstButtonCell.addClassName(style.buttonCellAlignMiddle());
				secondButtonCell.addClassName(style.buttonCellAlignMiddle());
			} break;
		}
	}
	
	private void setTextSize(TextSize textSize) {
		switch (textSize) {
			case LARGE: title.setStyleName(style.titleSmall(), false); break;
			case SMALL: title.setStyleName(style.titleSmall(), true); break;
		}
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		
		if (selected) table.addClassName(style.tableSelected());
		else table.removeClassName(style.tableSelected());
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		
		if (disabled) table.addClassName(style.tableDisabled());
		else table.removeClassName(style.tableDisabled());
		
		if (disabled && disabledIconResource!=null) icon.setResource(disabledIconResource);
		else icon.setResource(iconResource);
		
		title.setStyleName(style.titleDisabled(), disabled);
		
		primaryButton.setEnabled(!disabled);
		switchButton.setEnabled(!disabled);
		firstButton.setEnabled(!disabled);
		secondButton.setEnabled(!disabled);
		
		subtitle.setStyleName(style.subtitleDisabled(), disabled);
	}
	
	public boolean isDisabled() {
		return disabled;
	}

	public void addClickHandler(ClickHandler clickHandler) {
		titleFocus.addClickHandler(clickHandler);
		subtitleFocus.addClickHandler(clickHandler);
	}
	
	public void addPrimaryButtonClickHandler(ClickHandler clickHandler) {
		primaryButton.addClickHandler(clickHandler);
	}
	
	public void addSwitchButtonClickHandler(ClickHandler clickHandler) {
		switchButton.addClickHandler(clickHandler);
	}
	
	public void addFirstButtonClickHandler(ClickHandler clickHandler) {
		firstButton.addClickHandler(clickHandler);
	}
	
	public void addSecondButtonClickHandler(ClickHandler clickHandler) {
		secondButton.addClickHandler(clickHandler);
	}
	
	
}
