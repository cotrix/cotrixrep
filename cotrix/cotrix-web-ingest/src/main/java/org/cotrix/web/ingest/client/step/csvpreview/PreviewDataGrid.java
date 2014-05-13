/**
 * 
 */
package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.resources.CotrixSimplePager;
import org.cotrix.web.common.client.util.CachedDataProvider;
import org.cotrix.web.common.client.widgets.EditableTextHeader;
import org.cotrix.web.common.client.widgets.StyledTextInputCell;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.shared.CsvPreviewHeaders;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PreviewDataGrid extends ResizeComposite {
	
	interface DataGridResources extends PatchedDataGrid.Resources {

		@Source("PreviewDataGrid.css")
		DataGridStyle dataGridStyle();
	}
	
	interface DataGridStyle extends PatchedDataGrid.Style {}

	private PatchedDataGrid<List<String>> previewGrid;
	private CachedDataProvider<List<String>> dataprovider;
	private SimplePager pager;

	private List<EditableTextHeader> editableHeaders = new ArrayList<EditableTextHeader>();
	private CsvConfiguration configuration;

	private IngestServiceAsync service;
	
	@Inject
	private CommonResources resources;

	@Inject
	public PreviewDataGrid(IngestServiceAsync service) {
		this.service = service;
		setupGrid();

		DockLayoutPanel layoutPanel = new DockLayoutPanel(Unit.PX);
		layoutPanel.setWidth("100%");
		SimplePanel pagerContainer = new SimplePanel(pager);
		pagerContainer.setWidth("100%");
		layoutPanel.addSouth(pagerContainer, 40);
		layoutPanel.add(previewGrid);
		initWidget(layoutPanel);
		setWidth("100%");
	}

	protected void setupGrid()
	{
		DataGridResources resource = GWT.create(DataGridResources.class);
		previewGrid = new PatchedDataGrid<List<String>>(8, resource);
		previewGrid.setWidth("100%");
		previewGrid.setAutoAdjust(true);
		previewGrid.setPageSize(8);

		previewGrid.setAutoHeaderRefreshDisabled(true);

		previewGrid.setEmptyTableWidget(new Label("No data"));

		dataprovider = new CachedDataProvider<List<String>>() {

			@Override
			protected void onRangeChanged(final Range range) {
				Log.trace("onRangeChanged range: "+range);
				service.getCsvPreviewData(range, new ManagedFailureCallback<DataWindow<List<String>>>() {

					@Override
					public void onSuccess(DataWindow<List<String>> result) {
						Log.trace("retrieved "+result);
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

	protected void loadHeaders() {
		Log.trace("loading headers");
		service.getCsvPreviewHeaders(configuration, new ManagedFailureCallback<CsvPreviewHeaders>() {

			@Override
			public void onSuccess(CsvPreviewHeaders result) {
				createColumns(result);
				
				pager.setPage(0);
				previewGrid.setVisibleRangeAndClearData(previewGrid.getVisibleRange(), true);
			}
		});
	}

	protected void createColumns(CsvPreviewHeaders headers)
	{
		Log.trace("Columns labels: "+headers.getLabels());
		
		editableHeaders.clear();
		int count = previewGrid.getColumnCount();
		for (int i = 0; i<count; i++) previewGrid.removeColumn(0);
		Log.trace("ColumnCount: "+previewGrid.getColumnCount());

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
				EditableTextHeader header = new EditableTextHeader(new StyledTextInputCell(resources.css().textBox()), headerLabel);
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

	public CsvConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(CsvConfiguration configuration) {
		this.configuration = configuration;		
	}
}
