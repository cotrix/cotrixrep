package org.cotrix.lifecycle.utils;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Typed;
import javax.enterprise.util.TypeLiteral;

import org.cotrix.lifecycle.LifecycleEvent;

/**
 * Null {@link Event} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
@Typed
public class NullEvent implements Event<LifecycleEvent> {

	@Override
	public void fire(LifecycleEvent event) {
	}

	@Override
	public Event<LifecycleEvent> select(Annotation... qualifiers) {
		return null;
	}

	@Override
	public <U extends LifecycleEvent> Event<U> select(Class<U> subtype, Annotation... qualifiers) {
		return null;
	}

	@Override
	public <U extends LifecycleEvent> Event<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
		return null;
	}

	
}
