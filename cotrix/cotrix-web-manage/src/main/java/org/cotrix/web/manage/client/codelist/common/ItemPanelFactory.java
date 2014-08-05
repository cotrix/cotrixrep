/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ItemPanelFactory<T> {
	
	public ItemPanel<T> createPanel(T item);
	public ItemPanel<T> createPanelForNewItem(T item);
}
