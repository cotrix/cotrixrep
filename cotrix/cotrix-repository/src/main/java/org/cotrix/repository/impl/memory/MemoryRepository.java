package org.cotrix.repository.impl.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;

import org.cotrix.common.Utils;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Criterion;
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
		
		return reveal(query).execute();
	}
	
	public List<S> getAll() {
		return new ArrayList<S>(objects.values());
	}
	
	@Override
	public int size() {
		return objects.size();
	}
	
	
	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {

		return MCriteria.all(c1, c2);
	}

	public <T> Criterion<T> descending(final Criterion<T> c) {
		
		return MCriteria.descending(c);
	}
	
	public <R, SR extends EntityProvider<R>> Collection<R> adapt(Collection<SR> results) {
		Collection<R> adapted = new ArrayList<R>();
		for (SR result : results)
			adapted.add(result.entity());
		return adapted;
	}
	
	//helpers
	
	public void clear(@Observes Shutdown event) {
		objects.clear();
	}
	
	@SuppressWarnings("all")
	private <R> MQuery<S,R> reveal(Query<S,R> query) {
		return Utils.reveal(query,MQuery.class);
	}
}
