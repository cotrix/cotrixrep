package org.cotrix.web.codelistmanager.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

public class CodeListDetailPresenterImpl implements CodeListDetailPresenter {
	public interface OnNavigationClicked {
		public void onNavigationClicked(boolean isShowingNavLeft);
	}

	private OnNavigationClicked onNavigationClicked;

	private ManagerServiceAsync rpcService;
	private HandlerManager eventBus;
	private CodeListDetailView view;
	private AsyncDataProvider<String[]> dataProvider;
	private List<String[]> currentData;

	@Inject
	public CodeListDetailPresenterImpl(ManagerServiceAsync rpcService,
			HandlerManager eventBus, CodeListDetailView view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		HorizontalPanel hp = (HorizontalPanel) container;
		hp.add(view.asWidget());
		hp.setCellWidth(hp.getWidget(1), "100%");
	}

	public void setOnNavigationLeftClicked(
			OnNavigationClicked onNavigationClicked) {
		this.onNavigationClicked = onNavigationClicked;
	}

	public void onNavLeftClicked(boolean isShowingNavLeft) {
		if (isShowingNavLeft) {
			onNavigationClicked.onNavigationClicked(true);
			view.showNavRight();
		} else {
			onNavigationClicked.onNavigationClicked(false);
			view.showNavLeft();
		}
	}

	public void onCodelistNameClicked(boolean isVisible) {
		view.showMetadataPanel(isVisible);
	}

	public void setData(final String id) {
		view.showActivityIndicator();
		rpcService.getCodeListModel(id, new AsyncCallback<CotrixImportModel>() {
			public void onSuccess(CotrixImportModel result) {
				view.setData(result,id);
			}

			public void onFailure(Throwable caught) {
				Window.alert("Getting codelist fail");
			}
		});
	}

	public void setDataProvider(final DataGrid<String[]> dataGrid,final String id) {
		dataProvider = new AsyncDataProvider<String[]>() {
			@Override
			protected void onRangeChanged(HasData<String[]> display) {
				final Range range = display.getVisibleRange();
				final ColumnSortList sortList = dataGrid.getColumnSortList();
				final int start = range.getStart();
				int end = start + range.getLength();
				rpcService.getDataRange(id, start, end, new AsyncCallback<ArrayList<String[]>>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(ArrayList<String[]> result) {
						currentData = result;
						dataProvider.updateRowData(start, result);
					}
				});
			}
		};
		dataProvider.addDataDisplay(dataGrid);

	}
	private String[] getNewRow(){
		String[] existingRow = currentData.get(0);
		String[] newRow =new String[existingRow.length];
		for (int i = 0; i < existingRow.length; i++) {
			newRow[i] = " ";
		}
		return newRow;
	}
	
	public void insertRow(int row) {
		currentData.add(row, getNewRow());
		dataProvider.updateRowData(0,currentData);
	}

	public void deleteRow(int row) {
		currentData.remove(row);
		dataProvider.updateRowData(0, currentData);
	}

	public void onCellEdited(int row, int column, String value) {
		Window.alert("Row = "+row +" Column = "+column);
	}

}
