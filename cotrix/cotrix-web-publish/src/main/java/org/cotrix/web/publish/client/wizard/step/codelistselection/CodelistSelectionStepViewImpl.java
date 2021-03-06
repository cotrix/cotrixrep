package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.resources.CotrixSimplePager;
import org.cotrix.web.common.client.resources.DataGridListResource;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.PageSizer;
import org.cotrix.web.common.client.widgets.SearchBox;
import org.cotrix.web.common.client.widgets.cell.SelectionCheckBoxCell;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistSelectionStepViewImpl extends ResizeComposite implements CodelistSelectionStepView {

	@UiTemplate("CodelistSelectionStep.ui.xml")
	interface CodelistSelectionStepUiBinder extends UiBinder<Widget, CodelistSelectionStepViewImpl> {}

	private static CodelistSelectionStepUiBinder uiBinder = GWT.create(CodelistSelectionStepUiBinder.class);
	
	@UiField
	SearchBox searchBox;

	@UiField (provided = true) 
	PatchedDataGrid<UICodelist> dataGrid;

	@UiField(provided = true)
	SimplePager pager;
	
	@UiField
	PageSizer pageSizer;
	
	@Inject
	AlertDialog alertDialog;

	protected CodelistDataProvider dataProvider;

	protected SingleSelectionModel<UICodelist> selectionModel;

	private Presenter presenter;

	@Inject
	public CodelistSelectionStepViewImpl(CodelistDataProvider assetInfoDataProvider) {
		this.dataProvider = assetInfoDataProvider;
		setupGrid();
		initWidget(uiBinder.createAndBindUi(this));
		pageSizer.setDisplay(dataGrid);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		//The problem would be in the DeckLayoutPanel that don't call onresize
		if (visible) dataGrid.onResize();
	}


	protected void setupGrid()
	{

		dataGrid = new PatchedDataGrid<UICodelist>(25, DataGridListResource.INSTANCE, CodelistKeyProvider.INSTANCE);
		dataGrid.setWidth("100%");

		dataGrid.setAutoHeaderRefreshDisabled(true);

		dataGrid.setEmptyTableWidget(new Label("No data"));

		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.setDisplay(dataGrid);

		dataGrid.addColumnSortHandler(new AsyncHandler(dataGrid));

		selectionModel = new SingleSelectionModel<UICodelist>(CodelistKeyProvider.INSTANCE);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				UICodelist selected = selectionModel.getSelectedObject();
				if (selected!=null) presenter.codelistSelected(selected);	 
			}
		});

		dataGrid.setSelectionModel(selectionModel);

		// Check
		TextHeader nameHeader = new TextHeader("Name");

		Column<UICodelist, Boolean> checkColumn = new Column<UICodelist, Boolean>(new SelectionCheckBoxCell(true, false)) {

			@Override
			public Boolean getValue(UICodelist object) {
				boolean selected = selectionModel.isSelected(object);
				return selected;
			}
		};

		dataGrid.addColumn(checkColumn, nameHeader);
		dataGrid.setColumnWidth(checkColumn, "35px");

		// Name
		Column<UICodelist, String> nameColumn = new Column<UICodelist, String>(new ClickableTextCell()) {
			@Override
			public String getValue(UICodelist codelist) {
				return codelist!=null?ValueUtils.getValue(codelist.getName()):"n/a";
			}
		};
		nameColumn.setSortable(true);
		nameColumn.setDataStoreName(UICodelist.NAME_FIELD);

		nameColumn.setFieldUpdater(new FieldUpdater<UICodelist, String>() {

			@Override
			public void update(int index, UICodelist object, String value) {
				Log.trace("details selected for row "+index);
				presenter.codelistDetails(object);
			}
		});

		nameColumn.setCellStyleNames(CommonResources.INSTANCE.css().linkText());

		dataGrid.addColumn(nameColumn, nameHeader);

		// State
		Column<UICodelist, String> stateColumn = new Column<UICodelist, String>(new ClickableTextCell()) {
			@Override
			public String getValue(UICodelist object) {
				return String.valueOf(object.getState());
			}
		};
		stateColumn.setSortable(true);
		stateColumn.setDataStoreName(UICodelist.STATE_FIELD);

		dataGrid.addColumn(stateColumn, "State");
		dataGrid.setColumnWidth(stateColumn, "30%");


		// Version
		Column<UICodelist, String> versionColumn = new Column<UICodelist, String>(new TextCell()) {
			@Override
			public String getValue(UICodelist object) {
				return object.getVersion();
			}
		};

		versionColumn.setSortable(true);
		versionColumn.setDataStoreName(UICodelist.VERSION_FIELD);
		dataGrid.addColumn(versionColumn, "Version");
		dataGrid.setColumnWidth(versionColumn, "20%");

		dataProvider.setDatagrid(dataGrid);
		dataProvider.addDataDisplay(dataGrid);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void alert(String message) {
		alertDialog.center(message);
	}
	

	@UiHandler("searchBox")
	protected void onValueChange(ValueChangeEvent<String> event) {
		updateFilter(event.getValue());
	}
	
	private void updateFilter(String query) {
		dataProvider.setQuery(query);
		dataGrid.setVisibleRangeAndClearData(dataGrid.getVisibleRange(), true);
	}
	
	private void cleanFilter() {
		dataProvider.setQuery("");
		searchBox.clear();
	}

	public void reset()
	{
		Log.trace("RESET");
		cleanFilter();	
		dataProvider.setForceRefresh(true);
		selectionModel.clear();
		pager.setPage(0);
		dataGrid.setVisibleRangeAndClearData(dataGrid.getVisibleRange(), true);
	}

}
