/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.menu;

import org.cotrix.web.manage.shared.Group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ColumnPanel extends Composite {

	private static ColumnPanelUiBinder uiBinder = GWT.create(ColumnPanelUiBinder.class);

	interface ColumnPanelUiBinder extends UiBinder<Widget, ColumnPanel> {}
	
	interface ColumnPanelStyle extends CssResource {
		String tableDisabled();
		String titleDisabled();
		String subtitleDisabled();
	}
	
	public interface ColumnPanelListener {
		public void onUpClicked();
		public void onDownClicked();
		public void onCheckClicked(boolean down);
	}
	
	@UiField ColumnPanelStyle style;
	
	@UiField TableElement table;
	
	@UiField TableCellElement iconCell;
	@UiField ToggleButton switchButton;
	
	@UiField FocusPanel titleFocus;
	@UiField Label title;
		
	@UiField PushButton upButton;
	@UiField PushButton downButton;
	
	@UiField FocusPanel subtitleFocus;
	@UiField Label subtitle;
	
	private Group group;
	private ColumnPanelListener listener;
	private boolean active;

	public ColumnPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setListener(ColumnPanelListener listener) {
		this.listener = listener;
	}
	
	public void setGroup(Group group) {
		this.group = group;
		
		String name = group.getName().getLocalPart();
		title.setText(name);
		
		String subtitle = group.getSubtitle();
		this.subtitle.setText(subtitle);
	}
	
	public Group getGroup() {
		return group;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		title.setStyleName(style.titleDisabled(), !active);
		subtitle.setStyleName(style.subtitleDisabled(), !active);
		switchButton.setValue(active);
	}
	
	public void setButtons(boolean upActive, boolean downActive) {
		upButton.setEnabled(active && upActive);
		downButton.setEnabled(active && downActive);
	}
	
	public boolean isActive() {
		return active;
	}
	
	@UiHandler("upButton")
	public void onUpButtonClicked(ClickEvent event) {
		listener.onUpClicked();
	}
	
	@UiHandler("downButton")
	public void onDownButtonClicked(ClickEvent event) {
		listener.onDownClicked();
	}
	
	@UiHandler("switchButton")
	public void onCheckButtonClicked(ClickEvent event) {
		listener.onCheckClicked(switchButton.isDown());
	}
	
}
