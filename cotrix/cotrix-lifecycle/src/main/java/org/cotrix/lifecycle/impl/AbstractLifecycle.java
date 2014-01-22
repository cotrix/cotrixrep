package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Utils.*;

import javax.enterprise.event.Event;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.utils.NullEvent;

/**
 * Partial {@link Lifecycle} implementation
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class AbstractLifecycle implements Lifecycle {

	private static final long serialVersionUID = 1L;
	
	protected final String name;
	
	final String id;

	private Event<LifecycleEvent> event = new NullEvent();
	
	/**
	 * Creates an instance with a given name and for a given resource
	 * @param name the name of the instance
	 * @param id the identifier of the associated resource
	 */
	public AbstractLifecycle(String name,String id) {
		
		valid("lifecycle name",name);
		valid("resource identifier",id);
		
		this.name=name;
		this.id=id;
	}
	
	@Override
	public void setEventProducer(Event<LifecycleEvent> producer) {
		
		notNull("producer",producer);
		
		this.event = producer;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String resourceId() {
		return id;
	}
	
	/**
	 * Returns the event producer.
	 * @return the producer
	 */
	protected Event<LifecycleEvent> eventProducer() {
		return event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractLifecycle other = (AbstractLifecycle) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



}