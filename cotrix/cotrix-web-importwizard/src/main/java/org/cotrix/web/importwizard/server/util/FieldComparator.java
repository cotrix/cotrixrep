/**
 * 
 */
package org.cotrix.web.importwizard.server.util;

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
		return String.CASE_INSENSITIVE_ORDER.compare(valueProvider.getValue(item1), valueProvider.getValue(item2));
	}

}
