/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import com.google.gwt.user.client.ui.UIObject;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CodelistsMenuImpl.class)
public interface CodelistsMenu {
	
	public enum MenuButton {
		
		SHOW_ALL,
		SHOW_USER,
		
		GROUP_BY_NONE,
		GROUP_BY_NAME,
		GROUP_BY_STATE,
		
		SORT_BY_NAME,
		SORT_BY_VERSION,
		SORT_BY_NAME_REVERSE,
		SORT_BY_VERSION_REVERSE,
		
		FILTER_BY_STATE_DRAFT,
		FILTER_BY_STATE_LOCKED,
		FILTER_BY_STATE_SEALED
	}
	
	public interface Listener {
		public void onButtonClicked(MenuButton button);
		public void onHide();
	}
	
	public void show(UIObject target);
	public void hide();
	
	public void setListener(Listener listener);

}
