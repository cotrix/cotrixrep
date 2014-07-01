package org.cotrix.domain.common;

import static org.cotrix.common.CommonUtils.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * An immutable and typed collection of named domain entities.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of entities
 */
public interface NamedContainer<T> extends Container<T> {
		
	
		//public read-only API
	
		//(no implication entities are all in memory)
		
		/**
		 * Returns <code>true</code> if this container contains at least an entity with a given name. 
		 * @param name the name
		 * @return <code>true</code> if this container contains at least an entity with the given name
		 */
		boolean contains(QName name);
		
		
		/**
		 * Returns <code>true</code> if this container contains at least an entity with a given name. 
		 * @param name the name
		 * @return <code>true</code> if this container contains at least an entity with the given name
		 */
		boolean contains(Named name);
		
		
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
		
		
		/**
		 * Returns an entity in tis container with a given name.
		 * @param name the name
		 * @return the entity
		 * @throws IllegalStateException if there are zero or more than one entity with the given name
		 */
		T lookup(Named name) throws IllegalStateException;
		
	
		
	//private logic
	
	final class Private<T extends Identified.Abstract<T,S>, 
						S extends Identified.State & Named.State & EntityProvider<T>> 
						extends Container.Abstract<T, S,NamedStateContainer<S>> 
						implements NamedContainer<T> {
		
		
		public Private(NamedStateContainer<S> state) {
			super(state);
		}

		@Override
		public boolean contains(QName name) {
			
			notNull("name",name);
			
			return state().contains(name); 
		}
		
		@Override
		public boolean contains(Named named) {
			
			notNull("named entity",named);
			
			return state().contains(named);
		}

		@Override
		public Collection<T> getAll(QName name) {
			
			notNull("name",name);
			
			//delegate and wrap
			Collection<T> matches = new ArrayList<T>();
			
			for (S state : state().getAll(name))
				matches.add(state.entity());
			
			return matches;
			
		}

		@Override
		public T lookup(QName name) throws IllegalStateException {
			
			notNull("name",name);
			
			S match = state().lookup(name);
			
			return match==null?null:match.entity();
		}
		
		@Override
		public T lookup(Named named) throws IllegalStateException {
			return lookup(named.qname());
		}
	}

}
