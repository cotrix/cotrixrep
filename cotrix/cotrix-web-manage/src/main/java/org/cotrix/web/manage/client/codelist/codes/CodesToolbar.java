/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodesToolbar {
	
	enum Action {
		ALL_COLUMN,
		ALL_NORMAL,
		TO_METADATA;
	}
	
	public interface ToolBarListener {
		public void onAction(Action action);
		public void onMarkerMenu(MarkerType marker, boolean selected);
	}
	
	public void setListener(ToolBarListener listener);

	public void setEnabled(Action action, boolean enable);

	void setState(String msg);

	void showStateLoader(boolean visible);

}
