package org.cotrix.domain.common;

import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Named;

public class Bag<T extends Named & Copyable<T>> implements Copyable<Bag<T>> {

	private final Set<T> elements = new LinkedHashSet<T>();
	
	
	public Bag(List<T> elements) {
		
		notNull(elements);
		
		for (T e : elements)
			this.elements.add(e.copy());
	}
	
	public Bag() {
		this(Collections.<T>emptyList());
	}
	
	public Iterable<T> list() {
		return elements;
	}
	
	public int size() {
		return elements.size();
	}

	public boolean has(QName name) throws IllegalArgumentException {
		
		valid(name);

		return !get(name).isEmpty();
	}
	
	public boolean has(T element) throws IllegalArgumentException {
		
		notNull(element);
		
		return elements.contains(element);
	}
	
	public List<T> get(QName name) throws IllegalArgumentException {
		
		valid(name);
		
		List<T> matches = new ArrayList<T>();
		
		for (T e : list())
			if (e.name().equals(name))
				matches.add(e);

		return matches;
	}

	
	public boolean add(T element) throws IllegalArgumentException {
		
		notNull(element);
		
		return elements.add(element);
		
	}

	public List<T> remove(QName name) {
		
		valid(name);
		
		List<T> removed = new ArrayList<T>();
		
		Iterator<T> elements = this.elements.iterator();
		
		while (elements.hasNext()) {
			T e = elements.next();
			if (e.name().equals(name))
				elements.remove();
				removed.add(e);
		}
		
		return removed;
	}

	public Bag<T> copy() {
		List<T> copied = new ArrayList<T>();
		for (T e : elements)
			copied.add(e.copy());
		return new Bag<T>(copied);
	}

	@Override
	public String toString() {
		final int maxLen = 100;
		return elements != null ? toString(elements, maxLen) : null ;
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
		Bag<?> other = (Bag<?>) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	
	
	
	
	
	
	
	
}
