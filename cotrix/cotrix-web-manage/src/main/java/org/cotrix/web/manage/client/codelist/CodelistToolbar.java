/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CodelistToolbar {
	
	enum Action {
		LOCK,
		UNLOCK,
		ALL_COLUMN,
		ALL_NORMAL,
		FINALIZE;
	}
	
	public interface ToolBarListener {
		public void onAction(Action action);
	}
	
	public void setListener(ToolBarListener listener);

	public void setEnabled(Action action, boolean enable);

	void setState(String msg);

	void showStateLoader(boolean visible);

}
