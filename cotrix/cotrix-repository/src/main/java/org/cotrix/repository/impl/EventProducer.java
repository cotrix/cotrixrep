package org.cotrix.repository.impl;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.events.Modified;
import org.cotrix.common.events.New;
import org.cotrix.common.events.Removed;
import org.cotrix.repository.UpdateAction;

public class EventProducer {
	
	public static class UpdateActionEvent {
		
		public final String id;
		public final UpdateAction<?> action;
		
		public UpdateActionEvent(String id, UpdateAction<?> action) {
			this.id = id;
			this.action = action;
		}
	}
	
	public static UpdateActionEvent event(String id, UpdateAction<?> action) {
		return new UpdateActionEvent(id, action);
	}

	@Inject @New
	public Event<Object> additions;
	
	@Inject @Removed
	public Event<Object> removals;
	
	@Inject @Modified
	public Event<Object> updates;
}
