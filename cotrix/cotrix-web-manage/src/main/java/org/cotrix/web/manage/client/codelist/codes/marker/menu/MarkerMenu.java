/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.menu;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;

import com.google.gwt.user.client.ui.UIObject;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(MarkerMenuImpl.class)
public interface MarkerMenu {
	
	public interface Listener {
		public void onButtonClicked(MarkerType marker, boolean selected);
		public void onHide();
	}
	
	public void show(UIObject target);
	public void hide();
	
	public void setListener(Listener listener);

}
