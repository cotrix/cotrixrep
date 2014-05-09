/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Ranges {
	
	public static <T> List<T> subList(List<T> list, Range range)
	{
		if (list.isEmpty()) return Collections.emptyList();
		int startIndex = Math.max(0, range.getStart());
		int endIndex = Math.min(startIndex+range.getLength(), list.size());
		return new ArrayList<T>(list.subList(startIndex, endIndex));
	}
	
	public static <T> List<T> subListReverseOrder(List<T> list, Range range)
	{
		if (list.isEmpty()) return Collections.emptyList();
		List<T> sublist = new ArrayList<T>(range.getLength());
		int startIndex = Math.max(0, range.getStart());
		int endIndex = Math.min(startIndex+range.getLength(), list.size());
		for (int i = startIndex; i<endIndex; i++) sublist.add(list.get(list.size()-1-i));
		return sublist;
	}
	
	public static <T> List<T> subList(List<T> list, Range range, Predicate<T> predicate)
	{
		if (predicate == ALL) return subList(list, range);
		
		return subList(list, range, predicate, false);
	}
	
	public static <T> List<T> subListReverseOrder(List<T> list, Range range, Predicate<T> predicate)
	{
		if (predicate == ALL) return subListReverseOrder(list, range);
		
		return subList(list, range, predicate, true);
	}
	
	private static <T> List<T> subList(List<T> list, Range range, Predicate<T> predicate, boolean reverse)
	{
		if (list.isEmpty() || range.getLength() == 0) return Collections.emptyList();
		
		int startIndex = Math.max(0, range.getStart());
		int endIndex = Math.min(startIndex+range.getLength(), list.size());
		
		int firstElement = reverse?list.size() : 0;
		ListIterator<T> iterator = list.listIterator(firstElement);
		
		int counter = -1;
		
		ArrayList<T> sublist = new ArrayList<>(range.getLength());
		
		while(reverse?iterator.hasPrevious():iterator.hasNext()) {
			T item = reverse?iterator.previous():iterator.next();
			if (!predicate.apply(item)) continue;
			counter++;
			if (counter<startIndex) continue;
			if (counter>=endIndex) break;
			sublist.add(item);
		}
		
		return sublist;
	}
	
	public static <T> int size(List<T> list, Predicate<T> predicate) {
		if (predicate == ALL) return list.size();
		int counter = 0;
		for (T item : list) if (predicate.apply(item)) counter++;
		return counter;
	}
	
	public interface Predicate<T> {
		public boolean apply(T item);
	}
	
	private static final Predicate<?> ALL = new Predicate<Object>() {

		@Override
		public boolean apply(Object item) {
			return true;
		}
	};
	
	@SuppressWarnings("unchecked")
	public static final <T> Predicate<T> noFilter() {
		return (Predicate<T>) ALL;
	}

}
