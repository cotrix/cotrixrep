/**
 * 
 */
package org.cotrix.web.common.server.util;

import java.util.Comparator;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FieldComparator<T> implements Comparator<T> {
	
	public interface ValueProvider<T> {
		public String getValue(T item);
	}
	
	protected ValueProvider<T> valueProvider;

	/**
	 * @param valueProvider
	 */
	public FieldComparator(ValueProvider<T> valueProvider) {
		this.valueProvider = valueProvider;
	}

	@Override
	public int compare(T item1, T item2) {
		String value1 = valueProvider.getValue(item1);
		String value2 = valueProvider.getValue(item2);
		if (value1 == null) return -1;
		if (value2 == null) return 1;
		return String.CASE_INSENSITIVE_ORDER.compare(value1, value2);
	}

}
