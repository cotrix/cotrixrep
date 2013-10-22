package org.cotrix.domain;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Copyable;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Mutable;


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
	
	
	/**
	 * A {@link Mutable}, {@link Copyable}, {@link Identified.Abstract} implementation of {@link Container}.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <T> the type of the contained objects 
	 */
	public class Private<T extends Identified.Abstract<T>> implements Container<T>, Copyable<Container.Private<T>> {
		
		private final Set<T> objects = new LinkedHashSet<T>();
		
		/**
		 * Creates an instance that contain given entities.
		 * 
		 * @param objects the entities
		 */
		public Private(List<? extends T> objects) {
			
			notNull("elements",objects);
			
			for (T object : objects)
				add(object);
			
		}
		
		@Override
		public Iterator<T> iterator() {
			return objects.iterator();
		}

		@Override
		public int size() {
			return objects.size();
		}
		
		
		/**
		 * Returns the objects in this container.
		 * @return the objects
		 */
		public List<T> objects() {
			return new ArrayList<T>(objects);
		}

		public void update(Private<T> delta) {
			
			Map<String, T> index = indexObjects();

			for (T object : delta) {
		
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
			
			return objects.add(object);
			
		}
		
		@Override
		public Private<T> copy(IdGenerator generator) {
			
			List<T> copied = new ArrayList<T>();
			for (T object : this)
				copied.add(object.copy(generator));
			
			return new Private<T>(copied); 
			
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
			Private<?> other = (Private<?>) obj;
			if (objects == null) {
				if (other.objects != null)
					return false;
			} else if (!objects.equals(other.objects))
				return false;
			return true;
		}
		
	}

}
