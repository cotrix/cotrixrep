/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}
