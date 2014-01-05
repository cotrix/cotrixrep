package org.cotrix.repository.impl.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import org.cotrix.repository.spi.StateRepository;

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
		
		return query.execute();
	}
	
	public List<S> getAll() {
		return new ArrayList<S>(objects.values());
	}
	
	@Override
	public int size() {
		return objects.size();
	}
	
	
	
	//definition shared by subclasses in their role of query factories
	
	
	//memry criterion based on comparable
	public interface MCriterion<T> extends Comparator<T>, Criterion<T> {}
	
	
	public <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {

		return new MCriterion<T>() {

			public int compare(T o1, T o2) {
				int result = reveal(c1).compare(o1, o2);

				if (result == 0)
					result = reveal(c2).compare(o1, o2);

				return result;
			};

		};
	}

	public <T> Criterion<T> descending(final Criterion<T> c) {
		
		return new MCriterion<T>() {

			public int compare(T o1, T o2) {
				return (-1)*reveal(c).compare(o1,o2);
			};

		};
	}
	
	
	public <R, SR extends EntityProvider<? extends R>> Collection<R> adapt(Collection<SR> results) {
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
	private static <R> MCriterion<R> reveal(Criterion<R> criterion) {
		return Utils.reveal(criterion, MCriterion.class);
	}
}
