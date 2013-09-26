/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataSaver extends AbstractDataSaver<CodeListMetadata> {
	
	@Inject
	protected ManagerServiceAsync service;
	
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	@Inject
	public MetadataSaver(@Assisted String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	public void save(CodeListMetadata data, AsyncCallback<Void> callback) {
		service.saveMetadata(codelistId, data, callback);
	}

}
