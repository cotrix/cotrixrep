package org.cotrix.repository.impl;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.domain.events.Qualifiers.Modified;
import org.cotrix.domain.events.Qualifiers.New;
import org.cotrix.domain.events.Qualifiers.Removed;

public class EventProducer {

	@Inject @New
	public Event<Object> additions;
	
	@Inject @Removed
	public Event<Object> removals;
	
	@Inject @Modified
	public Event<Object> updates;
}
