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
public class MetadataProvider implements AsyncDataProvider<CodeListMetadata> {
	
	@Inject
	protected ManagerServiceAsync service;
	
	protected String codelistId;

	/**
	 * @param codelistId
	 */
	@Inject
	public MetadataProvider(@Assisted String codelistId) {
		this.codelistId = codelistId;
	}

	@Override
	public void getData(AsyncCallback<CodeListMetadata> callaback) {
		service.getMetadata(codelistId, callaback);
	}

}
