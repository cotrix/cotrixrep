/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface AsyncDataProvider<T> {
	
	public void getData(AsyncCallback<T> callaback);
	
	
}
