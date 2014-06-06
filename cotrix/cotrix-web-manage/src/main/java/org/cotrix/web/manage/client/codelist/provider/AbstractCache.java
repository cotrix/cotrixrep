/**
 * 
 */
package org.cotrix.web.manage.client.codelist.provider;

import java.util.List;

import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractCache<T> {
	
	private Class<T> type;
	
	@Inject
	private ManageServiceAsync service;
	
	@Inject @CurrentCodelist
	private String codelistId;
	
	private List<T> cache = null;
	
	public AbstractCache(Class<T> type) {
		this.type = type;
	}

	@Inject
	private void init(@CodelistBus EventBus codelistBus) {
		codelistBus.addHandler(DataEditEvent.getType(type), new DataEditEvent.DataEditHandler<T>() {

			@Override
			public void onDataEdit(DataEditEvent<T> event) {
				switch (event.getEditType()) {
					case ADD: cache.add(event.getData()); break;
					case REMOVE: cache.remove(event.getData()); break;
					default: break;
				}
			}
		});
	}
	
	
	public void getItems(final AsyncCallback<List<T>> callback) {
		if (cache != null) callback.onSuccess(cache);
		else {
			retrieveItems(codelistId, new AsyncCallback<List<T>>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(List<T> result) {
					cache = result;
					callback.onSuccess(result);
				}
			});
		}
	}
	
	protected abstract void retrieveItems(String codelistId, AsyncCallback<List<T>> callback);
	
	/*{
		service.getCodelistLinkTypes(codelistId, new AsyncCallback<DataWindow<UILinkType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistLinkTypes", caught);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(DataWindow<UILinkType> result) {
				Log.trace("retrieved CodelistLinkTypes: "+result);
				linkTypesCache = result.getData();
				callback.onSuccess(linkTypesCache);
			}
		});
	}*/

}
