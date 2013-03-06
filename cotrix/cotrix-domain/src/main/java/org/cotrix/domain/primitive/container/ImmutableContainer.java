package org.cotrix.domain.primitive.container;

import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Copyable;

/**
 * An {@link Copyable} {@link Container} implementation. 
 * 
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained object
 * 
 */
public class ImmutableContainer<T extends Copyable<T>> extends AbstractContainer<T> implements Container<T>, Copyable<Container<T>> {

	private final Set<T> objects = new LinkedHashSet<T>();
	
	/**
	 * Creates an instance that contain given objects.
	 * 
	 * @param objects the objects
	 */
	public ImmutableContainer(List<T> objects) {

		notNull(objects);

		for (T object : objects) {

			notNull(object);
			
			objects.add(object);
		}
	}

	public Iterator<T> iterator() {
		return objects.iterator();
	}

	@Override
	public int size() {
		return objects.size();
	}

	protected boolean add(T object) throws IllegalArgumentException {

		notNull(object);
		
		return objects.add(object);
		
	}
	
	protected boolean remove(T object) throws IllegalArgumentException {

		notNull(object);
		
		return objects.remove(object);
		
	}

	@Override
	public ImmutableContainer<T> copy(IdGenerator generator) {
		
		List<T> copied = new ArrayList<T>();
		for (T object : this)
			copied.add(object.copy(generator));
		
		return new ImmutableContainer<T>(copied); 
		
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
		ImmutableContainer<?> other = (ImmutableContainer<?>) obj;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

}
