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
public class MetadataProvider implements AsyncDataProvider<CodeListMetadata> {
	
	@Inject
	protected ManagerServiceAsync service;
	
	@Inject @CodelistId
	protected String codelistId;

	@Override
	public void getData(AsyncCallback<CodeListMetadata> callaback) {
		service.getMetadata(codelistId, callaback);
	}

}
