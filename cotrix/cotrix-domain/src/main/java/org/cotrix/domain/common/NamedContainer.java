package org.cotrix.domain.common;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * A container of named entities.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the container entities 
 */
public interface NamedContainer<T> extends Container<T> {
		
	
	
		//public read-only API
	
		/**
		 * Returns <code>true</code> if this container contains at least an entity with a given name. 
		 * @param name the name
		 * @return <code>true</code> if this container contains at least an entity with the given name
		 */
		boolean contains(QName name);
		
		
		/**
		 * Returns all the entities in this container with a given name.
		 * @param name the name
		 * @return the entities with the given name
		 */
		Collection<T> getAll(QName name);
		
		
		/**
		 * Returns an entity in tis container with a given name.
		 * @param name the name
		 * @return the entity
		 * @throws IllegalStateException if there are zero or more than one entity with the given name
		 */
		T lookup(QName name) throws IllegalStateException;
		
	
		
	//private logic
	
	final class Private<T extends Identified.Abstract<T,S>, S extends Identified.State & Named.State & EntityProvider<T>> extends Container.Private<T, S> implements NamedContainer<T> {
		
		public Private(Collection<S> entities) {
			
			super(entities);
			
		}

		@Override
		public boolean contains(QName name) {
			return !getAll(name).isEmpty();
		}

		@Override
		public Collection<T> getAll(QName name) {
			
			Collection<T> matches = new ArrayList<T>();
			for (S state : state())
				if (state.name().equals(name))
					matches.add(state.entity());
			System.out.println(matches);
			return matches;
		}

		@Override
		public T lookup(QName name) throws IllegalStateException {
			Collection<T> matches = getAll(name);
			if (matches.size()==1)
				return matches.iterator().next();
			else
				throw new IllegalStateException("zero or more than one element with name "+name);
		}
		
		
	}

}
