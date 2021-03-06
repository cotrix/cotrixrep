package org.cotrix.repository.impl;

import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.cotrix.common.events.After;
import org.cotrix.common.events.Before;
import org.cotrix.common.events.Updated;
import org.cotrix.common.events.New;
import org.cotrix.common.events.Removed;
import org.cotrix.repository.UpdateAction;

@SuppressWarnings("serial")
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
	
	public static AnnotationLiteral<Before> before =  new AnnotationLiteral<Before>(){};
	public static AnnotationLiteral<After> after =  new AnnotationLiteral<After>(){};

	@Inject @New
	public Event<Object> additions;
	
	@Inject @Removed
	public Event<Object> removals;
	
	@Inject @Updated
	public Event<Object> updates;
}
