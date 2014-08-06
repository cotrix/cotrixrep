/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
import org.cotrix.web.manage.client.codelist.common.header.EditingHeader;
import org.cotrix.web.manage.shared.UILogbookEntry;

import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class LogbookEntryEditingPanelFactory implements ItemPanelFactory<UILogbookEntry> {
	
	@Override
	public LogbookEntryPanel createPanel(UILogbookEntry item) {
		EditingHeader header = getHeader();
		LogbookEntryPanel attributeDefinitionPanel = new LogbookEntryPanel(item, header);
		return attributeDefinitionPanel;
	}

	@Override
	public LogbookEntryPanel createPanelForNewItem(UILogbookEntry item) {
		EditingHeader header = getHeader();
		LogbookEntryPanel attributeDefinitionPanel = new LogbookEntryPanel(item, header);
		return attributeDefinitionPanel;
	}
	
	
	private EditingHeader getHeader() {
		EditingHeader header = new EditingHeader(icons.logBook());
		return header;
	}

}
