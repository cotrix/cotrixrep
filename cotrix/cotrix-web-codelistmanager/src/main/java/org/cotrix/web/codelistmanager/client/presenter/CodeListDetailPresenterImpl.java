package org.cotrix.web.codelistmanager.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.AlertDialog;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.UICode;


import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.DataGrid;
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
	private int PAGE_SIZE = 30;
	private AsyncDataProvider<UICode[]> dataProvider;
	private List<UICode[]> currentData;
	public static final String ADD = "ADD";
	public static final String EDIT = "EDIT";
	public static final String DELETE = "DELETE";
	public HashMap<String,UICode> editedCodes = new HashMap<String,UICode>();

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
	private ArrayList<String[]> UICodeToString(ArrayList<UICode[]> result){
		ArrayList<String[]> data = new ArrayList<String[]>();
		for (UICode[] codes : result) {
			String[] s = new String[codes.length];
			int index = 0;
			for (UICode code : codes) {
				s[index++] = code.getAttribute().getValue();
			}
			data.add(s);
		}
		return data;
	}
	public void setDataProvider(final DataGrid<UICode[]> dataGrid,final String id) {
		dataProvider = new AsyncDataProvider<UICode[]>() {
			@Override
			protected void onRangeChanged(HasData<UICode[]> display) {
				final Range range = display.getVisibleRange();
				final ColumnSortList sortList = dataGrid.getColumnSortList();
				final int start = range.getStart();
				int end = start + range.getLength(); 
				rpcService.getDataRange(id, start, end, new AsyncCallback<ArrayList<UICode[]>>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(ArrayList<UICode[]> result) {
//						Window.alert("result size"+result.size());
						currentData = result;
						dataProvider.updateRowData(start, result);
					}
				});
			}

		};
		dataProvider.addDataDisplay(dataGrid);

	}
	private UICode[] getNewRow(){
		UICode[] existingRow = currentData.get(0);
		UICode[] newRow = new UICode[existingRow.length];
		for (int i = 0; i < existingRow.length; i++) {
			newRow[i].getAttribute().setValue(" ");
		}
		return newRow;
	}

	public void insertRow(int row) {
		row = row - view.getPageIndex()*PAGE_SIZE;
		currentData.add(row, getNewRow());
		dataProvider.updateRowData(view.getPageIndex()*PAGE_SIZE,currentData);

		BatchCommand command = new BatchCommand();
		command.setCommand(ADD);
	}

	public void deleteRow(int row) {
		row = row - view.getPageIndex()*PAGE_SIZE;
		currentData.remove(row);
		dataProvider.updateRowData(view.getPageIndex()*PAGE_SIZE, currentData);

		BatchCommand command = new BatchCommand();
		command.setCommand(DELETE);
	}

	public void onCellEdited(UICode code) {
		editedCodes.put(code.getAttribute().getId(), code);
		view.enableSaveButton(true);
	}

	public void onSaveButtonClicked() {
		rpcService.editCode(new ArrayList<UICode>(editedCodes.values()), new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert("Edit code list fail");
			}

			public void onSuccess(Void result) {
				Window.alert("Updat data success !!!!");
				view.enableSaveButton(false);
			}
		});
		// clear old command

		editedCodes = new HashMap<String, UICode>();

	}

}
