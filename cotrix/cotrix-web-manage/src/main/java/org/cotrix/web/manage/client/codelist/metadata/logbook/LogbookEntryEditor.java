/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import org.cotrix.web.manage.client.codelist.common.form.ItemPanel.ItemEditor;
import org.cotrix.web.manage.shared.UILogbookEntry;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookEntryEditor implements ItemEditor<UILogbookEntry> {
	
	private LogbookEntryDetailsPanel detailsPanel;
	private UILogbookEntry entry;
	
	public LogbookEntryEditor(UILogbookEntry entry, LogbookEntryDetailsPanel detailsPanel) {
		this.detailsPanel = detailsPanel;
		this.entry = entry;
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
	public String getHeaderTitle() {
		return entry.getTimestamp();
	}
	
	@Override
	public String getHeaderSubtitle() {
		return entry.getEvent().toString();
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
	public void onStartEditing() {
	}

	@Override
	public void onStopEditing() {
	}

	@Override
	public void onEdit(AsyncCallback<Boolean> callBack) {
		callBack.onSuccess(true);
	}

	@Override
	public void onSave() {
	}
}
