/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataProvider implements AsyncDataProvider<UICodelistMetadata> {
	
	@Inject
	protected ManageServiceAsync service;
	
	@Inject @CurrentCodelist
	protected String codelistId;

	@Override
	public void getData(AsyncCallback<UICodelistMetadata> callaback) {
		service.getMetadata(codelistId, callaback);
	}

}
