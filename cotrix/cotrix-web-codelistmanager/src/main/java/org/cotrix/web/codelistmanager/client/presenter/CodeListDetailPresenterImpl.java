package org.cotrix.web.codelistmanager.client.presenter;

import java.util.ArrayList;
import java.util.List;

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

	public void setData(final int id) {
		view.showActivityIndicator();
		rpcService.getCodeListModel(id, new AsyncCallback<CotrixImportModel>() {
			public void onSuccess(CotrixImportModel result) {
				view.setData(result,id);
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	public void setDataProvider(final DataGrid<String[]> dataGrid,final int id) {
		AsyncDataProvider<String[]> dataProvider = new AsyncDataProvider<String[]>() {
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
						dataGrid.setRowData(start, result);
					}
				});
			}
		};
		dataProvider.addDataDisplay(dataGrid);
		

	}

}
