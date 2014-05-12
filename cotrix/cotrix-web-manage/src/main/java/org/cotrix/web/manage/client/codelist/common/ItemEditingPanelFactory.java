/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ItemEditingPanelFactory<T, P extends ItemEditingPanel<T>> {
	
	public P createPanel(T item);
	public P createPanelForNewItem(T item);
}
