package org.cotrix.application.logbook;

import static java.lang.String.*;
import static org.cotrix.domain.utils.DomainUtils.*;

public enum LogbookEvent {

	CREATED(null),
	IMPORTED("Import origin: %s"),
	PUBLISHED("Publication Target:%s",true),
	VERSIONED("%s",true),
	SEALED(null),
	LOCKED(null,true);
	
	private final String format;
	private final boolean removable;
	
	private LogbookEvent(String format) {
		this(format,false);
	}
	
	private LogbookEvent(String format,boolean deleteable) {
		this.format=format;
		this.removable = deleteable;
	}
	
	Logbook.Entry entry() {
		return new Logbook.Entry(this,currentUser().name(),removable);
	}
	
	Logbook.Entry entryWith(Object ...args) {
		return new Logbook.Entry(this,currentUser().name(),removable)
					.description(format(format,args));
	}
}
