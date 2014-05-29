package org.cotrix.lifecycle.impl;

import javax.enterprise.event.Observes;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.events.Qualifiers.New;
import org.cotrix.domain.events.Qualifiers.Removed;
import org.cotrix.lifecycle.LifecycleService;

public class EventListener {

	void onRemove(@Observes @Removed Codelist list, LifecycleService service){
		
		service.delete(list.id());
	}
	
	void onAdd(@Observes @New Codelist list, LifecycleService service){
		
		service.start(list.id());
	}
}
