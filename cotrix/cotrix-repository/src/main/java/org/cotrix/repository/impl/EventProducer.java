package org.cotrix.repository.impl;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.events.Modified;
import org.cotrix.common.events.New;
import org.cotrix.common.events.Removed;

public class EventProducer {

	@Inject @New
	public Event<Object> additions;
	
	@Inject @Removed
	public Event<Object> removals;
	
	@Inject @Modified
	public Event<Object> updates;
}
