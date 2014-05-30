/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes;

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
	}
	
	public void setListener(ToolBarListener listener);

	public void setEnabled(Action action, boolean enable);

	void setState(String msg);

	void showStateLoader(boolean visible);

}
