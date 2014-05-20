/**
 * 
 */
package org.cotrix.web.common.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface LabelProvider<T> {
	public String getLabel(T item);
}
