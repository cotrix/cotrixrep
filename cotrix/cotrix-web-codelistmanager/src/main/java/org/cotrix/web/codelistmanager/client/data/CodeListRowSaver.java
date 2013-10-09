/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListRowSaver extends AbstractDataSaver<UICodeListRow> {
	
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject
	@CodelistId
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	@Inject
	public CodeListRowSaver(CodeListRowEditor editor) {
		editor.addDataEditHandler(this);
	}

	@Override
	public void save(UICodeListRow data, AsyncCallback<Void> callback) {
		Log.trace("SAVING ROW: "+data);
		//service.saveMetadata(codelistId, data, callback);
	}

}
