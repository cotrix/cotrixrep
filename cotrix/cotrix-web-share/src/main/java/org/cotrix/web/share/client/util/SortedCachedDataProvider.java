/**
 * 
 */
package org.cotrix.web.share.client.util;

import org.cotrix.web.share.shared.ColumnSortInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class SortedCachedDataProvider<T> extends CachedDataProvider<T> {
	
	protected ColumnSortInfo defaultSortInfo;

	/**
	 * 
	 */
	public SortedCachedDataProvider(String defaultSortedField) {
		defaultSortInfo = new ColumnSortInfo(defaultSortedField, true);
	}

	@Override
	protected void onRangeChanged(Range range) {
		
		ColumnSortInfo sortInfo = getSortInfo();
		Log.trace("sortInfo: "+sortInfo);
		
		onRangeChange(range, sortInfo);
	}
	
	protected ColumnSortInfo getSortInfo() {
		for (HasData<T> display:getDataDisplays()) {
			if (display instanceof AbstractCellTable) {
				AbstractCellTable<T> abstractCellTable = (AbstractCellTable<T>) display;
				ColumnSortList columnSortList = abstractCellTable.getColumnSortList();
				if (columnSortList.size()>0) {
					com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo columnSortInfo = columnSortList.get(0);
					return new ColumnSortInfo(columnSortInfo.getColumn().getDataStoreName(), columnSortInfo.isAscending());
				}
			}
		}
		return defaultSortInfo;
	}
	
	protected abstract void onRangeChange(Range range, ColumnSortInfo sortInfo);
	
}
