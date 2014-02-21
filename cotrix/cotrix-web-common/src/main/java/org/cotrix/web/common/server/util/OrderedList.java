/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.common.server.util.FieldComparator.ValueProvider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class OrderedList<T> {
	
	protected Map<String, FieldComparator<T>> comparators = new HashMap<String, FieldComparator<T>>();
	protected Map<String, List<T>> orderedLists = new HashMap<String, List<T>>();
	protected int size;
	
	public void addField(String fieldName, ValueProvider<T> valueProvider)
	{
		if (size>0) throw new IllegalStateException("You can't add more fields after the first insertion");
		FieldComparator<T> comparator = new FieldComparator<T>(valueProvider);
		comparators.put(fieldName, comparator);
		orderedLists.put(fieldName, new ArrayList<T>());
	}
	
	public void add(T item)
	{
		for (List<T> list:orderedLists.values()) list.add(item);
		size++;
	}
	
	public void reset()
	{
		clear();
		comparators.clear();
		orderedLists.clear();
	}
	
	public void clear()
	{
		for (List<T> list:orderedLists.values()) list.clear();
		size = 0;
	}
	
	public void sort()
	{
		for (String field:orderedLists.keySet()) {
			FieldComparator<T> comparator = comparators.get(field);
			List<T> list = orderedLists.get(field);
			Collections.sort(list, comparator);
		}
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

}
