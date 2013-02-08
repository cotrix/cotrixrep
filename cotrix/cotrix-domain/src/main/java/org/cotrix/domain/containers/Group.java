package org.cotrix.domain.containers;

import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Named;

public class Group<T extends Named & Copyable<T>> implements Copyable<Group<T>> {

	private final Map<QName,T> elements = new LinkedHashMap<QName, T>();
	
	
	public Group(List<T> elements) {
		
		notNull(elements);
		
		for (T e : elements)
			this.elements.put(e.name(),e);
	}
	
	public Group() {
		this(Collections.<T>emptyList());
	}
	
	public Iterable<T> list() {
		return elements.values();
	}
	
	public int size() {
		return elements.size();
	}

	public boolean has(QName name) throws IllegalArgumentException {
		valid(name);
		return elements.containsKey(name);
	}
	
	public boolean has(T element) throws IllegalArgumentException {
		notNull(element);
		return elements.containsValue(element);
	}
	
	public T get(QName name) throws IllegalStateException, IllegalArgumentException {
		
		valid(name);
		
		exists(name);
		
		return elements.get(name);
	}

	
	public T add(T element) {
		return elements.put(element.name(),element);
	}

	public T remove(QName name) {
		
		valid(name);
		
		exists(name);
		
		return elements.remove(name);
	}

	public Group<T> copy() {
		List<T> copied = new ArrayList<T>();
		for (T e : elements.values())
			copied.add(e.copy());
		return new Group<T>(copied);
	}
	
	
	
	//helper
	private void exists(QName name) throws IllegalStateException {
		if (!elements.containsKey(name))
			throw new IllegalStateException("unknown element "+name);
	}

	@Override
	public String toString() {
		final int maxLen = 100;
		return elements != null ? toString(elements.values(), maxLen) : null ;
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
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
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
		Group<?> other = (Group<?>) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	
	
	
	
	
	
	
	
}
