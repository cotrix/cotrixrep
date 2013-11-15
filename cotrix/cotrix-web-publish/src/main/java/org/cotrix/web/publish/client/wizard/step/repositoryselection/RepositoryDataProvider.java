/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import java.util.List;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDataProvider extends AsyncDataProvider<UIRepository> {
	
	protected static final ColumnSortInfo DEFAULT_SORT_INFO = new ColumnSortInfo(UIRepository.NAME_FIELD, true);
	
	@Inject
	protected PublishServiceAsync service;
	protected PatchedDataGrid<UIRepository> datagrid;
	protected boolean forceRefresh = true;
	
	/**
	 * @param service
	 */
	public RepositoryDataProvider() {
		super(RepositoryKeyProvider.INSTANCE);
	}

	/**
	 * @param datagrid the datagrid to set
	 */
	public void setDatagrid(PatchedDataGrid<UIRepository> datagrid) {
		this.datagrid = datagrid;
	}

	/**
	 * @param forceRefresh the forceRefresh to set
	 */
	public void setForceRefresh(boolean forceRefresh) {
		this.forceRefresh = forceRefresh;
	}

	@Override
	protected void onRangeChanged(HasData<UIRepository> display) {
	
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
		
		service.getRepositories(range, sortInfo, force, new AsyncCallback<DataWindow<UIRepository>>() {
			
			@Override
			public void onSuccess(DataWindow<UIRepository> batch) {
				List<UIRepository> repositories = batch.getData();
				Log.trace("loaded "+repositories.size()+" repositories");
				updateRowCount(batch.getTotalSize(), true);
				updateRowData(range.getStart(), repositories);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//TODO show the error to the user?
				Log.error("An error occurred loading the codelists", caught);
			}
		});
	}

}
