/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
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
	public ItemPanel<UILogbookEntry> createPanel(UILogbookEntry item) {
		EditingHeader header = getHeader();
		LogbookEntryEditor editor = new LogbookEntryEditor(item);
		ItemPanel<UILogbookEntry> attributeDefinitionPanel = new ItemPanel<UILogbookEntry>(editor, header);
		return attributeDefinitionPanel;
	}

	@Override
	public ItemPanel<UILogbookEntry> createPanelForNewItem(UILogbookEntry item) {
		return createPanel(item);
	}
	
	private EditingHeader getHeader() {
		EditingHeader header = new EditingHeader(icons.logBook());
		return header;
	}

}
