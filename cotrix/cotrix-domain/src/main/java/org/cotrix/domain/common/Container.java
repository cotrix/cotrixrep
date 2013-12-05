package org.cotrix.domain.common;

import static org.cotrix.common.Utils.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;


/**
 * An immutable, typed container of domain entities.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the container entities 
 */
public interface Container<T> extends Iterable<T> {

	//public read-only interface
	
	/**
	 * Returns the number of entities in this container.
	 * @return the number of entities
	 */
	int size();
	
	/**
	 * Returns <code>true</code> if this container contains a given entity. 
	 * @param entity the entity
	 * @return <code>true</code> if this container contains a given entity
	 */
	 boolean contains(Object entity);

	
	
	//private logic
	
	public static class Private<T extends Identified.Abstract<T,S>, S extends Identified.State & EntityProvider<T>> implements Container<T> {
		
		private final Collection<S> entities;
	
		public Private(Collection<S> entities) {
			
			notNull("entities",entities);
			
			this.entities = entities;
			
		}
		
		@Override
		public Iterator<T> iterator() {
			return new IteratorAdapter<T,S>(entities.iterator());
		}
		
		public boolean contains(Object entity) {
			for (T e : this)
				if (e.equals(entity))
					return true;
			
			return false;
		};

		@Override
		public int size() {
			return entities.size();
		}
		
		
		//in-memory update, this is where size will matter
		public void update(Private<T,S> changeset) {
			
			Map<String,S> index = indexElements();

			for (T entityChangeset : changeset) {
		
				String id = entityChangeset.id();

				if (index.containsKey(id))
				
					switch (entityChangeset.status()) {
							
						case DELETED:
							entities.remove(index.remove(id));
							break;
						
						case MODIFIED: //wrap and dispatch
							index.get(id).entity().update(entityChangeset);
							break;

					} 
					
				else
					entities.add(entityChangeset.state());

			}	
		}
		
		public Collection<S> state() {
			return entities;
		}
		
		
	
		//helpers

		private Map<String,S> indexElements() {

			Map<String, S> index = new HashMap<String,S>();

			for (S object : entities)
				index.put(object.id(),object);

			return index;
		}




		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((entities == null) ? 0 : entities.hashCode());
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
			if (entities == null) {
				if (other.entities != null)
					return false;
			} else if (!entities.equals(other.entities))
				return false;
			return true;
		}

		@Override
		public String toString() {
			final int maxLen = 100;
			return "[" + (entities != null ? toString(maxLen) : null) + "]";
		}

		private String toString(int maxLen) {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			int i = 0;
			for (Iterator<?> iterator = this.iterator(); iterator.hasNext() && i < maxLen; i++) {
				if (i > 0)
					builder.append(", ");
				builder.append(iterator.next());
			}
			builder.append("]");
			return builder.toString();
		}
		
		
	}

}
