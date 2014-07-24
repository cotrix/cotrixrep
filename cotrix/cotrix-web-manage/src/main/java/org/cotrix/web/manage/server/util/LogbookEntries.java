/**
 * 
 */
package org.cotrix.web.manage.server.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.application.logbook.Logbook;
import org.cotrix.application.logbook.LogbookEvent;
import org.cotrix.web.manage.shared.UILogbookEntry;
import org.cotrix.web.manage.shared.UILogbookEntry.UILogbookEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LogbookEntries {
	
	public static List<UILogbookEntry> toUILogbookEntries(List<Logbook.Entry> entries) {
		List<UILogbookEntry> uiEntries = new ArrayList<>();
		for (Logbook.Entry entry:entries) uiEntries.add(toUILogbookEntry(entry));
		return uiEntries;
	}
	
	public static UILogbookEntry toUILogbookEntry(Logbook.Entry entry) {
		return new UILogbookEntry(
				entry.id(), 
				entry.timestamp(), 
				toUILogbookEvent(entry.event()), 
				entry.description(), 
				entry.user(), 
				entry.isRemovable());
	}

	
	private static UILogbookEvent toUILogbookEvent(LogbookEvent event) {
		switch (event) {
			case CREATED: return UILogbookEvent.CREATED;
			case IMPORTED: return UILogbookEvent.IMPORTED;
			case LOCKED: return UILogbookEvent.LOCKED;
			case PUBLISHED: return UILogbookEvent.PUBLISHED;
			case SEALED: return UILogbookEvent.SEALED;
			case VERSIONED: return UILogbookEvent.VERSIONED;
			default: throw new IllegalArgumentException("Unknown event "+event);
		}
	}
}
