package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Container;
import org.cotrix.domain.trait.Named;

public class MContainer<B extends Named.Bean> implements Container.Bean<B> {

	private final Map<String,B> elements = new LinkedHashMap<String,B>();
	

	public MContainer() {
	}
	
	public MContainer(Collection<B> elements) {
		
		notNull("elements",elements);
		
		for (B e : elements)
			add(e);
	}
	
	@Override
	public Iterator<B> iterator() {
		return elements.values().iterator();
	}
	
	@Override
	public B lookup(String id) {
		return elements.get(id);
	}
	
	@Override
	public boolean contains(String id) {
		return elements.containsKey(id);
	}
	
	@Override
	public int size() {
		return elements.size();
	}
	
	@Override
	public void add(B element) {
		elements.put(element.id(),element);
	}
	
	@Override
	public void remove(String id) {
		elements.remove(id);
	}
	
	@Override
	public Collection<B> get(Collection<String> ids) {
		
		Collection<B> matches = new ArrayList<B>();
		
		for (String id : ids)
			if (elements.containsKey(id))
					matches.add(elements.get(id));
		
		return matches;
	}
	
	protected Collection<B> elements() {
		return elements.values();
	}

	@Override
	public boolean contains(QName name) {
		
		return !get(name).isEmpty();
	
	}

	@Override
	public Collection<B> get(QName name) {
		
		Collection<B> matches = new ArrayList<B>();
		
		for (B state : elements())
			if (name.equals(state.qname()))
				matches.add(state);
	
		return matches;
	}

	@Override
	public B getFirst(QName name) {
		
		Collection<B> matches = get(name);
		
		return matches.isEmpty()? null: matches.iterator().next();
		
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
		if (!(obj instanceof Container.Bean))
			return false;
		Container.Bean other = (Container.Bean) obj;
		//wrapping in a collection (e.g. list) allows us to test with soft equality
		//otherwise keys would still be taken into account
		
		if (MIdentified.testmode) {
		
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
