package org.cotrix.domain.primitive.container;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.cotrix.domain.trait.Copyable;

/**
 * A partial {@link Container} implementation. 
 * 
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained object
 * 
 */
public abstract class AbstractContainer<T> implements Container<T>, Copyable<Container<T>> {

	private final Set<T> objects = new LinkedHashSet<T>();
	
	public Iterator<T> iterator() {
		return objects.iterator();
	}

	@Override
	public int size() {
		return objects.size();
	}

	protected Set<T> objects() {

		return objects;
		
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
		AbstractContainer<?> other = (AbstractContainer<?>) obj;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

}
