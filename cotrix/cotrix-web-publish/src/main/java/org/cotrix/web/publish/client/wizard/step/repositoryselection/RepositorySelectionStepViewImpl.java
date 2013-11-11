package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.share.client.resources.CommonResources;
import org.cotrix.web.share.client.resources.CotrixSimplePager;
import org.cotrix.web.share.client.resources.DataGridListResource;
import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.client.widgets.SelectionCheckBoxCell;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositorySelectionStepViewImpl extends ResizeComposite implements RepositorySelectionStepView {

	@UiTemplate("RepositorySelectionStep.ui.xml")
	interface RepositorySelectionStepUiBinder extends UiBinder<Widget, RepositorySelectionStepViewImpl> {}

	private static RepositorySelectionStepUiBinder uiBinder = GWT.create(RepositorySelectionStepUiBinder.class);
	
	@UiField (provided = true) 
	PatchedDataGrid<UICodelist> dataGrid;

	@UiField(provided = true)
	SimplePager pager;
	
	protected RepositoryDataProvider dataProvider;
	
	protected SingleSelectionModel<UICodelist> selectionModel;

	private AlertDialog alertDialog;

	private Presenter presenter;

	@Inject
	public RepositorySelectionStepViewImpl(RepositoryDataProvider assetInfoDataProvider) {
		this.dataProvider = assetInfoDataProvider;
		setupGrid();
		initWidget(uiBinder.createAndBindUi(this));
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

		dataGrid = new PatchedDataGrid<UICodelist>(6, DataGridListResource.INSTANCE, RepositoryKeyProvider.INSTANCE);
		dataGrid.setWidth("100%");

		dataGrid.setAutoHeaderRefreshDisabled(true);

		dataGrid.setEmptyTableWidget(new Label("No data"));

		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.setDisplay(dataGrid);
		
		dataGrid.addColumnSortHandler(new AsyncHandler(dataGrid));
		
		selectionModel = new SingleSelectionModel<UICodelist>(RepositoryKeyProvider.INSTANCE);
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
			public String getValue(UICodelist object) {
				return object.getName();
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


		// Type
		Column<UICodelist, String> typeColumn = new Column<UICodelist, String>(new TextCell()) {
			@Override
			public String getValue(UICodelist object) {
				return object.getVersion();
			}
		};
		
		typeColumn.setSortable(false);
		dataGrid.addColumn(typeColumn, "Version");
		dataGrid.setColumnWidth(typeColumn, "20%");
		

		// Repository
	/*	Column<UICodelist, String> repositoryColumn = new Column<UICodelist, String>(new ClickableTextCell()) {
			@Override
			public String getValue(UICodelist object) {
				return object.getRepositoryName();
			}
		};
		repositoryColumn.setSortable(true);
		repositoryColumn.setDataStoreName(AssetInfo.REPOSITORY_FIELD);
		repositoryColumn.setFieldUpdater(new FieldUpdater<AssetInfo, String>() {

			@Override
			public void update(int index, AssetInfo object, String value) {
				Log.trace("repository details selected for row "+index);
				presenter.repositoryDetails(object.getRepositoryId());
			}
		});
		repositoryColumn.setCellStyleNames(CommonResources.INSTANCE.css().linkText());
		
		dataGrid.addColumn(repositoryColumn, "Origin");
		dataGrid.setColumnWidth(repositoryColumn, "20%");*/
			
		dataProvider.setDatagrid(dataGrid);
		dataProvider.addDataDisplay(dataGrid);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog(false);
		}
		alertDialog.setMessage(message);
		alertDialog.center();
	}

	
	@UiHandler("refreshButton")
	protected void refresh(ClickEvent clickEvent)
	{
		dataProvider.setForceRefresh(true);
		dataGrid.setVisibleRangeAndClearData(dataGrid.getVisibleRange(), true);
	}
	
	public void reset()
	{
		selectionModel.clear();
		pager.setPage(0);
	}

}
