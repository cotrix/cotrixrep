package org.cotrix.domain.common;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cotrix.domain.trait.Identified;


/**
 * An immutable, typed container of domain objects.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the contained objects 
 */
public interface Container<T> extends Iterable<T> {

	/**
	 * Returns the number of objects in this container.
	 * @return the number of objects
	 */
	int size();
	
	
	static interface Provider<T,S> {
		
		S stateOf(T s);
		
		T objectFor(S s);
	}
	
	/**
	 * An {@link Identified.Abstract} implementation of {@link Container}.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <T> the type of the contained objects 
	 */
	public class Private<T extends Identified.Abstract<T>, S extends Identified.State<T>> implements Container<T> {
		
		private final Collection<S> objects;
		private final Provider<T,S> provider;
		
		/**
		 * Creates an instance that contain given entities.
		 * 
		 * @param objects the entities
		 */
		public Private(Collection<S> objects,Provider<T,S> provider) {
			
			notNull("elements",objects);
			
			this.objects=objects;
			this.provider=provider;
			
		}
		
		@Override
		public Iterator<T> iterator() {
			return new ElementIterator();
		}

		@Override
		public int size() {
			return objects.size();
		}
		
		public void update(Private<T,S> changeset) {
			
			Map<String, T> index = indexObjects();

			for (T object : changeset) {
		
				String id = object.id();

				if (index.containsKey(id)) {
				
					if (object.isChangeset())
						switch (object.status()) {
							case DELETED:
								objects.remove(index.remove(id));
								break;
							case MODIFIED:
								index.get(id).update(object);
								break;
	
						} 
				}
				//add case
				else
					add(object);

			}	
		}

		private Map<String, T> indexObjects() {

			Map<String, T> index = new HashMap<String, T>();

			for (T object : this)
				index.put(object.id(), object);

			return index;
		}

		protected boolean add(T object) throws IllegalArgumentException {

			notNull("element",object);
			
			return objects.add(provider.stateOf(object));
			
		}
		
		public Private<T,S> copy() {
			
			return copy(true); 
			
		}

		public Private<T,S> copy(boolean retainId) {
			
			List<S> copied = new ArrayList<S>();
			for (T object : this)
				copied.add(provider.stateOf(object.copy(retainId)));
			
			return new Private<T,S>(copied, provider); 
			
		}
		
		public Collection<S> objects() {
			return objects;
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
			Private<?,?> other = (Private<?,?>) obj;
			if (objects == null) {
				if (other.objects != null)
					return false;
			} else if (!objects.equals(other.objects))
				return false;
			return true;
		}
	
		
		class ElementIterator implements Iterator<T> {
			
			Iterator<S> inner;
			
			public ElementIterator() {
				this.inner=objects.iterator();
			}
			
			
			public boolean hasNext() {
				return inner.hasNext();
			}
			
			public T next() {
				return provider.objectFor(inner.next());
			}
			
			public void remove() {
				inner.remove();
			}
		}
	}

}
