package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Utils.*;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.LifecycleFactory;
import org.cotrix.lifecycle.LifecycleRegistry;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;

/**
 * Memory-based implementation of {@link LifecycleService}
 * @author Fabio Simeoni
 *
 */
@Singleton
public class MemoryLifecycleService implements LifecycleService {

	@Inject @Any
	private Event<LifecycleEvent> event;
	
	@Inject 
	private LifecycleRegistry registry;
	
	private final Map<String,Lifecycle> instances = new HashMap<String, Lifecycle>();
	
	@Inject
	public MemoryLifecycleService(LifecycleRegistry registry) {
		
		notNull("factory registry",registry);
		
		this.registry=registry;
	}
	
	@Override
	public Lifecycle start(String id) {

		return start(id, (State) null);
		
	}
	
	@Override
	public Lifecycle start(String id, State startState) {

		return start(LifecycleFactory.DEFAULT,id,startState);
		
	}
	
	@Override
	public Lifecycle start(String name, String id) {
		
		return start(name,id, null);
		
	}
	
	
	@Override
	public Lifecycle start(String name, String id,State state) {
		
		valid("lifecycle name",name);
		
		LifecycleFactory factory = registry.get(name);
		
		if (factory==null)
			throw new IllegalStateException("factory "+name+" is unknown");
		
		return start(factory,id,state);
		
	}
	

	
	//helper
	private Lifecycle start(LifecycleFactory factory, String id, State state) {
		
		valid("resource identifier",id);
		
		Lifecycle lc = state==null? 
				factory.create(id):
				factory.create(id,state);
		
		lc.setEventProducer(event);
		instances.put(lc.resourceId(),lc);
		return lc;
		
	}
	
	@Override
	public Lifecycle lifecycleOf(String id) {
		
		valid("resource identifier",id);

		Lifecycle lc = instances.get(id);
		
		if (lc==null)
			throw new IllegalStateException("no known lifecycle for resource "+id);
		
		return lc;
	}
}
