/**
 * 
 */
package org.cotrix.web.share.server.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Ranges {
	
	public static <T> List<T> subList(List<T> list, Range range)
	{
		int startIndex = Math.max(0, range.getStart());
		int endIndex = Math.min(startIndex+range.getLength(), list.size());
		return new ArrayList<T>(list.subList(startIndex, endIndex));
	}
	
	public static <T> List<T> subListReverseOrder(List<T> list, Range range)
	{
		List<T> sublist = new ArrayList<T>(range.getLength());
		int startIndex = Math.max(0, (list.size() - 1) - range.getStart());
		int endIndex = Math.max(startIndex+range.getLength(), 0);
		for (int i = startIndex; i>=0 && i<endIndex; i--) sublist.add(list.get(i));
		return sublist;
	}

}
