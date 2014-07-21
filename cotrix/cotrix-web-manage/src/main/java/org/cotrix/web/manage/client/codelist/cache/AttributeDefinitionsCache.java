/**
 * 
 */
package org.cotrix.web.manage.client.codelist.cache;

import java.util.Collection;

import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.ManageServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionsCache extends AbstractCache<UIAttributeDefinition> {
	
	@Inject
	private ManageServiceAsync service;

	public AttributeDefinitionsCache() {
		super(UIAttributeDefinition.class);
	}

	@Override
	protected void retrieveItems(String codelistId, final AsyncCallback<Collection<UIAttributeDefinition>> callback) {
		service.getCodelistAttributeTypes(codelistId, new AsyncCallback<DataWindow<UIAttributeDefinition>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistAttributeTypes", caught);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(DataWindow<UIAttributeDefinition> result) {
				Log.trace("retrieved CodelistAttributeTypes: "+result);
				callback.onSuccess(result.getData());
			}
		});		
	}

}
