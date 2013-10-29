/**
 * 
 */
package org.cotrix.web.share.client.util;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class FilteredCachedDataProvider<T> extends CachedDataProvider<T> {

	public interface Filter<T> {
		public boolean accept(T data);
	}
	
	protected List<Filter<T>> appliedFilters = new ArrayList<Filter<T>>();
	protected List<T> unfilteredCache;
	protected int totalCount;
	protected int unfilteredTotalCount;
	protected boolean applyFiltersOnLoad = false;
	
	protected void updateData(List<T> data, Range range, int totalSize) {
		super.updateData(data, range, totalSize);
		if (applyFiltersOnLoad) {
			unfilteredCache = null;
			applyFilters();
		}
	}
	
	public void applyFilters(Filter<T> ... filters)
	{
		appliedFilters = new ArrayList<Filter<T>>(filters.length);
		for (Filter<T> filter:filters) appliedFilters.add(filter);
		applyFilters();
	}
	
	protected void applyFilters()
	{
		if (unfilteredCache == null) {
			unfilteredCache = new ArrayList<T>(cache);
			unfilteredTotalCount = totalCount;
		}
		cache.clear();
		for (T data:unfilteredCache) {
			boolean accept = accept(data);
			if (accept) cache.add(data);
		}
		Log.trace("cache: "+cache);
		updateRowCount(cache.size(), true);
		refresh();
	}
	
	protected boolean accept(T data)
	{
		for (Filter<T> filter:appliedFilters) if (filter.accept(data)) return true;
		return false;
	}
	
	public void unapplyFilters()
	{
		appliedFilters.clear();
		if (unfilteredCache == null) return;
		cache = unfilteredCache;
		unfilteredCache = null;
		updateRowCount(unfilteredTotalCount, true);
		unfilteredTotalCount = 0;
		refresh();
	}
	
	  @Override
	  public void updateRowCount(int size, boolean exact) {
	    super.updateRowCount(size, exact);
	    this.totalCount = size;
	  }

}
