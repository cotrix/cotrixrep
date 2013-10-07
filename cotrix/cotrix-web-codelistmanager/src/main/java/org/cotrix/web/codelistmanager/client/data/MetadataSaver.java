/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataSaver extends AbstractDataSaver<CodeListMetadata> {
	
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject
	@CodelistId
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	@Inject
	public MetadataSaver(MetadataEditor editor) {
		editor.addDataEditHandler(this);
	}

	@Override
	public void save(CodeListMetadata data, AsyncCallback<Void> callback) {
		service.saveMetadata(codelistId, data, callback);
	}

}
