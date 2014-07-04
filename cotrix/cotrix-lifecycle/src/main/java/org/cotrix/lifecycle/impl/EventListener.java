package org.cotrix.lifecycle.impl;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;

import org.cotrix.common.events.New;
import org.cotrix.common.events.Removed;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.lifecycle.LifecycleService;

@Priority(100)
public class EventListener {

	void onRemove(@Observes @Removed Codelist list, LifecycleService service){
		
		service.delete(list.id());
	}
	
	void onAdd(@Observes @New Codelist list, LifecycleService service){
		
		service.start(list.id());
	}
}
