package org.cotrix.application.logbook;

import static java.lang.String.*;
import static org.cotrix.domain.utils.Utils.*;

public enum LogbookEvent {

	CREATED(null),
	IMPORTED("Import origin: %s"),
	PUBLISHED("Publication Target:%s"),
	VERSIONED("%s"),
	SEALED(null),
	LOCKED(null);
	
	private final String format;
	
	private LogbookEvent(String format) {
		this.format=format;
	}
	
	Logbook.Entry entry() {
		return new Logbook.Entry(this,currentUser());
	}
	
	Logbook.Entry entryWith(Object ...args) {
		return new Logbook.Entry(this,currentUser())
					.description(format(format,args));
	}
}
