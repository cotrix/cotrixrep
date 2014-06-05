/**
 * 
 */
package org.cotrix.web.ingest.client.util;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.resources.CotrixSimplePager;
import org.cotrix.web.common.client.util.CachedDataProvider;
import org.cotrix.web.common.client.widgets.AdvancedPagerLayout;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.client.widgets.PageSizer;
import org.cotrix.web.common.client.widgets.cell.EditableTextHeader;
import org.cotrix.web.common.client.widgets.cell.StyledTextInputCell;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.ingest.client.resources.Resources;
import org.cotrix.web.ingest.shared.PreviewHeaders;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.Range;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewDataGrid extends LoadingPanel {
	
	public interface PreviewDataProvider {
		public void getHeaders(AsyncCallback<PreviewHeaders> headersCallBack);
		public void getData(Range range, AsyncCallback<DataWindow<List<String>>> dataCallBack);
	}
	
	interface DataGridResources extends PatchedDataGrid.Resources {

		@Source("PreviewDataGrid.css")
		DataGridStyle dataGridStyle();
	}
	
	interface DataGridStyle extends PatchedDataGrid.Style {}

	private PatchedDataGrid<List<String>> previewGrid;
	private CachedDataProvider<List<String>> dataprovider;
	private SimplePager pager;

	private List<EditableTextHeader> editableHeaders = new ArrayList<EditableTextHeader>();

	private PreviewDataProvider previewDataProvider;

	public PreviewDataGrid(PreviewDataProvider previewDataProvider, int pageSize) {
		this.previewDataProvider = previewDataProvider;
		setupGrid(pageSize);

		DockLayoutPanel layoutPanel = new DockLayoutPanel(Unit.PX);
		layoutPanel.setWidth("100%");
		
		AdvancedPagerLayout pagerLayout = new AdvancedPagerLayout();
		pagerLayout.addPager(pager);
		
		PageSizer pageSizer = new PageSizer();
		pageSizer.setDisplay(previewGrid);
		pagerLayout.addPageSizer(pageSizer);
		
		layoutPanel.addSouth(pagerLayout, 40);
		layoutPanel.add(previewGrid);
		initWidget(layoutPanel);
		setWidth("100%");
	}

	private void setupGrid(int pageSize)
	{
		DataGridResources resource = GWT.create(DataGridResources.class);
		previewGrid = new PatchedDataGrid<List<String>>(pageSize, resource);
		previewGrid.setWidth("100%");
		previewGrid.setAutoAdjust(true);
		previewGrid.setPageSize(pageSize);

		previewGrid.setAutoHeaderRefreshDisabled(true);

		previewGrid.setEmptyTableWidget(new Label("No data"));

		dataprovider = new CachedDataProvider<List<String>>() {

			@Override
			protected void onRangeChanged(final Range range) {
				Log.trace("onRangeChanged range: "+range);
				previewDataProvider.getData(range, new AsyncCallback<DataWindow<List<String>>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(DataWindow<List<String>> result) {
						updateData(result, range);					
					}
				});
			}
		};
		dataprovider.addDataDisplay(previewGrid);

		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.getElement().getStyle().setProperty("margin", "0 auto");
		pager.setDisplay(previewGrid);
	}

	private void loadHeaders() {
		Log.trace("loading headers");
		showLoader();
		previewDataProvider.getHeaders(new ManagedFailureCallback<PreviewHeaders>() {

			public void onCallFailed() {
				hideLoader();
			}

			@Override
			public void onSuccess(PreviewHeaders result) {
				setHeaders(result);
			}
		});
	}
	
	private void setHeaders(PreviewHeaders headers) {
		createColumns(headers);
		
		hideLoader();
		pager.setPage(0);
		previewGrid.setVisibleRangeAndClearData(previewGrid.getVisibleRange(), true);
	}

	private void createColumns(PreviewHeaders headers)
	{
		Log.trace("Columns labels: "+headers.getLabels());
		
		editableHeaders.clear();
		int count = previewGrid.getColumnCount();
		for (int i = 0; i<count; i++) previewGrid.removeColumn(0);
		//Log.trace("ColumnCount: "+previewGrid.getColumnCount());

		int colIndex = 0;
		for (String headerLabel:headers.getLabels()) {
			final int index = colIndex++;
			Column<List<String>, String> column = new Column<List<String>, String>(new TextCell()) {
				@Override
				public String getValue(List<String> row) {
					return index<row.size()?row.get(index):"";
				}
			};
			column.setSortable(false);

			if (headers.isEditable()) {
				StyledTextInputCell cell = new StyledTextInputCell(CommonResources.INSTANCE.css().textBox()+ " "+Resources.INSTANCE.css().previewHeader());
				cell.setTitle("Rename this column.");
				EditableTextHeader header = new EditableTextHeader(cell, headerLabel);
				editableHeaders.add(header);
				previewGrid.addColumn(column, header);
			} else {
				previewGrid.addColumn(column, headerLabel);
			}

		}
		previewGrid.redrawHeaders();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		//GWT issue 7188 workaround
		if (visible) previewGrid.onResize();
	}


	public void loadData() {
		loadHeaders();
	}

	public void resetScroll() {
	}

	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		for (EditableTextHeader editableTextHeader:editableHeaders) {
			headers.add(editableTextHeader.getValue());
		}
		return headers;
	}
}
