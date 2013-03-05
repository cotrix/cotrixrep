package org.cotrix.domain.primitive.container;

import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cotrix.domain.primitive.entity.Entity;
import org.cotrix.domain.trait.Copyable;
import org.cotrix.domain.trait.Mutable;
import org.cotrix.domain.utils.IdGenerator;

/**
 * A {@link Mutable} and {@link Copyable} {@link Container} of {@link Entity}s.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained object
 * @param <C> the type of the container itself
 * 
 */
public class Bag<T extends Entity<T>> extends AbstractContainer<T> {

	private final Set<T> objects = new LinkedHashSet<T>();
	
	/**
	 * Creates an instance that contain given objects.
	 * 
	 * @param objects the objects
	 */
	public Bag(List<? extends T> objects) {

		notNull(objects);

		for (T object : objects)
			add(object);
	}

	public Iterator<T> iterator() {
		return objects.iterator();
	}

	@Override
	public int size() {
		return objects.size();
	}

	@Override
	public void update(Container<T> delta) {
		
		Map<String, T> index = indexObjects();

		for (T object : delta) {
	
			String id = object.id();

			if (index.containsKey(id)) {
				
				switch (object.change()) {
					case DELETED:
						objects.remove(index.remove(id));
						break;
					case MODIFIED:
						index.get(id).update(object);
						break;

				} 
			}
			//add case
			else {
				object.reset();
				add(object);
			}

		}	
	}

	private Map<String, T> indexObjects() {

		Map<String, T> index = new HashMap<String, T>();

		for (T object : objects)
			index.put(object.id(), object);

		return index;
	}

	protected boolean add(T object) throws IllegalArgumentException {

		notNull(object);
		
		propagateChangeFrom(object);
		
		return objects.add(object);
		
	}

	@Override
	public Bag<T> copy(IdGenerator generator) {
		
		List<T> copied = new ArrayList<T>();
		for (T object : this)
			copied.add(object.copy(generator));
		
		return new Bag<T>(copied); 
		
	}
	
	@Override
	public String toString() {
		final int maxLen = 100;
		return objects != null ? toString(objects, maxLen) : null;
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objects == null) ? 0 : objects.hashCode());
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
		Bag<?> other = (Bag<?>) obj;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

}
