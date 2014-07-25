package org.cotrix.domain.common;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

/**
 * SPI for collections of named state beans.
 * 
 * @author Fabio Simeoni
 *
 * @param <S> the bean type
 */
public interface NamedStateContainer<S> extends StateContainer<S> {
		
	//state SPI, potentially virtual over a persistent store
	
	boolean contains(QName name);
	
	boolean contains(Named named);
	
	//not expecting large output => materialised collection easier than stream
	Collection<S> getAll(QName name);  
	
	
	S lookup(QName name) throws IllegalStateException;
	
	S lookup(Named named) throws IllegalStateException;
	
	
	//default implementation based on materialised collection
	//use when expecting small scale
	
	class Default<S extends Identified.State & Named.State> extends StateContainer.Default<S> implements NamedStateContainer<S> {
		
		public Default() {
			super();
		}
		
		public Default(Collection<S> elements) {
			
			super(elements);
		}

		@Override
		public boolean contains(QName name) {
			
			return !getAll(name).isEmpty();
		
		}
		
		@Override
		public boolean contains(Named named) {
			return contains(named.qname());
		}

		@Override
		public Collection<S> getAll(QName name) {
			
			Collection<S> matches = new ArrayList<S>();
			
			for (S state : elements())
				if (name.equals(state.qname()))
					matches.add(state);
		
			return matches;
		}

		@Override
		public S lookup(QName name) throws IllegalStateException {
			
			Collection<S> matches = getAll(name);
			
			if (matches.size()==1)
				return matches.iterator().next();
			else
				throw new IllegalStateException("zero or more than one element with name "+name);
		}
		
		
		@Override
		public S lookup(Named named) throws IllegalStateException {
			return lookup(named.qname());
		}
	}
}
