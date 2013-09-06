/**
 * 
 */
package org.cotrix.web.importwizard.client.step.selection;

import java.util.List;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AssetsBatch;
import org.cotrix.web.importwizard.shared.ColumnSortInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetInfoDataProvider extends AsyncDataProvider<AssetInfo> {
	
	protected static final ColumnSortInfo DEFAULT_SORT_INFO = new ColumnSortInfo(AssetInfo.NAME_FIELD, true);
	
	@Inject
	protected ImportServiceAsync importService;
	protected DataGrid<AssetInfo> datagrid;
	protected boolean forceRefresh;
	
	/**
	 * @param importService
	 */
	public AssetInfoDataProvider() {
		super(AssetInfoKeyProvider.INSTANCE);
	}

	/**
	 * @param datagrid the datagrid to set
	 */
	public void setDatagrid(DataGrid<AssetInfo> datagrid) {
		this.datagrid = datagrid;
	}

	/**
	 * @param forceRefresh the forceRefresh to set
	 */
	public void setForceRefresh(boolean forceRefresh) {
		this.forceRefresh = forceRefresh;
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
		
		boolean force = forceRefresh;
		forceRefresh = false;
		
		importService.getAssets(range, sortInfo, force, new AsyncCallback<AssetsBatch>() {
			
			@Override
			public void onSuccess(AssetsBatch batch) {
				List<AssetInfo> assets = batch.getAssets();
				Log.trace("loaded "+assets.size()+" assets");
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), assets);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//TODO show the error to the user?
				Log.error("An error occurred loading the assets", caught);
			}
		});
	}

}
