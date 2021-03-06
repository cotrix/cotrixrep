/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.form;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ItemPanelFactory<T> {
	
	public ItemPanel<T> createPanel(T item);
}
