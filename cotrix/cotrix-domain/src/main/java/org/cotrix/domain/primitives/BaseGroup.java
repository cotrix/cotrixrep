package org.cotrix.domain.primitives;

import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.traits.Change;
import org.cotrix.domain.traits.Mutable;
import org.cotrix.domain.utils.IdGenerator;

/**
 * A {@link Mutable} implementation of {@link Group}.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained objects
 */
public class BaseGroup<T extends DomainObject<T>> extends BaseContainer<T,Group<T>> implements Group<T> {

	private final Map<QName, T> objects = new LinkedHashMap<QName, T>();

	/**
	 * Creates an instance that contain given objects.
	 * 
	 * @param objects the objects
	 */
	public BaseGroup(List<? extends T> objects) {

		notNull(objects);

		for (T object : objects)
			add(object);
	}

	/**
	 * Creates an instance with given objects and a given delta status.
	 * 
	 * @param objects the objects
	 * @param change the delta status
	 * @throw IllegalArgumentException if two or more of the given objects have the same name
	 */
	public BaseGroup(List<? extends T> objects, Change change) throws IllegalArgumentException {

		this(objects);

		notNull("delta status", change);
		setChange(change);
	}
	
	@Override
	public Iterator<T> iterator() {
		return objects.values().iterator();
	}
	
	@Override
	public int size() {
		return objects.size();
	}

	@Override
	public void update(Group<T> delta) throws IllegalArgumentException, IllegalStateException {

		Map<String, T> index = indexObjects();

		System.out.println(index);
		for (T object : delta) {
	
			String id = object.id();

			if (index.containsKey(id)) {
				
				switch (object.change()) {
					case DELETED:
						remove(index.remove(id).name());
						break;
					case MODIFIED:
						index.get(id).update(object);
						break;
				} 
			}
			//add case
			else {
				object.reset();
				add(object);
			}

		}

	}

	private Map<String, T> indexObjects() {

		Map<String, T> index = new HashMap<String, T>();

		for (T object : objects.values())
			index.put(object.id(), object);

		return index;
	}

	@Override
	public boolean contains(QName name) throws IllegalArgumentException {
		valid(name);
		return objects.containsKey(name);
	}

	@Override
	public boolean contains(T element) throws IllegalArgumentException {
		notNull(element);
		return objects.containsValue(element);
	}

	@Override
	public T get(QName name) throws IllegalStateException, IllegalArgumentException {

		valid(name);

		exists(name);

		return objects.get(name);
	}

	public boolean add(T e) {

		notNull(e);

		propagateChangeFrom(e);
		
		T added = this.objects.put(e.name(), e); 
		
		return added == null;
	}

	public T remove(QName name) {

		valid(name);

		exists(name);

		return objects.remove(name);
	}

	public BaseGroup<T> copy(IdGenerator generator) {
		List<T> copied = new ArrayList<T>();
		for (T e : objects.values())
			copied.add(e.copy(generator));
		return new BaseGroup<T>(copied);
		
		//we do not copy change status!
	}

	// helper
	private void exists(QName name) throws IllegalStateException {
		if (!objects.containsKey(name))
			throw new IllegalStateException("unknown element " + name);
	}

	@Override
	public String toString() {
		final int maxLen = 100;
		return objects != null ? toString(objects.values(), maxLen) : null;
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
		BaseGroup<?> other = (BaseGroup<?>) obj;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

}
