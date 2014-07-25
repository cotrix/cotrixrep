/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import org.cotrix.web.manage.client.codelist.common.ItemPanel;
import org.cotrix.web.manage.shared.UILogbookEntry;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookEntryPanel extends ItemPanel<UILogbookEntry> {

	public LogbookEntryPanel(UILogbookEntry entry) {
		super(new LogbookEntryEditor(entry));
	}
}
