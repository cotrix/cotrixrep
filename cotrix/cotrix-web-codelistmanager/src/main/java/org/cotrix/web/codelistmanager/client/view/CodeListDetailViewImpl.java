package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.client.resources.DataGridResource;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CodeListDetailViewImpl extends Composite implements CodeListDetailView {


	@UiTemplate("CodeListDetail.ui.xml")
	interface CodeListDetailUiBinder extends UiBinder<Widget, CodeListDetailViewImpl> {}
	private static CodeListDetailUiBinder uiBinder = GWT.create(CodeListDetailUiBinder.class);

	CotrixManagerResources resources = GWT.create(CotrixManagerResources.class);

	private Presenter presenter;
	private boolean isShowingNavLeft = true;
	@UiField Image nav;
	@UiField Label codelistName;
	@UiField Label metadata;
	@UiField VerticalPanel metadataPanel;
	@UiField HTMLPanel contentPanel;
	@UiField HTMLPanel blankPanel;
	@UiField HTMLPanel dataGridWrapper;
	@UiField HTMLPanel pagerWrapper;

//	@UiField(provided = true)
//	DataGrid<String[]> dataGrid;

//	@UiField(provided = true)
//	SimplePager pager;

	@UiHandler("nav")
	public void onNavLeftClicked(ClickEvent event) {
		presenter.onNavLeftClicked(this.isShowingNavLeft);
	}
	@UiHandler("codelistName")
	public void onCodelistNameClicked(ClickEvent event) {
		presenter.onCodelistNameClicked(metadataPanel.isVisible());
	}

	@UiField Style style;
	interface Style extends CssResource {
		String nav();
		String pager();
	}
	
	private class FlexColumn extends TextColumn<String[]>{
		int index;
		public FlexColumn(int index) {
			this.index = index;
		}
		@Override
		public String getValue(String[] column) {
			return column[index];
		}
	}
	
	
	private void initTable(CotrixImportModel model , int id){
		DataGridResource resource = GWT.create(DataGridResource.class);
		DataGrid<String[]> dataGrid = new DataGrid<String[]>(30,resource);
		dataGrid.setWidth("100%");
		dataGrid.setHeight("100%");
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(""));
		
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setStyleName(style.pager());
		pager.setDisplay(dataGrid);
		
		this.dataGridWrapper.clear();
		this.pagerWrapper.clear();
		this.dataGridWrapper.add(dataGrid);
		this.pagerWrapper.add(pager);
		
		String[] headers =  model.getCsvFile().getHeader();
		for (int i = 0; i < headers.length; i++) {
			FlexColumn column = new FlexColumn(i);
			column.setSortable(true);
			dataGrid.addColumn(column,headers[i]);
		}
	    dataGrid.setRowCount(model.getCsvFile().getData().size(), true);
	    dataGrid.setVisibleRange(0, 30);
	    this.presenter.setDataProvider(dataGrid,id);
	}

	public CodeListDetailViewImpl() {
//		DataGrid<String[]> dataGrid = new DataGrid<String[]>();
//		dataGrid.setAutoHeaderRefreshDisabled(true);
//		dataGrid.setEmptyTableWidget(new Label(""));
//
//		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//		pager.setDisplay(dataGrid);

		initWidget(uiBinder.createAndBindUi(this));
	}

	public void showNavLeft(){
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_left().getSafeUri().asString());
		isShowingNavLeft = true;
	}

	public void showNavRight(){
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


	private void showContentPanel(){
		this.contentPanel.setVisible(true);
		this.blankPanel.setVisible(false);
	}

	public void setData(CotrixImportModel model,int id) {
		Metadata metadata= model.getMetadata();

		this.codelistName.setText(metadata.getName());
		this.metadata.setText(metadata.getDescription());

		initTable(model,id);
		showContentPanel();
	}



}
