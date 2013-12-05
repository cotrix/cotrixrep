package org.cotrix.domain.common;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.EntityProvider;


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
	 * An {@link Identified.Abstract} implementation of {@link Container}.
	 * 
	 * @author Fabio Simeoni
	 *
	 * @param <T> the type of the contained objects 
	 */
	public static class Private<T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> implements Container<T> {
		
		private final Collection<S> elements;
	
		public Private(Collection<S> elements) {
			
			notNull("elements",elements);
			
			this.elements = elements;
			
		}
		
		@Override
		public Iterator<T> iterator() {
			return new IteratorAdapter<T,S>(elements.iterator());
		}

		@Override
		public int size() {
			return elements.size();
		}
		
		
		//in-memory update, this is where size will matter
		public void update(Private<T,S> changeset) {
			
			Map<String,S> index = indexElements();

			for (T entityChangeset : changeset) {
		
				String id = entityChangeset.id();

				if (index.containsKey(id))
				
					switch (entityChangeset.status()) {
							
						case DELETED:
							elements.remove(index.remove(id));
							break;
						
						case MODIFIED: //wrap and dispatch
							index.get(id).entity().update(entityChangeset);
							break;

					} 
					
				else
					elements.add(entityChangeset.state());

			}	
		}
		

		
		public Collection<S> copy() {
			
			Collection<S> copied = new ArrayList<S>();
			
			for (S element : elements)
				copied.add(element.entity().copy());
			
			return copied; 
			
		}
		
		public Collection<S> state() {
			return elements;
		}
		
		
	
		//helpers

		private Map<String,S> indexElements() {

			Map<String, S> index = new HashMap<String,S>();

			for (S object : elements)
				index.put(object.id(),object);

			return index;
		}




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((elements == null) ? 0 : elements.hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Private))
				return false;
			Private other = (Private) obj;
			if (elements == null) {
				if (other.elements != null)
					return false;
			} else if (!elements.equals(other.elements))
				return false;
			return true;
		}

		@Override
		public String toString() {
			final int maxLen = 100;
			return "Private [objects=" + (elements != null ? toString(elements, maxLen) : null) + "]";
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
		
		
	}

}
