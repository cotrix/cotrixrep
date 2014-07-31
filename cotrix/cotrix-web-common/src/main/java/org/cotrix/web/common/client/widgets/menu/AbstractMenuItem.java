/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractMenuItem extends MenuItem implements HasSelectionChangedHandlers {

	public AbstractMenuItem() {
		super(SafeHtmlUtils.EMPTY_SAFE_HTML);
	}

	public abstract void setSelected(boolean selected);

	public abstract void setManageSelection(boolean manage);

}
