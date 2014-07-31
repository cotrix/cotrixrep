/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.menu;

import org.cotrix.web.common.client.widgets.menu.AbstractMenuItem;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyle;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkersResource;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerMenuItem extends AbstractMenuItem {
	

	static interface MarkerMenuItemTemplate extends SafeHtmlTemplates {

		@Template("<div style=\"height:18px\"><div class=\"{0}\" style=\"{1}\"></div>{2}</div>")
		SafeHtml menuItem(String buttonStyle, SafeStyles markerStyle, String label);
	}
	
	static MarkerMenuItemTemplate TEMPLATE = GWT.create(MarkerMenuItemTemplate.class);
	
	private HandlerManager handlerManager;
	
	private String label;
	private MarkerStyle markerStyle;
	private boolean selected;
	private boolean manageSelection;
	private MarkerType value;
	private String selectedItemStyleName;
	
	public MarkerMenuItem(String label, MarkerType value, MarkerStyle markerStyle) {
		this.label = label;
		this.markerStyle = markerStyle;		
		this.value = value;
		
		setScheduledCommand( new ScheduledCommand() {
			
			@Override
			public void execute() {
				fireSelectionChange();
			}
		});
		
		handlerManager = new HandlerManager(this);
		
		selected = false;
		manageSelection = false;
		
		setSelected(false);
	}

	public MarkerType getValue() {
		return value;
	}

	public void setSelectedItemStyleName(String selectedItemStyleName) {
		this.selectedItemStyleName = selectedItemStyleName;
	}

	private void fireSelectionChange() {
		if (manageSelection) setSelected(!selected);
		SelectionChangeEvent.fire(this);
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		SafeHtml html = TEMPLATE.menuItem(selected?MarkersResource.INSTANCE.menuItemSelected():MarkersResource.INSTANCE.menuItem(),
				SafeStylesUtils.forTrustedBackgroundColor(markerStyle.getBackgroundColor()), label);
		setHTML(html);
	}
	
	public void setManageSelection(boolean manageSelection) {
		this.manageSelection = manageSelection;
	}
	
	public boolean isSelected() {
		return selected;
	}

	protected void setSelectionStyle(boolean selected) {
		if (selectedItemStyleName!=null) setStyleName(selectedItemStyleName, selected);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addSelectionChangeHandler(Handler handler) {
		return handlerManager.addHandler(SelectionChangeEvent.getType(), handler);
	}

}
