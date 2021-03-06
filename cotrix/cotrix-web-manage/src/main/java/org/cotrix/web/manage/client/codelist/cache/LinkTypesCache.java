/**
 * 
 */
package org.cotrix.web.manage.client.codelist.cache;

import java.util.Collection;

import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypesCache extends AbstractCache<UILinkDefinition> {
	
	@Inject
	private ManageServiceAsync service;

	@Inject @CurrentCodelist
	private String codelistId;

	public LinkTypesCache() {
		super(UILinkDefinition.class);
	}

	@Override
	protected void retrieveItems(final AsyncCallback<Collection<UILinkDefinition>> callback) {
		service.getCodelistLinkTypes(codelistId, new AsyncCallback<DataWindow<UILinkDefinition>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistLinkTypes", caught);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(DataWindow<UILinkDefinition> result) {
				Log.trace("retrieved CodelistLinkTypes: "+result);
				callback.onSuccess(result.getData());
			}
		});
		
	}

}
