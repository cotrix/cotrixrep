package org.cotrix.domain.common;

import static org.cotrix.common.CommonUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.domain.memory.MBeanContainer;
import org.cotrix.domain.trait.BeanOf;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * An immutable collection of entities.
 * 
 * @author Fabio Simeoni
 */
public interface Container<T> extends Iterable<T> {
		
	
	int size();

	
	//id-based access 
	
	boolean contains(String id);

	T lookup(String id);	

	
	//name-based access (less memory-efficient, mostly used at test time)
	
	boolean contains(QName name);
	
	T getFirst(QName name);
	
	Collection<T> get(QName name);
	
	
	//linguistic conveniences

	boolean contains(Named name);

	T getFirst(Named name);

	Collection<T> get(Named name);
	
	
		
	//private logic
	
	class Private<E extends Identified.Private<E,B>, B extends BeanOf<E> & Named.Bean> 
								
	 							  implements Container<E> {
		
		
		private final BeanContainer<B> beans;

		@SafeVarargs
		public Private(E ... es) {

			this (new MBeanContainer<B>());
			
			for (E e : es)
				beans.add(e.bean());
		}
		
		public Private(BeanContainer<B> beans) {

			notNull("beans", beans);

			this.beans = beans;

		}
		
		public BeanContainer<B> beans() {
			return beans;
		}

		@Override
		public Iterator<E> iterator() {
			return new IteratorAdapter<E,B>(beans.iterator());
		}

		
		@Override
		public int size() {
			return beans.size();
		}

		@Override
		public boolean contains(String id) {
			
			notNull("id",id);
			
			return beans.contains(id);
		}
		
		
		@Override
		public E lookup(String id) {

			notNull("id",id);
			
			return entityOf(beans.lookup(id));
		}
		

		@Override
		public boolean contains(QName name) {
			
			notNull("name",name);
			
			return beans.contains(name); 
		}
		
		@Override
		public boolean contains(Named named) {
			
			notNull("named entity",named);
			
			return contains(named.qname());
		}

		@Override
		public Collection<E> get(QName name) {
			
			notNull("name",name);
			
			//delegate and wrap
			Collection<E> matches = new ArrayList<E>();
			
			for (B bean : beans.get(name))
				matches.add(bean.entity());
			
			return matches;
			
		}
		
		
		@Override
		public Collection<E> get(Named named) {
			return get(named.qname());
		}

		@Override
		public E getFirst(QName name) {
			
			notNull("name",name);
			
			B bean= beans.getFirst(name);
			
			return entityOf(bean);
		}
		
		@Override
		public E getFirst(Named named) {
			
			return getFirst(named.qname());
		}
		
		
		
		
		public void update(Container.Private<E,B> changeset) {

			Map<String, E> updates = new HashMap<String, E>();

			for (E change : changeset) {

				String id = change.id();

				if (beans.contains(id)) //this is in-memory, no cost.

					if (change.status() == null)
						throw new IllegalArgumentException("invalid changeset:" + change.id() + " cannot be added twice");

					else
						switch (change.status()) {

							case DELETED:
								beans.remove(id);
								break;
	
							case MODIFIED: // accumulate updates
								updates.put(change.id(), change);
								break;
	
							}

				else
					beans.add(change.bean());

			}

			// process updates
			if (!updates.isEmpty())
				
				for (B bean : beans.get(updates.keySet()))
					bean.entity().update(updates.get(bean.id()));

		}



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((beans == null) ? 0 : beans.hashCode());
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
			if (beans == null) {
				if (other.beans != null)
					return false;
			} else if (!beans.equals(other.beans))
				return false;
			return true;
		}

		@Override
		public String toString() {
			final int maxLen = 100;
			return "[" + (beans != null ? toString(maxLen) : null) + "]";
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

		
		//helper
		private E entityOf(B b) {
			return b == null? null : b.entity();
		}
	
	}

}
