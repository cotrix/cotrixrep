/**
 * 
 */
package org.cotrix.web.share.client.util;

import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.view.client.AsyncDataProvider;
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
	
	protected void updateData(List<T> data, Range range, int totalSize)
	{
		this.cache = data;
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

	public List<T> getCache() {
		return cache;
	}
	
	public HandlerRegistration addDataUpdatedHandler(DataUpdatedEvent.DataUpdatedHandler handler)
	{
		return handlerManager.addHandler(DataUpdatedEvent.getType(), handler);
	}
}
