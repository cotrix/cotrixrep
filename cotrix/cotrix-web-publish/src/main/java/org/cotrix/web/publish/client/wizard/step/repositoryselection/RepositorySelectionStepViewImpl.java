package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.resources.CotrixSimplePager;
import org.cotrix.web.common.client.resources.DataGridListResource;
import org.cotrix.web.common.client.widgets.PageSizer;
import org.cotrix.web.common.client.widgets.SearchBox;
import org.cotrix.web.common.client.widgets.cell.SelectionCheckBoxCell;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.publish.shared.UIRepository;

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
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextHeader;
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
public class RepositorySelectionStepViewImpl extends ResizeComposite implements RepositorySelectionStepView {

	@UiTemplate("RepositorySelectionStep.ui.xml")
	interface RepositorySelectionStepUiBinder extends UiBinder<Widget, RepositorySelectionStepViewImpl> {}

	private static RepositorySelectionStepUiBinder uiBinder = GWT.create(RepositorySelectionStepUiBinder.class);
	
	@UiField
	SearchBox searchBox;
	
	@UiField (provided = true) 
	PatchedDataGrid<UIRepository> dataGrid;

	@UiField(provided = true)
	SimplePager pager;
	
	@UiField
	PageSizer pageSizer;
	
	protected RepositoryDataProvider dataProvider;
	
	protected SingleSelectionModel<UIRepository> selectionModel;

	private Presenter presenter;
	
	@Inject
	AlertDialog alertDialog;

	@Inject
	public RepositorySelectionStepViewImpl(RepositoryDataProvider assetInfoDataProvider) {
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

		dataGrid = new PatchedDataGrid<UIRepository>(25, DataGridListResource.INSTANCE, RepositoryKeyProvider.INSTANCE);
		dataGrid.setWidth("100%");

		dataGrid.setAutoHeaderRefreshDisabled(true);

		dataGrid.setEmptyTableWidget(new Label("No data"));

		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.setDisplay(dataGrid);
		
		dataGrid.addColumnSortHandler(new AsyncHandler(dataGrid));
		
		selectionModel = new SingleSelectionModel<UIRepository>(RepositoryKeyProvider.INSTANCE);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				UIRepository selected = selectionModel.getSelectedObject();
				if (selected!=null) presenter.repositorySelected(selected);	 
			}
		});
		
		dataGrid.setSelectionModel(selectionModel);

		// Check
		TextHeader nameHeader = new TextHeader("Name");
		
		Column<UIRepository, Boolean> checkColumn = new Column<UIRepository, Boolean>(new SelectionCheckBoxCell(true, false)) {
			
			@Override
			public Boolean getValue(UIRepository object) {
				boolean selected = selectionModel.isSelected(object);
				return selected;
			}
		};
		
		dataGrid.addColumn(checkColumn, nameHeader);
		dataGrid.setColumnWidth(checkColumn, "35px");
		
		// Name
		Column<UIRepository, String> nameColumn = new Column<UIRepository, String>(new ClickableTextCell()) {
			@Override
			public String getValue(UIRepository object) {
				return object.getName().getLocalPart();
			}
		};
		nameColumn.setSortable(true);
		nameColumn.setDataStoreName(UIRepository.NAME_FIELD);
		
		nameColumn.setFieldUpdater(new FieldUpdater<UIRepository, String>() {

			@Override
			public void update(int index, UIRepository object, String value) {
				Log.trace("details selected for row "+index);
				presenter.repositoryDetails(object);
			}
		});
		
		nameColumn.setCellStyleNames(CommonResources.INSTANCE.css().linkText());
		
		dataGrid.addColumn(nameColumn, nameHeader);


		// Publish Type
		Column<UIRepository, String> publishedTypesColumn = new Column<UIRepository, String>(new TextCell()) {
			@Override
			public String getValue(UIRepository object) {
				return object.getPublishedTypes();
			}
		};
		
		publishedTypesColumn.setSortable(true);
		publishedTypesColumn.setDataStoreName(UIRepository.PUBLISHED_TYPES_FIELD);
		
		dataGrid.addColumn(publishedTypesColumn, "Published types");
		dataGrid.setColumnWidth(publishedTypesColumn, "20%");
			
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
		cleanFilter();	
		selectionModel.clear();
		Log.trace("setRefreshCache");
		dataProvider.setForceRefresh(true);
		pager.setPage(0);
		dataGrid.setVisibleRangeAndClearData(dataGrid.getVisibleRange(), true);
	}

}
