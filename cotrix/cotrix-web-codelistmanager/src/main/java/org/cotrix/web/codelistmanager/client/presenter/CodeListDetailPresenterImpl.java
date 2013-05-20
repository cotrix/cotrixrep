package org.cotrix.web.codelistmanager.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.el.Coercions;
import org.cotrix.web.codelistmanager.client.ManagerServiceAsync;
import org.cotrix.web.codelistmanager.client.view.AlertDialog;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.codelistmanager.shared.CodeCell;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.omg.IOP.Codec;

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
	private int PAGE_SIZE = 30;
	private AsyncDataProvider<CodeCell[]> dataProvider;
	private List<CodeCell[]> currentData;
	public static final String ADD = "ADD";
	public static final String EDIT = "EDIT";
	public static final String DELETE = "DELETE";
	public ArrayList<BatchCommand> commands = new ArrayList<BatchCommand>();
	
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
	private ArrayList<String[]> codecellToString(ArrayList<CodeCell[]> result){
		ArrayList<String[]> data = new ArrayList<String[]>();
		for (CodeCell[] codeCells : result) {
			String[] s = new String[codeCells.length];
			int index = 0;
			for (CodeCell codeCell : codeCells) {
				s[index++] = codeCell.getValue();
			}
			data.add(s);
		}
		return data;
	}
	public void setDataProvider(final DataGrid<CodeCell[]> dataGrid,final String id) {
		dataProvider = new AsyncDataProvider<CodeCell[]>() {
			@Override
			protected void onRangeChanged(HasData<CodeCell[]> display) {
				final Range range = display.getVisibleRange();
				final ColumnSortList sortList = dataGrid.getColumnSortList();
				final int start = range.getStart();
				int end = start + range.getLength(); 
				rpcService.getDataRange(id, start, end, new AsyncCallback<ArrayList<CodeCell[]>>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(ArrayList<CodeCell[]> result) {
						currentData = result;
						dataProvider.updateRowData(start, result);
					}
				});
			}

		};
		dataProvider.addDataDisplay(dataGrid);

	}
	private CodeCell[] getNewRow(){
		CodeCell[] existingRow = currentData.get(0);
		CodeCell[] newRow = new CodeCell[existingRow.length];
		for (int i = 0; i < existingRow.length; i++) {
			newRow[i].setValue(" ");
		}
		return newRow;
	}
	
	public void insertRow(int row) {
		row = row - view.getPageIndex()*PAGE_SIZE;
		currentData.add(row, getNewRow());
		dataProvider.updateRowData(view.getPageIndex()*PAGE_SIZE,currentData);

		BatchCommand command = new BatchCommand();
		command.setCommand(ADD);
		command.setValue("row = "+row);
		commands.add(command);
	}

	public void deleteRow(int row) {
		row = row - view.getPageIndex()*PAGE_SIZE;
		currentData.remove(row);
		dataProvider.updateRowData(view.getPageIndex()*PAGE_SIZE, currentData);
	
		BatchCommand command = new BatchCommand();
		command.setCommand(DELETE);
		command.setValue("row = "+row);
		commands.add(command);
	}

	public void onCellEdited(int row, int column, CodeCell codeCell) {
		BatchCommand command = new BatchCommand();
		command.setCommand(EDIT);
		command.setValue("row ="+row + " column = "+column + " value = "+codeCell.getValue());
		command.setCodeCell(codeCell);
		commands.add(command);
		
	}
	
	public void onSaveButtonClicked() {
		String display = "";
		for (BatchCommand command : commands) {
			display += display + command.getCommand() + " : "+command.getValue() +"<br>";
			Window.alert(command.getCodeCell().getCodelistId());
			rpcService.editCode(command.getCodeCell(), new AsyncCallback<Void>() {

				public void onFailure(Throwable caught) {
					Window.alert("Edit code list fail");
				}

				public void onSuccess(Void result) {
					Window.alert("Updat data success !!!!");
				}
			});
		}
		// clear old command
		commands = new ArrayList<BatchCommand>();
		
		AlertDialog dialog = new AlertDialog();
		dialog.setMessage(display);
		dialog.show();
		
	}

}
