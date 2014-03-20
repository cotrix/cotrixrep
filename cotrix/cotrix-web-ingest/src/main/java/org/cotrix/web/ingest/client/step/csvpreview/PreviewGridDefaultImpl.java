/**
 * 
 */
package org.cotrix.web.ingest.client.step.csvpreview;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.resources.Resources;
import org.cotrix.web.ingest.shared.PreviewData;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PreviewGridDefaultImpl extends ResizeComposite implements PreviewGrid {
	
	protected static final int HEADER_ROW = 0;
	
	protected ScrollPanel scroll;
	protected FlexTable grid;
	protected FlexTable loadingContainter;
	
	protected List<TextBox> headerFields = new ArrayList<TextBox>();
	
	@Inject
	IngestServiceAsync service;
	
	@Inject
	Resources resources;
	
	protected CsvConfiguration configuration;
	
	@Inject
	public PreviewGridDefaultImpl(Resources resources)
	{
		scroll = new ScrollPanel();
		grid = new FlexTable();
		grid.setStyleName(resources.css().preview());
		scroll.setWidget(grid);
		
		setupLoadingContainer();
		initWidget(scroll);
	}

	protected void setupLoadingContainer()
	{
		loadingContainter = new FlexTable();
		loadingContainter.getElement().setAttribute("align", "center");
		loadingContainter.setWidth("100%");
		loadingContainter.setHeight("100%");
		Image loader = new Image(CommonResources.INSTANCE.dataLoader());
		loadingContainter.setWidget(0, 0, loader);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void loadData()
	{
		setLoading();
		AsyncCallback<PreviewData> callback = new AsyncCallback<PreviewData>() {
			
			@Override
			public void onSuccess(PreviewData result) {
				setData(result);
				unsetLoading();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				unsetLoading();
			}
		};
		
		Log.trace("loading preview data with configuration "+configuration);
		service.getCsvPreviewData(configuration, callback);
	}
	
	protected void setLoading()
	{
		scroll.setWidget(loadingContainter);
	}
	
	protected void unsetLoading()
	{
		scroll.setWidget(grid);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void resetScroll() {
		scroll.scrollToTop();
		scroll.scrollToLeft();
	}
	
	protected void setData(PreviewData data)
	{
		grid.removeAllRows();
		setupHeader(data.getHeadersLabels(), data.isHeadersEditable());
		setRows(data.getRows());
	}
	
	protected void setupHeader(List<String> headersLabels, boolean editable)
	{
		Log.trace("setupHeader "+headersLabels+" editable? "+editable);
		headerFields.clear();
		for (int i = 0; i < headersLabels.size(); i++) {
			String headerLabel = headersLabels.get(i);
			
			Widget header = null;
			if (editable) {
				TextBox headerField = new TextBox();
				headerField.setValue(headerLabel);
				headerField.setStyleName(CommonResources.INSTANCE.css().textBox());
				headerFields.add(headerField);
				header = headerField;
			} else {
				header = new HTML(headerLabel);
			}
			
			grid.setWidget(HEADER_ROW, i, header);
			grid.getCellFormatter().setStyleName(HEADER_ROW, i, resources.css().previewHeader());
		}
	}
	

	protected void setRows(List<List<String>> rows) {
		for (List<String> row:rows) addRow(row);
	}
	
	protected void addRow(List<String> row)
	{
		int rowIndex = grid.getRowCount();
		for (int i = 0; i < row.size(); i++) {
			String cell = row.get(i);
			grid.setWidget(rowIndex, i, new HTML(cell));
			grid.getCellFormatter().setStyleName(rowIndex, i, resources.css().previewCell());
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		for (TextBox headerField:headerFields) headers.add(headerField.getText());
		return headers;
	}
		
	@Override
	public CsvConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(CsvConfiguration configuration) {
		this.configuration = configuration;
	}
}
