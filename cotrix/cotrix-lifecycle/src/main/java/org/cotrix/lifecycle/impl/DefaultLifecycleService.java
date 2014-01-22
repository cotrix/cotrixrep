package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Utils.*;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.LifecycleFactory;
import org.cotrix.lifecycle.LifecycleRegistry;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.LifecycleRepository.ResumptionToken;


public class DefaultLifecycleService implements LifecycleService {

	@Inject @Any
	private Event<LifecycleEvent> event;
	
	@Inject 
	private LifecycleRegistry registry;
	
	@Inject
	private LifecycleRepository repository;

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
		valid("resource identifier",id);

		LifecycleFactory factory = registry.get(name);
		
		if (factory==null)
			throw new IllegalStateException("factory "+name+" is unknown");
		
		Lifecycle lc = state==null? 
				factory.create(id):
				factory.create(id,state);
				
				
		repository.add(lc);
		
		lc.setEventProducer(event);
		
		return lc;
	}
	
	
	@Override
	public Lifecycle lifecycleOf(String id) {
		
		valid("resource identifier",id);

		ResumptionToken token = repository.lookup(id);
		
		if (token==null)
			throw new IllegalStateException("no known lifecycle for resource "+id);
		
		
		LifecycleFactory factory = registry.get(token.name);
		
		Lifecycle lc = factory.create(id,token.state);
				
		lc.setEventProducer(event);
		
		return lc;
	}
	
	@Override
	public void update(Lifecycle lc) {
		
		notNull("lifecycle",lc);
		
		repository.update(lc);
		
	}
	
}