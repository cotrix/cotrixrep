package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.client.resources.DataGridResource;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

public class CodeListDetailViewImpl extends Composite implements
CodeListDetailView, ContextMenuHandler {

	@UiTemplate("CodeListDetail.ui.xml")
	interface CodeListDetailUiBinder extends
	UiBinder<Widget, CodeListDetailViewImpl> {
	}

	private static CodeListDetailUiBinder uiBinder = GWT
			.create(CodeListDetailUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);

	private Presenter presenter;
	private boolean isShowingNavLeft = true;
	@UiField Image nav;
	@UiField Label codelistName;
	@UiField Label metadata;
	@UiField VerticalPanel metadataPanel;
	@UiField VerticalPanel loadingPanel;
	@UiField HTMLPanel contentPanel;
	@UiField HTMLPanel blankPanel;
	@UiField ResizeLayoutPanel dataGridWrapper;
	@UiField HTMLPanel pagerWrapper;
	@UiField Style style;
	private SimplePager pager;
	private int row  = -1;
	private int column  = -1;

	interface Style extends CssResource {
		String nav();
		String pager();
		String contextMenu();
		String flexTable();
	}
	private PopupPanel contextMenu;
	private CotrixDataGrid<String[]> dataGrid;

	@UiHandler("nav")
	public void onNavLeftClicked(ClickEvent event) {
		presenter.onNavLeftClicked(this.isShowingNavLeft);
	}

	@UiHandler("codelistName")
	public void onCodelistNameClicked(ClickEvent event) {
		presenter.onCodelistNameClicked(metadataPanel.isVisible());
	}


	private class FlexColumn extends Column<String[],String> {
		int index;
		public FlexColumn(Cell<String> cell,int index) {
			super(cell);
			this.index = index;
		}
		
		public String getValue(String[] column) {
			return column[index];
		}
	}

	private void initTable(CotrixImportModel model, String id) {
		VerticalPanel panel = new VerticalPanel();

		ContextMenuItem item1  = new ContextMenuItem("Inssert row above");
		item1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				contextMenu.hide();
				presenter.insertRow(row);
			}
		});

		ContextMenuItem item2  = new ContextMenuItem("Inssert row below");
		item2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				contextMenu.hide();
				presenter.insertRow(row+1);
			}
		});

		ContextMenuItem item3  = new ContextMenuItem("Delete this row");
		item3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				contextMenu.hide();
				presenter.deleteRow(row);
			}
		});




		panel.add(item1);
		panel.add(item2);
		panel.add(item3);

		this.contextMenu = new PopupPanel(true);
		this.contextMenu.add(panel);
		this.contextMenu.setStyleName(style.contextMenu());
		this.contextMenu.hide();


		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER,pagerResources, false, 0, true);
		pager.setStyleName(style.pager());
		pager.setDisplay(dataGrid);

		DataGridResource resource = GWT.create(DataGridResource.class);
		dataGrid = new CotrixDataGrid<String[]>(30, resource);
		dataGrid.setStyleName(style.flexTable());
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(""));
		dataGrid.addCellPreviewHandler(new Handler<String[]>() {
			public void onCellPreview(CellPreviewEvent<String[]> event) {
				column = event.getColumn();
				row  = event.getIndex();
			}
		});

		this.dataGridWrapper.clear();
		this.pagerWrapper.clear();
		this.dataGridWrapper.add(dataGrid);
		this.pagerWrapper.add(pager);

		String[] headers = model.getCsvFile().getHeader();
		for (int i = 0; i < headers.length; i++) {
			final int columnIndex = i;
			FlexColumn column = new FlexColumn(new CotrixEditTextCell(),i);
			column.setSortable(true);
			column.setFieldUpdater(new FieldUpdater<String[], String>() {
				public void update(int index, String[] object, String value) {
					object[index] = value;
					presenter.onCellEdited(index, columnIndex, value);
				}
			});
			dataGrid.addColumn(column, headers[i]);
			dataGrid.setColumnWidth(i, "200px");
		}
		dataGrid.setRowCount(12000, true);
		dataGrid.setVisibleRange(0, 30);
		dataGrid.getRowCount();
		dataGrid.setHeight(dataGridWrapper.getOffsetHeight() + "px");

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				dataGrid.setHeight(dataGridWrapper.getOffsetHeight() - 110
						+ "px");
			}
		});

		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				dataGrid.setHeight(dataGridWrapper.getOffsetHeight() - 110
						+ "px");
			}
		});
		this.presenter.setDataProvider(dataGrid, id);
		addDomHandler(this, ContextMenuEvent.getType());
	}

	public CodeListDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void showNavLeft() {
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_left().getSafeUri().asString());
		isShowingNavLeft = true;
	}

	public void showNavRight() {
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_right().getSafeUri().asString());
		isShowingNavLeft = false;
	}

	public void init() {

	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void showMetadataPanel(boolean isVisible) {
		metadataPanel.setVisible(!isVisible);
	}

	private void showContentPanel() {
		this.loadingPanel.setVisible(false);
		this.contentPanel.setVisible(true);
		this.blankPanel.setVisible(false);
	}

	public void showActivityIndicator() {
		this.contentPanel.setVisible(false);
		this.blankPanel.setVisible(false);
		this.loadingPanel.setVisible(true);
	}

	public void setData(CotrixImportModel model, String id) {
		Metadata metadata = model.getMetadata();

		this.codelistName.setText(metadata.getName());
		this.metadata.setText(metadata.getDescription());

		initTable(model, id);
		showContentPanel();
	}

	public void onContextMenu(ContextMenuEvent event) {
		// stop the browser from opening the context menu
		event.preventDefault();
		event.stopPropagation();
		this.contextMenu.setPopupPosition(event.getNativeEvent().getClientX(),
				event.getNativeEvent().getClientY());
		this.contextMenu.show();
	}



}
