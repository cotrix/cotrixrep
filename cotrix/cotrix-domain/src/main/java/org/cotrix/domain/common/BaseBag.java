package org.cotrix.domain.common;

import static org.cotrix.domain.common.Delta.*;
import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Mutable;
import org.cotrix.domain.utils.IdGenerator;

/**
 * A {@link Mutable} implementation of {@link Bag}.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained object
 */
public class BaseBag<T extends DomainObject<T>> implements Bag<T> {

	private final Set<T> objects = new LinkedHashSet<T>();
	private Delta delta;

	/**
	 * Creates an instance that contain given objects.
	 * 
	 * @param objects the objects
	 */
	public BaseBag(List<? extends T> objects) {

		notNull(objects);

		// defensive copy
		for (T e : objects)
			add(e);
	}

	/**
	 * Creates an instance with given objects and a given delta status.
	 * 
	 * @param objects the objects
	 * @param status the delta status
	 */
	public BaseBag(List<? extends T> objects, Delta status) {

		this(objects);

		notNull("delta status", status);
		this.delta = status;
		

	}

	@Override
	public Delta delta() {
		return delta;
	}

	@Override
	public void setDelta(Delta status) {
		this.delta=status;
	}
	
	public BaseBag() {
		this(Collections.<T> emptyList());
	}

	@Override
	public Iterator<T> iterator() {
		return objects.iterator();
	}

	@Override
	public int size() {
		return objects.size();
	}

	public void update(Bag<T> delta) {
		
		Delta status = delta.delta();
		
		if (status!=CHANGED && status!=DELETED)
			throw new IllegalArgumentException("not a delta update");

		Map<String, T> index = indexObjects();

		if (status==DELETED) 
			objects.clear();
		else
			for (T object : delta) {
	
				
	
				String id = object.id();
	
				if (index.containsKey(id)) {
					
					switch (object.delta()) {
						case DELETED:
							objects.remove(index.remove(id));
							break;
						case CHANGED:
							index.get(id).update(object);
							break;

					} 
				}
				//add case
				else {
					
					object.setDelta(null);
					add(object);
				}
	
			}
		
			
	};

	private Map<String, T> indexObjects() {

		Map<String, T> index = new HashMap<String, T>();

		for (T object : objects)
			index.put(object.id(), object);

		return index;
	}

	@Override
	public boolean contains(QName name) {

		valid(name);

		return !get(name).isEmpty();
	}

	@Override
	public boolean contains(T object) {

		notNull(object);

		return objects.contains(object);
	}

	@Override
	public List<T> get(QName name) {

		valid(name);

		List<T> matches = new ArrayList<T>();

		for (T e : this)
			if (e.name().equals(name))
				matches.add(e);

		return matches;
	}

	public boolean add(T object) throws IllegalArgumentException {

		notNull(object);
		
		Delta elementDelta = object.delta();
		
		if (elementDelta!=null)
			switch(elementDelta) {
				case NEW:
				case CHANGED :this.delta=CHANGED;break;
			}
		
		return objects.add(object);

	}

	public List<T> remove(QName name) {

		valid(name);

		List<T> removed = new ArrayList<T>();

		Iterator<T> elements = this.objects.iterator();

		while (elements.hasNext()) {
			T e = elements.next();
			if (e.name().equals(name)) {
				elements.remove();
				removed.add(e);
			}
		}

		return removed;
	}

	public BaseBag<T> copy(IdGenerator generator) {
		List<T> copied = new ArrayList<T>();
		for (T e : objects)
			copied.add(e.copy(generator));
		BaseBag<T> copy = new BaseBag<T>(copied); 
		copy.setDelta(delta());
		return copy;
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
		BaseBag<?> other = (BaseBag<?>) obj;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

}
