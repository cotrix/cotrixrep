/**
 * 
 */
package org.cotrix.web.common.client.util;

import java.util.List;

import org.cotrix.web.common.shared.DataWindow;

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
public abstract class CachedDataProvider<T> extends AsyncDataProvider<T> {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	protected Range lastRange;
	protected List<T> cache;
	
	@Override
	protected void onRangeChanged(final HasData<T> display) {
		final Range range = display.getVisibleRange();
		Log.trace("onRangeChanged range: "+range);

		onRangeChanged(range);
	}
	
	protected abstract void onRangeChanged(Range range);
	
	
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
		updateRowCount(totalSize, true);
		updateRowData(range.getStart(), data);
		lastRange = range;
		fireUpdated(range, true);
	}
	
	public void refresh()
	{
		updateRowData(lastRange.getStart(), cache);
		fireUpdated(lastRange, true);
	}
	
	protected void fireUpdated(Range range, boolean refresh) {
		handlerManager.fireEvent(new DataUpdatedEvent(range, refresh));
	}

	public List<T> getCache() {
		return cache;
	}
	
	public HandlerRegistration addDataUpdatedHandler(DataUpdatedEvent.DataUpdatedHandler handler)
	{
		return handlerManager.addHandler(DataUpdatedEvent.getType(), handler);
	}
}
