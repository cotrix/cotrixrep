package org.cotrix.web.importwizard.client.step.selection;

import org.cotrix.web.importwizard.client.resources.DataGridResource;
import org.cotrix.web.importwizard.client.step.selection.CodelistDetailsPanel.BackHandler;
import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SelectionStepViewImpl extends Composite implements SelectionStepView {

	@UiTemplate("SelectionStep.ui.xml")
	interface ChannelStepUiBinder extends UiBinder<Widget, SelectionStepViewImpl> {}

	private static ChannelStepUiBinder uiBinder = GWT.create(ChannelStepUiBinder.class);
	
	@UiField FlowPanel mainPanel;
	@UiField DockLayoutPanel gridPanel;
	
	@UiField (provided = true) 
	DataGrid<AssetInfo> dataGrid;

	@UiField(provided = true)
	SimplePager pager;
	
	protected AssetInfoDataProvider assetInfoDataProvider;

	private AlertDialog alertDialog;
	
	protected CodelistDetailsPanel detailsPanel;

	private Presenter presenter;
	
	protected interface Style extends CssResource {
		String detailsText();
	}
	
	@UiField 
	protected Style style;
	
	protected Column<AssetInfo, String> nameColumn;

	@Inject
	public SelectionStepViewImpl(AssetInfoDataProvider assetInfoDataProvider) {
		this.assetInfoDataProvider = assetInfoDataProvider;
		
		setupGrid();
		initWidget(uiBinder.createAndBindUi(this));
		setHeight("520px");
		
		nameColumn.setCellStyleNames(style.detailsText());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		//TODO we should use ResizeComposite instead of Composite
		//The problem would be in the DeckLayoutPanel that don't call onresize
		if (visible) dataGrid.onResize();
	}


	protected void setupGrid()
	{

		dataGrid = new DataGrid<AssetInfo>(10, DataGridResource.INSTANCE, AssetInfoKeyProvider.INSTANCE);
		dataGrid.setWidth("100%");

		dataGrid.setAutoHeaderRefreshDisabled(true);

		dataGrid.setEmptyTableWidget(new Label("No data"));

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(dataGrid);

		final SingleSelectionModel<AssetInfo> selectionModel = new SingleSelectionModel<AssetInfo>(AssetInfoKeyProvider.INSTANCE);
		selectionModel.addSelectionChangeHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				AssetInfo selectedAsset = selectionModel.getSelectedObject();
				presenter.assetSelected(selectedAsset);
			}
		});
		dataGrid.setSelectionModel(selectionModel);
		
		dataGrid.addColumnSortHandler(new AsyncHandler(dataGrid));

		
		// Check
		TextHeader nameHeader = new TextHeader("Name");
		
		Column<AssetInfo, Boolean> checkColumn = new Column<AssetInfo, Boolean>(new SelectionCheckBoxCell()) {
			
			@Override
			public Boolean getValue(AssetInfo object) {
				return false;
			}
		};
		
		dataGrid.addColumn(checkColumn, nameHeader);
		dataGrid.setColumnWidth(checkColumn, "35px");
		
		// Name
		nameColumn = new Column<AssetInfo, String>(new ClickableTextCell()) {
			@Override
			public String getValue(AssetInfo object) {
				return object.getName();
			}
		};
		nameColumn.setSortable(true);
		nameColumn.setDataStoreName(AssetInfo.NAME_FIELD);
		
		nameColumn.setFieldUpdater(new FieldUpdater<AssetInfo, String>() {

			@Override
			public void update(int index, AssetInfo object, String value) {
				System.out.println("UPDATE UPDATE UPDATE index");
				presenter.assetDetails(object);
			}
		});
		
		dataGrid.addColumn(nameColumn, nameHeader);


		// Type
		Column<AssetInfo, String> typeColumn = new Column<AssetInfo, String>(new TextCell()) {
			@Override
			public String getValue(AssetInfo object) {
				return object.getType();
			}
		};
		
		typeColumn.setSortable(false);
		dataGrid.addColumn(typeColumn, "Type");
		dataGrid.setColumnWidth(typeColumn, "20%");
		

		// Repository
		Column<AssetInfo, String> repositoryColumn = new Column<AssetInfo, String>(new TextCell()) {
			@Override
			public String getValue(AssetInfo object) {
				return object.getRepositoryName();
			}
		};
		repositoryColumn.setSortable(true);
		repositoryColumn.setDataStoreName(AssetInfo.REPOSITORY_FIELD);
		
		dataGrid.addColumn(repositoryColumn, "Origin");
		dataGrid.setColumnWidth(repositoryColumn, "20%");
			
		assetInfoDataProvider.setDatagrid(dataGrid);
		assetInfoDataProvider.addDataDisplay(dataGrid);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}


	@Override
	public void showAssetDetails(AssetDetails asset) {
		if (detailsPanel == null) {
			detailsPanel = new CodelistDetailsPanel();
			detailsPanel.setBackHandler(new BackHandler() {
				
				@Override
				public void backPressed() {
					switchPanels();
				}
			});
		}
		detailsPanel.setAsset(asset);
		switchPanels();
	}
	
	protected void switchPanels()
	{
		if (mainPanel.getWidget(0)==gridPanel) {
			mainPanel.remove(gridPanel);
			mainPanel.add(detailsPanel);
		} else {
			mainPanel.remove(detailsPanel);
			mainPanel.add(gridPanel);
		}
	}
	
	public void reset()
	{
		dataGrid.redraw();
	}



}
