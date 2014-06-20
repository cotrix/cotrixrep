package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Utils.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Lifecycle> lifecyclesOf(Collection<String> ids) {
		
		notNull("resource identifiers",ids);
		
		Map<String,Lifecycle> lifecycles = new HashMap<String, Lifecycle>();
		
		if (ids.isEmpty())
			return lifecycles;
			
		for (Map.Entry<String,ResumptionToken> tokenEntry : repository.lookup(ids).entrySet()) {
			
			ResumptionToken token = tokenEntry.getValue();
			
			LifecycleFactory factory = registry.get(token.name);
			
			Lifecycle lc = factory.create(tokenEntry.getKey(),token.state);
					
			lc.setEventProducer(event);
			
			lifecycles.put(lc.resourceId(), lc);
		}
		
		return lifecycles;
	}
	
	@Override
	public void update(Lifecycle lc) {
		
		notNull("lifecycle",lc);
		
		repository.update(lc);
		
	}
	
	@Override
	public void delete(String id) {
		
		notNull("resource identifier",id);
		
		repository.delete(id);
	}
	
}
