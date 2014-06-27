package org.cotrix.domain.common;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cotrix.domain.memory.IdentifiedMS;
import org.cotrix.domain.trait.Identified;

/**
 * SPI for collections of state beans.
 * 
 * @author Fabio Simeoni
 *
 * @param <S> the bean type
 */
public interface StateContainer<S> extends Iterable<S> {

	
	//state SPI, potentially virtual over a persistent store
	
	int size();
	
	void remove(String id);
	
	boolean contains(Identified.State element);
	
	boolean contains(String id);
	
	void add(S element);
	
	//not expecting large input or output => materialised collection easier than stream
	Collection<S> get(Collection<String> ids);
	
	
	
	
	//default implementation based on materialised collection
	
	class Default<S extends Identified.State> implements StateContainer<S> {
		
		private final Map<String,S> elements = new LinkedHashMap<String,S>();
		
		//used to construct new entities in memory
		public Default() {
			this(Collections.<S>emptyList());
		}
		
		//used to reconstitute entities from store
		public Default(Collection<S> elements) {
			
			notNull("elements",elements);
			
			for (S e : elements)
				add(e);
		}
		
		@Override
		public Iterator<S> iterator() {
			return elements.values().iterator();
		}
		
		@Override
		public boolean contains(String id) {
			return elements.containsKey(id);
		}
		
		@Override
		public boolean contains(Identified.State element) {
			return elements.containsValue(element);
		}
		
		@Override
		public int size() {
			return elements.size();
		}
		
		@Override
		public void add(S element) {
			elements.put(element.id(),element);
		}
		
		@Override
		public void remove(String id) {
			elements.remove(id);
		}
		
		@Override
		public Collection<S> get(Collection<String> ids) {
			
			Collection<S> matches = new ArrayList<S>();
			
			for (String id : ids)
				if (elements.containsKey(id))
						matches.add(elements.get(id));
			
			return matches;
		}
		
		protected Collection<S> elements() {
			return elements.values();
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
			if (!(obj instanceof StateContainer))
				return false;
			StateContainer other = (StateContainer) obj;
			//wrapping in a collection (e.g. list) allows us to test with soft equality
			//otherwise keys would still be taken into account
			
			if (IdentifiedMS.testmode) {
			
				if (!(collect(elements.values()).equals(collect(other))))
					return false;
				
			}
			else {
				
				if (!collectUnordered(elements.values()).equals(collectUnordered(other)))
					return false;
			}
			
			return true;
		}
		
		
	}
}
