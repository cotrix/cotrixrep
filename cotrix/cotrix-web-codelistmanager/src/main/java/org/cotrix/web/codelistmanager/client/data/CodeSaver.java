/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.shared.UICode;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeSaver extends AbstractDataSaver<UICode> {
	
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject
	@CodelistId
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	@Inject
	public CodeSaver(CodeEditor editor) {
		editor.addDataEditHandler(this);
	}

	@Override
	public void save(UICode data, AsyncCallback<Void> callback) {
		Log.trace("SAVING ROW: "+data);
		service.saveCodelistRow(codelistId, data, callback);
	}

}
