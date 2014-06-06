/**
 * 
 */
package org.cotrix.web.manage.client.codelist.cache;

import java.util.List;

import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.ManageServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypesCache extends AbstractCache<UIAttributeType> {
	
	@Inject
	private ManageServiceAsync service;

	public AttributeTypesCache() {
		super(UIAttributeType.class);
	}

	@Override
	protected void retrieveItems(String codelistId, final AsyncCallback<List<UIAttributeType>> callback) {
		service.getCodelistAttributeTypes(codelistId, new AsyncCallback<DataWindow<UIAttributeType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistAttributeTypes", caught);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(DataWindow<UIAttributeType> result) {
				Log.trace("retrieved CodelistAttributeTypes: "+result);
				callback.onSuccess(result.getData());
			}
		});		
	}

}
