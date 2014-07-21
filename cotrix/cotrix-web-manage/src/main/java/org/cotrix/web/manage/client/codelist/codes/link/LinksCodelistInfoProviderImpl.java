/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.shared.UICodeInfo;
import org.cotrix.web.manage.shared.UILinkDefinitionInfo;

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
	public void getCodelistLinkDefinitions(final AsyncCallback<List<UILinkDefinitionInfo>> callback) {
		
		linkTypesCache.getItems(new AsyncCallback<Collection<UILinkDefinition>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Collection<UILinkDefinition> result) {
				List<UILinkDefinitionInfo> linkTypeInfos = new ArrayList<UILinkDefinitionInfo>(result.size());
				for (UILinkDefinition linkType:result) linkTypeInfos.add(new UILinkDefinitionInfo(linkType.getId(), linkType.getName()));
				callback.onSuccess(linkTypeInfos);
			}
		});
	}

	@Override
	public void getCodelistCodes(String linkTypeId,	AsyncCallback<List<UICodeInfo>> callback) {
		managerService.getCodes(codelist.getId(), linkTypeId, callback);
	}

}
