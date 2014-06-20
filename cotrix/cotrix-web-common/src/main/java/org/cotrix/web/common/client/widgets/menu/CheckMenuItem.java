/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CheckMenuItem extends MenuItem implements HasSelectionChangedHandlers {
	

	static interface RadioMenuItemTemplate extends SafeHtmlTemplates {

		@Template("<div style=\"height:18px\"><img src=\"{1}\" class=\"{2}\" style=\"vertical-align:middle;padding-right:5px;\"/>{0}</div>")
		SafeHtml checked(SafeHtml label, SafeUri img, String imgStyle);
		
		@Template("<div style=\"height:18px;padding-left:14px;\">{0}</div>")
		SafeHtml unchecked(SafeHtml label);
	}
	
	static RadioMenuItemTemplate template = GWT.create(RadioMenuItemTemplate.class);
	
	private HandlerManager handlerManager;
	
	private SafeHtml label;
	private ImageResource image;
	private boolean selected;
	private boolean manageSelection;
	private String value;
	private String selectedItemStyleName;
	
	@UiConstructor
	public CheckMenuItem(String label, ImageResource image, String value) {
		super(SafeHtmlUtils.EMPTY_SAFE_HTML);
		this.label = SafeHtmlUtils.fromString(label);
		this.image = image;		
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
	
	public void setSelectedItemStyleName(String selectedItemStyleName) {
		this.selectedItemStyleName = selectedItemStyleName;
	}

	public String getValue() {
		return value;
	}

	private void fireSelectionChange() {
		if (manageSelection) setSelected(!selected);
		SelectionChangeEvent.fire(this);
	}
	
	public void setImage(ImageResource resource) {
		this.image = resource;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		SafeHtml html = selected?template.checked(label, image.getSafeUri(), ""):template.unchecked(label);
		setHTML(html);
	}
	
	public void setManageSelection(boolean manageSelection) {
		this.manageSelection = manageSelection;
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
