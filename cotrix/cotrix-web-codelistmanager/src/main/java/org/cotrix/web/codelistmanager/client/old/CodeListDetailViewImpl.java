package org.cotrix.web.codelistmanager.client.old;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.client.resources.DataGridResource;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.UICode;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListDetailViewImpl extends ResizeComposite implements CodeListDetailView, ContextMenuHandler {

	@UiTemplate("CodeListDetail.ui.xml")
	interface CodeListDetailUiBinder extends
	UiBinder<Widget, CodeListDetailViewImpl> {
	}

	private static CodeListDetailUiBinder uiBinder = GWT
			.create(CodeListDetailUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);

	private Presenter presenter;
	private boolean isShowingNavLeft = true;
	
	@UiField DeckLayoutPanel mainPanel;
	@UiField VerticalPanel loadingPanel;
	@UiField DockLayoutPanel contentPanel;
	@UiField HTMLPanel blankPanel;
	
	@UiField VerticalPanel metadataPanel;
	
	@UiField Image nav;
	@UiField Label codelistName;
	@UiField Label metadata;


	@UiField ResizeLayoutPanel dataGridWrapper;
	@UiField HTMLPanel pagerWrapper;
	@UiField Button save;
	@UiField Style style;

	private SimplePager pager;
	private int row  = -1;
	private int column  = -1;

	interface Style extends CssResource {
		String nav();
		String pager();
		String contextMenu();
		String flexTable();
		String save_active();
		String save_inactive();
	}
	private PopupPanel contextMenu;
	private CotrixDataGrid<UICode[]> dataGrid;
	

	public CodeListDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		mainPanel.showWidget(blankPanel);
	}

	@UiHandler("nav")
	public void onNavLeftClicked(ClickEvent event) {
		presenter.onNavLeftClicked(this.isShowingNavLeft);
	}

	@UiHandler("save")
	public void onSaveButtonClicked(ClickEvent event){
		presenter.onSaveButtonClicked()	;
	}

	@UiHandler("codelistName")
	public void onCodelistNameClicked(ClickEvent event) {
		presenter.onCodelistNameClicked(metadataPanel.isVisible());
	}
	public void enableSaveButton(boolean enable){
		if(enable){
			this.save.setEnabled(true);
			this.save.setStyleName(style.save_active());
		}else{
			this.save.setEnabled(false);
			this.save.setStyleName(style.save_inactive());
		}
	}

	private class FlexColumn extends Column<UICode[],String> {
		int index;
		public FlexColumn(Cell<String> cell,int index) {
			super(cell);
			this.index = index;
		}

		@Override
		public String getValue(UICode[] column) {
			if(column.length <= index){
				return "";
			}else{
				return column[index].getAttribute().getValue();
			}
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


		DataGridResource resource = GWT.create(DataGridResource.class);
		dataGrid = new CotrixDataGrid<UICode[]>(30, resource);
		dataGrid.setStyleName(style.flexTable());
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(""));
		dataGrid.addCellPreviewHandler(new Handler<UICode[]>() {
			public void onCellPreview(CellPreviewEvent<UICode[]> event) {
				column = event.getColumn();
				row  = event.getIndex();
			}
		});


		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER,pagerResources, false, 0, true);
		pager.setStyleName(style.pager());
		pager.setDisplay(dataGrid);
		this.dataGridWrapper.clear();
		this.pagerWrapper.clear();
		this.dataGridWrapper.add(dataGrid);
		this.pagerWrapper.add(pager);

		String[] headers = model.getCsvFile().getHeader();
		for (int i = 0; i < headers.length; i++) {
			final int columnIndex = i;
			FlexColumn column = new FlexColumn(new CotrixEditTextCell(),i);
			column.setSortable(true);
			column.setFieldUpdater(new FieldUpdater<UICode[], String>() {
				public void update(int index, UICode[] codes, String value) {
					codes[columnIndex].getAttribute().setValue(value);
					presenter.onCellEdited(codes[columnIndex]);
				}

			});

			dataGrid.addColumn(column, headers[i]);
			dataGrid.setColumnWidth(i, "200px");
		}

		dataGrid.setRowCount(model.getTotalRow(), true);
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
		mainPanel.showWidget(contentPanel);
		/*this.loadingPanel.setVisible(false);
		this.contentPanel.setVisible(true);
		this.blankPanel.setVisible(false);*/
	}

	public void showActivityIndicator() {
		mainPanel.showWidget(loadingPanel);
		/*this.contentPanel.setVisible(false);
		this.blankPanel.setVisible(false);
		this.loadingPanel.setVisible(true);*/
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

	public int getPageIndex() {
		return pager.getPage();
	}



}