/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import org.cotrix.web.manage.client.codelist.common.ItemPanel.ItemEditor;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.UILogbookEntry;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookEntryEditor implements ItemEditor<UILogbookEntry> {
	
	private LogbookEntryDetailsPanel detailsPanel;
	private UILogbookEntry entry;
	
	public LogbookEntryEditor(UILogbookEntry entry) {
		this.detailsPanel = new LogbookEntryDetailsPanel();
		this.entry = entry;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return detailsPanel.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		detailsPanel.fireEvent(event);		
	}

	@Override
	public void read() {
	}

	@Override
	public void write() {
		detailsPanel.setDescription(entry.getDescription());
		detailsPanel.setEvent(entry.getEvent().toString());
		detailsPanel.setTimestamp(entry.getTimestamp());
		detailsPanel.setUser(entry.getUser());
	}

	@Override
	public String getLabel() {
		return entry.getEvent().toString();
	}
	
	@Override
	public String getLabelValue() {
		return ": "+entry.getTimestamp();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public UILogbookEntry getItem() {
		return entry;
	}

	@Override
	public IsWidget getView() {
		return detailsPanel;
	}

	@Override
	public boolean isSwitchVisible() {
		return false;
	}

	@Override
	public void startEditing() {
	}

	@Override
	public void stopEditing() {
	}
	
	@Override
	public ImageResource getBullet() {
		return CotrixManagerResources.INSTANCE.logbookBullet();
	}
}
