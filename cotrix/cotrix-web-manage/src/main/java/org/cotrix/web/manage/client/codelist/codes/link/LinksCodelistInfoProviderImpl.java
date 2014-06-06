/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.provider.LinkTypesCache;
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
	private ManageServiceAsync managerService;
	
	@Inject @CurrentCodelist
	private UICodelist codelist;
	
	@Inject @CurrentCodelist
	private LinkTypesCache linkTypesCache;

	@Override
	public void getCodelistLinkTypes(final AsyncCallback<List<UILinkTypeInfo>> callback) {
		
		linkTypesCache.getItems(new AsyncCallback<List<UILinkType>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<UILinkType> result) {
				List<UILinkTypeInfo> linkTypeInfos = new ArrayList<UILinkTypeInfo>(result.size());
				for (UILinkType linkType:result) linkTypeInfos.add(new UILinkTypeInfo(linkType.getId(), linkType.getName()));
				callback.onSuccess(linkTypeInfos);
			}
		});
	}

	@Override
	public void getCodelistCodes(String linkTypeId,	AsyncCallback<List<UICodeInfo>> callback) {
		managerService.getCodes(codelist.getId(), linkTypeId, callback);
	}

}
