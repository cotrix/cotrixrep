/**
 * 
 */
package org.cotrix.web.manage.client.codelist.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataSavedEvent;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.event.ManagerBus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractCache<T extends Identifiable> implements Iterable<T> {

	private Class<T> type;

	private Map<String, T> cache = null;

	public AbstractCache(Class<T> type) {
		this.type = type;
	}

	@Inject
	private void init(@CodelistBus EventBus codelistBus) {
		codelistBus.addHandler(DataEditEvent.getType(type), new DataEditEvent.DataEditHandler<T>() {

			@Override
			public void onDataEdit(DataEditEvent<T> event) {
				Log.trace("cache "+type+" onDataEdit "+event);
				switch (event.getEditType()) {
					case UPDATE: addItem(event.getData()); break;
					case REMOVE: cache.remove(event.getData()); break;
					default: break;
				}
			}
		});
	}
	
	@Inject
	private void bind(final @ManagerBus EventBus eventBus) {
		eventBus.addHandler(DataSavedEvent.TYPE, new DataSavedEvent.DataSavedHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDataSaved(final DataSavedEvent event) {
				DataEditEvent<?> editEvent = event.getEditEvent();
				if (editEvent.getEditType() == EditType.ADD && editEvent.getData().getClass() == type) addItem((T) editEvent.getData());
			}
		});
	}

	public Collection<T> getItems() {
		if (cache == null) throw new IllegalStateException("Cache for type "+type+" not ready!");
		return cache.values();
	}

	public void setup(final AsyncCallback<Void> callback) {

		retrieveItems(new AsyncCallback<Collection<T>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("cache "+type+" filling failed ", caught);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Collection<T> result) {
				Log.trace("cache "+type+" filled cache with "+result);
				setCache(result);
				callback.onSuccess(null);
			}
		});

	}

	private void setCache(Collection<T> items) {
		if (cache == null) cache = new HashMap<String, T>();
		cache.clear();
		for (T item:items) addItem(item);
	}

	private void addItem(T item) {
		cache.put(item.getId(), item);
	}

	public T getItem(String id) {
		return cache.get(id);
	}

	@Override
	public Iterator<T> iterator() {
		return cache.values().iterator();
	}

	protected abstract void retrieveItems(AsyncCallback<Collection<T>> callback);
}
