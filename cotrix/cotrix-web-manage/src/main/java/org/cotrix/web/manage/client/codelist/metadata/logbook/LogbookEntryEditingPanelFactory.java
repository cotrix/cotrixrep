/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.logbook;

import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;
import org.cotrix.web.manage.shared.UILogbookEntry;

import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class LogbookEntryEditingPanelFactory implements ItemEditingPanelFactory<UILogbookEntry, LogbookEntryPanel> {
	
	@Override
	public LogbookEntryPanel createPanel(UILogbookEntry item) {
		LogbookEntryPanel attributeDefinitionPanel = new LogbookEntryPanel(item);
		return attributeDefinitionPanel;
	}

	@Override
	public LogbookEntryPanel createPanelForNewItem(UILogbookEntry item) {
		LogbookEntryPanel attributeDefinitionPanel = new LogbookEntryPanel(item);
		return attributeDefinitionPanel;
	}

}