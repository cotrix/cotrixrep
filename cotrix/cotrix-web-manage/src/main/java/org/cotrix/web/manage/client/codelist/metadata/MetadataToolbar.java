/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MetadataToolbar {
	
	enum Action {
		LOCK,
		UNLOCK,
		FINALIZE,
		TO_CODES;
	}
	
	public interface ToolBarListener {
		public void onAction(Action action);
	}
	
	public void setListener(ToolBarListener listener);

	public void setEnabled(Action action, boolean enable);

}
