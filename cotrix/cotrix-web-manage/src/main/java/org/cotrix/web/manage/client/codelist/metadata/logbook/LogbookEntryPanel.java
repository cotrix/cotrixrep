/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelHeader;
import org.cotrix.web.manage.shared.UILogbookEntry;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookEntryPanel extends ItemPanel<UILogbookEntry> {

	public LogbookEntryPanel(UILogbookEntry entry, ItemPanelHeader header) {
		super(new LogbookEntryEditor(entry), header);
	}
}
