/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.manage.client.codelist.common.form.EditingHeader;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
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
		LogbookEntryDetailsPanel view = new LogbookEntryDetailsPanel();
		LogbookEntryEditor editor = new LogbookEntryEditor(item, view);
		ItemPanel<UILogbookEntry> attributeDefinitionPanel = new ItemPanel<UILogbookEntry>(header, view, editor);
		return attributeDefinitionPanel;
	}
	
	private EditingHeader getHeader() {
		EditingHeader header = new EditingHeader(icons.logBook());
		header.setSmall();
		return header;
	}

}
