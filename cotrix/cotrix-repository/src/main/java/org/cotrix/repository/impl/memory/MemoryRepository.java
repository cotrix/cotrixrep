package org.cotrix.repository.impl.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;

import org.cotrix.common.Utils;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.impl.StateRepository;


public abstract class MemoryRepository<S extends Identified.State> implements StateRepository<S> {

	private final Map<String,S> objects = new HashMap<String,S>();
	
	@Override
	public void add(S object) {
		
		objects.put(object.id(),object);
	}
	
	@Override
	public S lookup(String id) {
		return objects.get(id);
	}
	
	@Override
	public boolean contains(String id) {
		return objects.containsKey(id);
	}
	
	@Override
	public void remove(String id) {
		
		objects.remove(id);
			
	}
	
	@Override
	public <R> R get(Query<S,R> query) {
		
		return reveal(query).execute(this);
	}
	
	public List<S> getAll() {
		return new ArrayList<S>(objects.values());
	}
	
	@Override
	public int size() {
		return objects.size();
	}
	
	public void clear(@Observes Shutdown event) {
		objects.clear();
	}
	
	//helpers
	
	@SuppressWarnings("all")
	private <R> MQuery<?,S,R> reveal(Query<S,R> query) {
		return Utils.reveal(query,MQuery.class);
	}
}
