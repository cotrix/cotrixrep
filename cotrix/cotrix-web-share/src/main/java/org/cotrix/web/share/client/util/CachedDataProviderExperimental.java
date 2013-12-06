/**
 * 
 */
package org.cotrix.web.share.client.util;

import java.util.List;

import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class CachedDataProviderExperimental<T> extends AsyncDataProvider<T> {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	protected Range lastRange;
	protected List<T> cache;
	protected int lastRowCount = 0;
	
	@Override
	protected void onRangeChanged(final HasData<T> display) {
		final Range range = display.getVisibleRange();
		Log.trace("onRangeChanged range: "+range);

		onRangeChanged(range, new ManagedFailureCallback<DataWindow<T>>() {

			@Override
			public void onSuccess(DataWindow<T> result) {
				updateData(result, range);
			}
		});
	}
	
	protected abstract void onRangeChanged(Range range, ManagedFailureCallback<DataWindow<T>> callaback);
		
	
	protected void updateData(DataWindow<T> dataWindow)
	{
		updateData(dataWindow.getData(), new Range(0, dataWindow.getTotalSize()), dataWindow.getTotalSize());
	}
	
	protected void updateData(DataWindow<T> dataWindow, Range range)
	{
		updateData(dataWindow.getData(), range, dataWindow.getTotalSize());
	}
	
	protected void updateData(List<T> data, Range range, int totalSize)
	{
		this.cache = data;
		this.lastRowCount = totalSize;
		updateRowCount(totalSize, true);
		updateRowData(range.getStart(), data);
		lastRange = range;
		handlerManager.fireEvent(new DataUpdatedEvent(range));
	}
	
	public void refresh()
	{
		handlerManager.fireEvent(new DataUpdatedEvent(lastRange));
		updateRowData(lastRange.getStart(), cache);
	}
	

	public void remove(T item) {
		int index = cache.indexOf(item);
		if (index>=0) remove(index);
	}
	
	public void remove(int index) {
		System.out.println("remove "+index);
		
		if (index<0 || index>=cache.size()) throw new IndexOutOfBoundsException("Cache size "+cache.size());
		cache.remove(index);
		final int nextItemIndex = lastRange.getStart() + cache.size() + 1;
		Log.trace("nextItemIndex: "+nextItemIndex);
		
		if (nextItemIndex<lastRowCount) {
			onRangeChanged(new Range(nextItemIndex,1), new ManagedFailureCallback<DataWindow<T>>() {

				@Override
				public void onSuccess(DataWindow<T> result) {
					Log.trace("retrieved "+result);
					cache.add(result.getData().get(0));
					Log.trace("cache: "+cache);
					lastRowCount--;
					updateRowCount(lastRowCount, true);
					updateRowData(lastRange.getStart(), cache);
				}
			});
		} else {
			lastRowCount--;
			updateRowCount(lastRowCount, true);
			updateRowData(lastRange.getStart(), cache);
		}
	}


	public List<T> getCache() {
		return cache;
	}
	
	public HandlerRegistration addDataUpdatedHandler(DataUpdatedEvent.DataUpdatedHandler handler)
	{
		return handlerManager.addHandler(DataUpdatedEvent.getType(), handler);
	}
}
