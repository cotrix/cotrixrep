/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import com.google.gwt.uibinder.client.UiChild;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CheckMenuGroup extends AbstractMenuGroup {
	
	@UiChild(tagname="check")
	public void add(final CheckMenuItem item) {
		item.setManageSelection(true);
		addItem(item);
	}
}
