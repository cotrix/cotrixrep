/**
 * 
 */
package org.cotrix.web.ingest.client.step.selection;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.shared.AssetInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class AssetInfoDataProvider extends AsyncDataProvider<AssetInfo> {
	
	protected static final ColumnSortInfo DEFAULT_SORT_INFO = new ColumnSortInfo(AssetInfo.NAME_FIELD, true);
	
	@Inject
	private IngestServiceAsync importService;
	private PatchedDataGrid<AssetInfo> datagrid;
	private boolean refreshCache;
	private boolean requestDiscovery;
	private String query;
	
	/**
	 * @param importService
	 */
	public AssetInfoDataProvider() {
		super(AssetInfoKeyProvider.INSTANCE);
		query = "";
	}

	/**
	 * @param datagrid the datagrid to set
	 */
	public void setDatagrid(PatchedDataGrid<AssetInfo> datagrid) {
		this.datagrid = datagrid;
	}

	public void setRequestDiscovery(boolean requestDiscovery) {
		this.requestDiscovery = requestDiscovery;
	}

	public void setRefreshCache(boolean refreshCache) {
		this.refreshCache = refreshCache;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	protected void onRangeChanged(HasData<AssetInfo> display) {
	
		final Range range = display.getVisibleRange();
		Log.trace("onRangeChanged range: "+range);
		
		ColumnSortList columnSortList = datagrid.getColumnSortList();
		
		ColumnSortInfo sortInfo = DEFAULT_SORT_INFO;
		if (columnSortList.size()>0) {
			com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo columnSortInfo = columnSortList.get(0);
			sortInfo = new ColumnSortInfo(columnSortInfo.getColumn().getDataStoreName(), columnSortInfo.isAscending());
		}
		
		Log.trace("sortInfo: "+sortInfo);
		
		boolean discovery = requestDiscovery;
		requestDiscovery = false;
		
		boolean cache = refreshCache;
		refreshCache = false;
		
		importService.getAssets(range, sortInfo, query, cache, discovery, new ManagedFailureCallback<DataWindow<AssetInfo>>() {
			
			@Override
			public void onSuccess(DataWindow<AssetInfo> batch) {
				List<AssetInfo> assets = batch.getData();
				Log.trace("loaded "+assets.size()+" assets");
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), assets);
			}
		});
	}

}
