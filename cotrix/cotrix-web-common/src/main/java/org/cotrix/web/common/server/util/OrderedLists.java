/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class OrderedLists<T> {
	
	public interface ValueProvider<E> {
		public String getValue(E item);
	}
	
	protected Map<String, List<T>> orderedLists = new HashMap<String, List<T>>();
	protected int size;
	
	public void addField(String fieldName, ValueProvider<T> valueProvider)
	{
		if (size>0) throw new IllegalStateException("You can't add more fields after the first insertion");
		FieldComparator<T> comparator = new FieldComparator<T>(valueProvider);
		orderedLists.put(fieldName, new SortedList<T>(comparator));
	}
	
	public void add(T item)
	{
		for (List<T> list:orderedLists.values()) list.add(item);
		size++;
	}
	
	public void reset()
	{
		clear();
		orderedLists.clear();
	}
	
	public void clear()
	{
		for (List<T> list:orderedLists.values()) list.clear();
		size = 0;
	}
	
	public int size()
	{
		return size;
	}
	
	public List<T> getSortedList(String field)
	{
		if (!orderedLists.containsKey(field)) throw new IllegalArgumentException("Unknow field "+field);
		return orderedLists.get(field);
	}
	
	static class FieldComparator<E> implements Comparator<E> {
		

		
		protected ValueProvider<E> valueProvider;

		/**
		 * @param valueProvider
		 */
		public FieldComparator(ValueProvider<E> valueProvider) {
			this.valueProvider = valueProvider;
		}

		@Override
		public int compare(E item1, E item2) {
			String value1 = valueProvider.getValue(item1);
			String value2 = valueProvider.getValue(item2);
			if (value1 == null) return -1;
			if (value2 == null) return 1;
			return String.CASE_INSENSITIVE_ORDER.compare(value1, value2);
		}

	}

}
