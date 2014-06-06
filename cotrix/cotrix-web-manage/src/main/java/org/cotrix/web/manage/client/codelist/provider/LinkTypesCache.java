/**
 * 
 */
package org.cotrix.web.manage.client.codelist.provider;

import java.util.List;

import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.ManageServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypesCache extends AbstractCache<UILinkType> {
	
	@Inject
	private ManageServiceAsync service;

	public LinkTypesCache() {
		super(UILinkType.class);
	}

	@Override
	protected void retrieveItems(String codelistId, final AsyncCallback<List<UILinkType>> callback) {
		service.getCodelistLinkTypes(codelistId, new AsyncCallback<DataWindow<UILinkType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistLinkTypes", caught);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(DataWindow<UILinkType> result) {
				Log.trace("retrieved CodelistLinkTypes: "+result);
				callback.onSuccess(result.getData());
			}
		});
		
	}

}
