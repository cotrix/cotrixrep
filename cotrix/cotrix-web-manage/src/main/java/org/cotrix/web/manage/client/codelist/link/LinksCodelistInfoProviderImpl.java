/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkTypeInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinksCodelistInfoProviderImpl implements LinksCodelistInfoProvider {
	
	@Inject
	protected ManageServiceAsync managerService;
	
	@Inject @CurrentCodelist
	protected UICodelist codelist;

	@Override
	public void getCodelistLinkTypes(AsyncCallback<List<UILinkTypeInfo>> callback) {
		managerService.getLinkTypes(codelist.getId(), callback);		
	}

	@Override
	public void getCodelistCodes(String linkTypeId,	AsyncCallback<List<UICodeInfo>> callback) {
		managerService.getCodes(codelist.getId(), linkTypeId, callback);
	}

}
