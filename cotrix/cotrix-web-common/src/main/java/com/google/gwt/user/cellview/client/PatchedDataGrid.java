/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.cellview.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TableLayout;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableColElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.user.client.ui.CustomScrollPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

/**
 * A tabular view with a fixed header and footer section and a scrollable data
 * section in the middle. This widget supports paging and columns.
 * 
 * <p>
 * <h3>Columns</h3> The {@link Column} class defines the
 * {@link com.google.gwt.cell.client.Cell} used to render a column. Implement
 * {@link Column#getValue(Object)} to retrieve the field value from the row
 * object that will be rendered in the {@link com.google.gwt.cell.client.Cell}.
 * </p>
 * 
 * <p>
 * <h3>Headers and Footers</h3> A {@link Header} can be placed at the top
 * (header) or bottom (footer) of the {@link PatchedDataGrid}. You can specify a header
 * as text using {@link #addColumn(Column, String)}, or you can create a custom
 * {@link Header} that can change with the value of the cells, such as a column
 * total. The {@link Header} will be rendered every time the row data changes or
 * the table is redrawn. If you pass the same header instance (==) into adjacent
 * columns, the header will span the columns.
 * </p>
 * 
 * <p>
 * <h3>Examples</h3>
 * <dl>
 * <dt>Trivial example</dt>
 * <dd>{@example com.google.gwt.examples.cellview.CellTableExample}</dd>
 * <dt>FieldUpdater example</dt>
 * <dd>{@example com.google.gwt.examples.cellview.CellTableFieldUpdaterExample}</dd>
 * <dt>Key provider example</dt>
 * <dd>{@example com.google.gwt.examples.view.KeyProviderExample}</dd>
 * </dl>
 * </p>
 * 
 * @param <T> the data type of each row
 */
public class PatchedDataGrid<T> extends AbstractCellTable<T> implements RequiresResize {

	/**
	 * A ClientBundle that provides images for this widget.
	 */
	public interface Resources extends ClientBundle {
		/**
		 * The loading indicator used while the table is waiting for data.
		 */
		@Source("cellTableLoading.gif")
		@ImageOptions(flipRtl = true)
		ImageResource dataGridLoading();

		/**
		 * Icon used when a column is sorted in ascending order.
		 */
		@Source("sortAscending.png")
		@ImageOptions(flipRtl = true)
		ImageResource dataGridSortAscending();

		/**
		 * Icon used when a column is sorted in descending order.
		 */
		@Source("sortDescending.png")
		@ImageOptions(flipRtl = true)
		ImageResource dataGridSortDescending();

		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style dataGridStyle();
	}

	/**
	 * Styles used by this widget.
	 */
	@ImportedWithPrefix("gwt-CellTable")
	public interface Style extends CssResource {
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "com/google/gwt/user/cellview/client/DataGrid.css";

		/**
		 * Applied to every cell.
		 */
		String dataGridCell();

		/**
		 * Applied to even rows.
		 */
		String dataGridEvenRow();

		/**
		 * Applied to cells in even rows.
		 */
		String dataGridEvenRowCell();

		/**
		 * Applied to the first column.
		 */
		String dataGridFirstColumn();

		/**
		 * Applied to the first column footers.
		 */
		String dataGridFirstColumnFooter();

		/**
		 * Applied to the first column headers.
		 */
		String dataGridFirstColumnHeader();

		/**
		 * Applied to footers cells.
		 */
		String dataGridFooter();

		/**
		 * Applied to headers cells.
		 */
		String dataGridHeader();

		/**
		 * Applied to the hovered row.
		 */
		String dataGridHoveredRow();

		/**
		 * Applied to the cells in the hovered row.
		 */
		String dataGridHoveredRowCell();

		/**
		 * Applied to the keyboard selected cell.
		 */
		String dataGridKeyboardSelectedCell();

		/**
		 * Applied to the keyboard selected row.
		 */
		String dataGridKeyboardSelectedRow();

		/**
		 * Applied to the cells in the keyboard selected row.
		 */
		String dataGridKeyboardSelectedRowCell();

		/**
		 * Applied to the last column.
		 */
		String dataGridLastColumn();

		/**
		 * Applied to the last column footers.
		 */
		String dataGridLastColumnFooter();

		/**
		 * Applied to the last column headers.
		 */
		String dataGridLastColumnHeader();

		/**
		 * Applied to odd rows.
		 */
		String dataGridOddRow();

		/**
		 * Applied to cells in odd rows.
		 */
		String dataGridOddRowCell();

		/**
		 * Applied to selected rows.
		 */
		String dataGridSelectedRow();

		/**
		 * Applied to cells in selected rows.
		 */
		String dataGridSelectedRowCell();

		/**
		 * Applied to header cells that are sortable.
		 */
		String dataGridSortableHeader();

		/**
		 * Applied to header cells that are sorted in ascending order.
		 */
		String dataGridSortedHeaderAscending();

		/**
		 * Applied to header cells that are sorted in descending order.
		 */
		String dataGridSortedHeaderDescending();

		/**
		 * Applied to the table.
		 */
		String dataGridWidget();
	}

	/**
	 * A simple widget wrapper around a table element.
	 */
	static class TableWidget extends Widget {
		private final TableColElement colgroup;
		private TableSectionElement section;
		private final TableElement tableElem;

		public TableWidget() {
			// Setup the table.
			tableElem = Document.get().createTableElement();
			tableElem.setCellSpacing(0);
			tableElem.getStyle().setTableLayout(TableLayout.FIXED);
			tableElem.getStyle().setWidth(100.0, Unit.PCT);
			setElement(tableElem);

			// Add the colgroup.
			colgroup = Document.get().createColGroupElement();
			tableElem.appendChild(colgroup);
		}

		public void addColumnStyleName(int index, String styleName) {
			ensureTableColElement(index).addClassName(styleName);
		}

		/**
		 * Get the {@link TableColElement} at the specified index, creating it if
		 * necessary.
		 * 
		 * @param index the column index
		 * @return the {@link TableColElement}
		 */
		public TableColElement ensureTableColElement(int index) {
			// Ensure that we have enough columns.
			for (int i = colgroup.getChildCount(); i <= index; i++) {
				colgroup.appendChild(Document.get().createColElement());
			}
			return colgroup.getChild(index).cast();
		}

		public void removeColumnStyleName(int index, String styleName) {
			if (index >= colgroup.getChildCount()) {
				return;
			}
			ensureTableColElement(index).removeClassName(styleName);
		}

		/**
		 * Hide columns that aren't used in the table.
		 * 
		 * @param start the first unused column index
		 */
		void hideUnusedColumns(int start) {
			/*
			 * Set the width to zero for all col elements that appear after the last
			 * column. Clearing the width would cause it to take up the remaining
			 * width in a fixed layout table.
			 */
			int colCount = colgroup.getChildCount();
			for (int i = start; i < colCount; i++) {
				colgroup.removeChild(colgroup.getChild(colgroup.getChildCount()-1));
			}
		}

		void setColumnWidth(int column, String width) {
			if (width == null) {
				ensureTableColElement(column).getStyle().clearWidth();
			} else {
				ensureTableColElement(column).getStyle().setProperty("width", width);
			}
		}
	}

	/**
	 * Adapter class to convert {@link Resources} to
	 * {@link AbstractCellTable.Resources}.
	 */
	private static class ResourcesAdapter implements AbstractCellTable.Resources {

		private final PatchedDataGrid.Resources resources;
		private final StyleAdapter style;

		public ResourcesAdapter(PatchedDataGrid.Resources resources) {
			this.resources = resources;
			this.style = new StyleAdapter(resources.dataGridStyle());
		}

		@Override
		public ImageResource sortAscending() {
			return resources.dataGridSortAscending();
		}

		@Override
		public ImageResource sortDescending() {
			return resources.dataGridSortDescending();
		}

		@Override
		public AbstractCellTable.Style style() {
			return style;
		}
	}

	/**
	 * Adapter class to convert {@link Style} to {@link AbstractCellTable.Style}.
	 */
	private static class StyleAdapter implements AbstractCellTable.Style {
		private final PatchedDataGrid.Style style;

		public StyleAdapter(PatchedDataGrid.Style style) {
			this.style = style;
		}

		@Override
		public String cell() {
			return style.dataGridCell();
		}

		@Override
		public String evenRow() {
			return style.dataGridEvenRow();
		}

		@Override
		public String evenRowCell() {
			return style.dataGridEvenRowCell();
		}

		@Override
		public String firstColumn() {
			return style.dataGridFirstColumn();
		}

		@Override
		public String firstColumnFooter() {
			return style.dataGridFirstColumnFooter();
		}

		@Override
		public String firstColumnHeader() {
			return style.dataGridFirstColumnHeader();
		}

		@Override
		public String footer() {
			return style.dataGridFooter();
		}

		@Override
		public String header() {
			return style.dataGridHeader();
		}

		@Override
		public String hoveredRow() {
			return style.dataGridHoveredRow();
		}

		@Override
		public String hoveredRowCell() {
			return style.dataGridHoveredRowCell();
		}

		@Override
		public String keyboardSelectedCell() {
			return style.dataGridKeyboardSelectedCell();
		}

		@Override
		public String keyboardSelectedRow() {
			return style.dataGridKeyboardSelectedRow();
		}

		@Override
		public String keyboardSelectedRowCell() {
			return style.dataGridKeyboardSelectedRowCell();
		}

		@Override
		public String lastColumn() {
			return style.dataGridLastColumn();
		}

		@Override
		public String lastColumnFooter() {
			return style.dataGridLastColumnFooter();
		}

		@Override
		public String lastColumnHeader() {
			return style.dataGridLastColumnHeader();
		}

		@Override
		public String oddRow() {
			return style.dataGridOddRow();
		}

		@Override
		public String oddRowCell() {
			return style.dataGridOddRowCell();
		}

		@Override
		public String selectedRow() {
			return style.dataGridSelectedRow();
		}

		@Override
		public String selectedRowCell() {
			return style.dataGridSelectedRowCell();
		}

		@Override
		public String sortableHeader() {
			return style.dataGridSortableHeader();
		}

		@Override
		public String sortedHeaderAscending() {
			return style.dataGridSortedHeaderAscending();
		}

		@Override
		public String sortedHeaderDescending() {
			return style.dataGridSortedHeaderDescending();
		}

		@Override
		public String widget() {
			return style.dataGridWidget();
		}
	}

	private static final int DEFAULT_PAGESIZE = 50;
	private static Resources DEFAULT_RESOURCES;

	/**
	 * Create the default loading indicator using the loading image in the
	 * specified {@link Resources}.
	 * 
	 * @param resources the resources containing the loading image
	 * @return a widget loading indicator
	 */
	private static Widget createDefaultLoadingIndicator(Resources resources) {
		ImageResource loadingImg = resources.dataGridLoading();
		if (loadingImg == null) {
			return null;
		}
		Image image = new Image(loadingImg);
		image.getElement().getStyle().setMarginTop(30.0, Unit.PX);
		return image;
	}

	private static Resources getDefaultResources() {
		if (DEFAULT_RESOURCES == null) {
			DEFAULT_RESOURCES = GWT.create(Resources.class);
		}
		return DEFAULT_RESOURCES;
	}

	final TableWidget tableData;
	final TableWidget tableFooter;
	final TableWidget tableHeader;
	private final FlexTable emptyTableWidgetContainer;
	private final HeaderPanel headerPanel;
	private final FlexTable loadingIndicatorContainer;
	private final Style style;
	private final Element tableDataContainer;
	private final ScrollPanel tableDataScroller;
	private final SimplePanel tableFooterContainer;
	private final Element tableFooterScroller;
	private final SimplePanel tableHeaderContainer;
	private final Element tableHeaderScroller;
	private Map<Column<T, ?>, Double> gridColumnWidths = new HashMap<Column<T, ?>, Double>();
	private List<Column<T, ?>> fixedWidthColumns = new ArrayList<Column<T, ?>>();
	private Element measuringElement;
	private boolean autoAdjust = false;
	private LoadingState previousLoadingState;
	private boolean lastColumnSpan = false;
	private Column<T, ?> lastColumn;

	/**
	 * Constructs a table with a default page size of 50.
	 */
	public PatchedDataGrid() {
		this(DEFAULT_PAGESIZE);
	}

	/**
	 * Constructs a table with the given page size.
	 * 
	 * @param pageSize the page size
	 */
	public PatchedDataGrid(final int pageSize) {
		this(pageSize, getDefaultResources());
	}

	/**
	 * Constructs a table with the given page size and the given
	 * {@link ProvidesKey key provider}.
	 * 
	 * @param pageSize the page size
	 * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	 *          object should act as its own key
	 */
	public PatchedDataGrid(int pageSize, ProvidesKey<T> keyProvider) {
		this(pageSize, getDefaultResources(), keyProvider);
	}

	/**
	 * Constructs a table with the given page size with the specified
	 * {@link Resources}.
	 * 
	 * @param pageSize the page size
	 * @param resources the resources to use for this widget
	 */
	public PatchedDataGrid(int pageSize, Resources resources) {
		this(pageSize, resources, null);
	}

	/**
	 * Constructs a table with the given page size, the specified
	 * {@link Resources}, and the given key provider.
	 * 
	 * @param pageSize the page size
	 * @param resources the resources to use for this widget
	 * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	 *          object should act as its own key
	 */
	public PatchedDataGrid(int pageSize, Resources resources, ProvidesKey<T> keyProvider) {
		this(pageSize, resources, keyProvider, createDefaultLoadingIndicator(resources));
	}

	/**
	 * Constructs a table with the given page size, the specified
	 * {@link Resources}, and the given key provider.
	 * 
	 * @param pageSize the page size
	 * @param resources the resources to use for this widget
	 * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	 *          object should act as its own key
	 * @param loadingIndicator the widget to use as a loading indicator, or null
	 *          to disable
	 */
	public PatchedDataGrid(int pageSize, Resources resources, ProvidesKey<T> keyProvider,
			Widget loadingIndicator) {
		super(new HeaderPanel(), pageSize, new ResourcesAdapter(resources), keyProvider);
		headerPanel = (HeaderPanel) getWidget();

		// Inject the stylesheet.
		this.style = resources.dataGridStyle();
		this.style.ensureInjected();

		// Create the header and footer widgets..
		tableHeader = new TableWidget();
		tableHeader.section = tableHeader.tableElem.createTHead();
		tableFooter = new TableWidget();
		tableFooter.section = tableFooter.tableElem.createTFoot();

		/*
		 * Wrap the header and footer widgets in a div because we cannot set the
		 * min-width of a table element. We set the width/min-width of the div
		 * container instead.
		 */
		tableHeaderContainer = new SimplePanel(tableHeader);
		tableFooterContainer = new SimplePanel(tableFooter);

		/*
		 * Get the element that wraps the container so we can adjust its scroll
		 * position.
		 */
		headerPanel.setHeaderWidget(tableHeaderContainer);
		tableHeaderScroller = tableHeaderContainer.getElement().getParentElement();
		headerPanel.setFooterWidget(tableFooterContainer);
		tableFooterScroller = tableFooterContainer.getElement().getParentElement();

		/*
		 * Set overflow to hidden on the scrollable elements so we can change the
		 * scrollLeft position.
		 */
		tableHeaderScroller.getStyle().setOverflow(Overflow.HIDDEN);
		tableFooterScroller.getStyle().setOverflow(Overflow.HIDDEN);

		// Create the body.
		tableData = new TableWidget();
		if (tableData.tableElem.getTBodies().getLength() > 0) {
			tableData.section = tableData.tableElem.getTBodies().getItem(0);
		} else {
			tableData.section = Document.get().createTBodyElement();
			tableData.tableElem.appendChild(tableData.section);
		}
		tableDataScroller = new CustomScrollPanel(tableData);
		tableDataScroller.setHeight("100%");
		headerPanel.setContentWidget(tableDataScroller);
		tableDataContainer = tableData.getElement().getParentElement();

		/*
		 * CustomScrollPanel applies the inline block style to the container
		 * element, but we want the container to fill the available width.
		 */
		tableDataContainer.getStyle().setDisplay(Display.BLOCK);

		/*
		 * Create the containers for the empty table message and loading indicator.
		 * The containers are centered tables that contain one cell, which aligns
		 * the widget in the center of the panel.
		 */
		emptyTableWidgetContainer = new FlexTable();
		emptyTableWidgetContainer.getElement().setAttribute("align", "center");
		emptyTableWidgetContainer.setWidth("100%");
		emptyTableWidgetContainer.setHeight("100%");
		loadingIndicatorContainer = new FlexTable();
		loadingIndicatorContainer.getElement().setAttribute("align", "center");
		loadingIndicatorContainer.setWidth("100%");
		loadingIndicatorContainer.setHeight("100%");

		// Set the loading indicator.
		setLoadingIndicator(loadingIndicator); // Can be null.

		// Synchronize the scroll positions of the three tables.
		tableDataScroller.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				int scrollLeft = tableDataScroller.getHorizontalScrollPosition();
				tableHeaderScroller.setScrollLeft(scrollLeft);
				tableFooterScroller.setScrollLeft(scrollLeft);
			}
		});
	}

	/**
	 * Constructs a table with a default page size of 50, and the given
	 * {@link ProvidesKey key provider}.
	 * 
	 * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	 *          object should act as its own key
	 */
	public PatchedDataGrid(ProvidesKey<T> keyProvider) {
		this(DEFAULT_PAGESIZE, keyProvider);
	}

	public ScrollPanel getScrollPanel() {
		HeaderPanel header = (HeaderPanel) getWidget();
		return (ScrollPanel) header.getContentWidget();
	}

	/**
	 * @return the autoAdjust
	 */
	public boolean isAutoAdjust() {
		return autoAdjust;
	}

	/**
	 * @param autoAdjust the autoAdjust to set
	 */
	public void setAutoAdjust(boolean autoAdjust) {
		this.autoAdjust = autoAdjust;
	}

	public void setLastColumnSpan(boolean lastColumnSpan) {
		this.lastColumnSpan = lastColumnSpan;
	}

	public void addFixedWidthColumn(Column<T, ?> col, Header<?> header, double width) {
		fixedWidthColumns.add(col);
		gridColumnWidths.put(col, width);
		addColumn(col, header);
	}

	public void insertColumn(int beforeIndex, Column<T, ?> col, Header<?> header, Header<?> footer) {
		//Log.trace("insertColumn "+header.getValue());

		super.insertColumn(beforeIndex, col, header, footer);
		if (autoAdjust) {
			double width = getColumnSize(col);
			setColumnWidth(col, width, Unit.PX);
			gridColumnWidths.put(col, width);
			if (lastColumnSpan) resetLastColumnSize();
			updateTableWidth();
		}
	}

	public void addColumns(List<Column<T, ?>> cols, List<Header<?>> headers) {
		//Log.trace("insertColumn "+header.getValue());

		for (int i = 0; i<cols.size(); i++) {
			Column<T, ?> col = cols.get(i);
			Header<?> header = headers.get(i);
			super.insertColumn(getColumnCount(), col, header, null);

			if (autoAdjust) {
				double width = getColumnSize(col);
				setColumnWidth(col, width, Unit.PX);
				gridColumnWidths.put(col, width);
			}
		}

		if (autoAdjust) {
			if (lastColumnSpan) resetLastColumnSize();
			updateTableWidth();
		}
	}

	public void replaceColumns(List<Column<T, ?>> colsToRemove, List<Column<T, ?>> colsToAdd, List<Header<?>> headers) {
		//Log.trace("insertColumn "+header.getValue());
		
		showLoader();

		if (autoAdjust) {
			//we reset last column size to normal
			if (lastColumnSpan) resetLastColumnSize();

			Map<Column<T, ?>, Double> gridColumnWidthsCopy = new HashMap<Column<T, ?>, Double>(gridColumnWidths);

			for (Column<T, ?> col:colsToRemove) {
				//Log.trace("removing col "+col);
				gridColumnWidths.remove(col);
				fixedWidthColumns.remove(col);
				if (lastColumn == col) lastColumn = null;
				super.removeColumn(getColumnIndex(col));
			}

			for (int i = 0; i<colsToAdd.size(); i++) {
				Column<T, ?> col = colsToAdd.get(i);
				//Log.trace("adding col "+col);
				
				Header<?> header = headers.get(i);
				super.insertColumn(getColumnCount(), col, header, null);

				Double widthCache = gridColumnWidthsCopy.get(col);
				double width = widthCache!=null?widthCache:getColumnSize(col);
				//Log.trace("col width "+width);
				setColumnWidth(col, width, Unit.PX);
				gridColumnWidths.put(col, width);
			}

			updateTableWidth();

		} else {
			removeColumns(colsToRemove);
			addColumns(colsToAdd, headers);
		}
		
		hideLoader();
	}

	public void removeColumns(List<Column<T, ?>> cols) {
		Log.trace("removeColumns "+cols);

		for (Column<T, ?> col:cols) {
			if (autoAdjust) {
				Log.trace("removing form autosize");
				gridColumnWidths.remove(col);
				fixedWidthColumns.remove(col);
				if (lastColumn == col) lastColumn = null;
			}
			super.removeColumn(getColumnIndex(col));
		}

		if (autoAdjust) {
			if (lastColumnSpan) resetLastColumnSize();
			updateTableWidth();
		}
	}

	public void removeColumn(int index) {
		Log.trace("removeColumn "+index);
		if (autoAdjust) {
			//Log.trace("removing form autosize");
			Column<T, ?> column = getColumn(index);
			gridColumnWidths.remove(column);
			fixedWidthColumns.remove(column);
			if (lastColumn == column) lastColumn = null;
			if (lastColumnSpan) resetLastColumnSize();
		}

		super.removeColumn(index);
		if (autoAdjust) {
			updateTableWidth();
		}
	}

	private void resetLastColumnSize() {
		Log.trace("resetLastColumnSize lastColumn: "+lastColumn);
		if (lastColumn!=null) {
			double width = getRequiredSize(lastColumn);
			//Log.trace("width "+width);
			setColumnWidth(lastColumn, width, Unit.PX);
			gridColumnWidths.put(lastColumn, width);
		}
	}

	private double getColumnSize(Column<T, ?> col) {
		if (fixedWidthColumns.contains(col)) return gridColumnWidths.get(col);
		return getRequiredSize(col);
	}

	public void refreshColumnSizes() {
		Log.trace("refreshColumnSizes");

		for (int i = 0; i < getColumnCount(); i++) {
			Column<T, ?> column = getColumn(i);
			double width = getColumnSize(column);
			setColumnWidth(column, width, Unit.PX);
			gridColumnWidths.put(column, width);
		}

		updateTableWidth();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <D> double getRequiredSize(Column<T, D> column)
	{
		//Log.trace("getRequiredSize "+column);
		startMeasuring();
		int colIndex = getColumnIndex(column);
		Header header = getHeader(colIndex);
		double max = measureHeaderCell(header.getCell(), header.getValue(), header.getHeaderStyleNames());
		int absRow = 0;
		for (T t : getVisibleItems()) {
			D value = column.getValue(t);
			Cell<D> cell = (Cell<D>) column.getCell();
			Context context = new Context(absRow++, colIndex, getValueKey(t), absRow);
			double valueWidth = measureCell(cell, value, context);
			max = Math.max(valueWidth, max);
		}
		finishMeasuring();

		//Log.trace("col "+header.getValue()+" width "+max);

		return max;
	}

	protected <D> void setColWidth(Column<T, D> column, double width) {
		setColumnWidth(column, width, Unit.PX);
		gridColumnWidths.put(column, width);
	}

	private double columnsWidth() {
		double columnsWidth = 0;
		for (Double colWidth:gridColumnWidths.values()) {
			//Log.trace("colWidth "+colWidth);
			columnsWidth += colWidth;
		}
		return columnsWidth;
	}

	protected void updateTableWidth()
	{
		int widgetWidth = getTableWidth();
		Log.trace("widgetWidth: "+widgetWidth);
		
		if (widgetWidth<=0) {
			Log.trace("aborting table width setup");
			return;
		}
		
		if (lastColumnSpan) resetLastColumnSize();
		
		//Log.trace("updateTableWidth");
		double columnsWidth = columnsWidth();
		//Log.trace("TOTAL columns width: "+columnsWidth);

		if (columnsWidth<widgetWidth && lastColumnSpan) spanLastColumn(widgetWidth-columnsWidth);
		else lastColumn = null;

		double tableWidth = Math.max(columnsWidth, widgetWidth);
		//Log.trace("new tableWidth: "+tableWidth);

		setTableWidth(tableWidth, Unit.PX);
	}

	private void spanLastColumn(double span) {
		Log.trace("spanLastColumn");

		lastColumn = getColumn(getColumnCount()-1);
		Log.trace("lastColumn "+lastColumn);

		//FIXME -1px added as workaround
		double width = getColumnSize(lastColumn) + span - 1;

		setColumnWidth(lastColumn, width, Unit.PX);
		gridColumnWidths.put(lastColumn, width);
	}

	protected int getTableWidth() {
		return getElement().getOffsetWidth();
	}

	private void startMeasuring() {
		Document document = Document.get();
		measuringElement = document.createElement("div");
		measuringElement.getStyle().setPosition(Position.ABSOLUTE);
		measuringElement.getStyle().setLeft(-1000, Unit.PX);
		measuringElement.getStyle().setTop(-1000, Unit.PX);
		document.getBody().appendChild(measuringElement);
	}

	private <D> double measureHeaderCell(Cell<D> cell, D value, String headerStyle)
	{
		//FIXME 4px added as workaround
		Context context = new Context(0, 0, "");
		String style = getResources().style().header() + " "+ headerStyle;
		return 4 + measureCell(cell, value, context, style);
	}

	private <D> double measureCell(Cell<D> cell, D value, Context context)
	{
		return measureCell(cell, value, context, getResources().style().cell());
	}

	private <D> double measureCell(Cell<D> cell, D value, Context context, String style)
	{
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		cell.render(context, value, sb);
		String valueHTML = "<div class=\""+style+"\" >"+sb.toSafeHtml().asString()+"</div>";//style=\"display: table-cell;\"
		//System.out.println("cell HTML: "+valueHTML);
		return measureText(valueHTML);
	}

	private double measureText(String text) {
		measuringElement.setInnerHTML(text);
		return measuringElement.getOffsetWidth();
	}

	private void finishMeasuring() {
		Document.get().getBody().removeChild(measuringElement);
	}

	@Override
	public void addColumnStyleName(int index, String styleName) {
		tableHeader.addColumnStyleName(index, styleName);
		tableFooter.addColumnStyleName(index, styleName);
		tableData.addColumnStyleName(index, styleName);
	}

	/**
	 * Clear the width of the tables in this widget, which causes them to fill the
	 * available width.
	 * 
	 * <p>
	 * The table width is not the same as the width of this widget. If the tables
	 * are narrower than this widget, there will be a gap between the table and
	 * the edge of the widget. If the tables are wider than this widget, a
	 * horizontal scrollbar will appear so the user can scroll horizontally.
	 * </p>
	 * 
	 * @see #setMinimumTableWidth(double, Unit)
	 * @see #setTableWidth(double, Unit)
	 */
	public void clearTableWidth() {
		tableHeaderContainer.getElement().getStyle().clearWidth();
		tableFooterContainer.getElement().getStyle().clearWidth();
		tableDataContainer.getStyle().clearWidth();
	}

	@Override
	public void onResize() {
		Log.trace("onResize");
		headerPanel.onResize();
		if (autoAdjust) updateTableWidth();
	}

	@Override
	public void removeColumnStyleName(int index, String styleName) {
		tableHeader.removeColumnStyleName(index, styleName);
		tableFooter.removeColumnStyleName(index, styleName);
		tableData.removeColumnStyleName(index, styleName);
	}

	@Override
	public void setEmptyTableWidget(Widget widget) {
		emptyTableWidgetContainer.setWidget(0, 0, widget);
		emptyTableWidgetContainer.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		super.setEmptyTableWidget(widget);
	}

	@Override
	public void setLoadingIndicator(Widget widget) {
		loadingIndicatorContainer.setWidget(0, 0, widget);
		loadingIndicatorContainer.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		super.setLoadingIndicator(widget);
	}

	/**
	 * Set the minimum width of the tables in this widget. If the widget become
	 * narrower than the minimum width, a horizontal scrollbar will appear so the
	 * user can scroll horizontally.
	 * 
	 * <p>
	 * Note that this method is not supported in IE6 and earlier versions of IE.
	 * </p>
	 * 
	 * @param value the width
	 * @param unit the unit of the width
	 * @see #setTableWidth(double, Unit)
	 */
	public void setMinimumTableWidth(double value, Unit unit) {
		/*
		 * The min-width style attribute doesn't apply to tables, so we set the
		 * min-width of the element that contains the table instead. The table width
		 * is fixed at 100%.
		 */
		tableHeaderContainer.getElement().getStyle().setProperty("minWidth", value, unit);
		tableFooterContainer.getElement().getStyle().setProperty("minWidth", value, unit);
		tableDataContainer.getStyle().setProperty("minWidth", value, unit);
	}

	/**
	 * Set the width of the tables in this widget. By default, the width is not
	 * set and the tables take the available width.
	 * 
	 * <p>
	 * The table width is not the same as the width of this widget. If the tables
	 * are narrower than this widget, there will be a gap between the table and
	 * the edge of the widget. If the tables are wider than this widget, a
	 * horizontal scrollbar will appear so the user can scroll horizontally.
	 * </p>
	 * 
	 * <p>
	 * If your table has many columns and you want to ensure that the columns are
	 * not truncated, you probably want to use
	 * {@link #setMinimumTableWidth(double, Unit)} instead. That will ensure that
	 * the table is wide enough, but it will still allow the table to expand to
	 * 100% if the user has a wide screen.
	 * </p>
	 * 
	 * <p>
	 * Note that setting the width in percentages will not work on older versions
	 * of IE because it does not account for scrollbars when calculating the
	 * width.
	 * </p>
	 * 
	 * @param value the width
	 * @param unit the unit of the width
	 * @see #setMinimumTableWidth(double, Unit)
	 */
	public void setTableWidth(double value, Unit unit) {
		/*
		 * The min-width style attribute doesn't apply to tables, so we set the
		 * min-width of the element that contains the table instead. For
		 * consistency, we set the width of the container as well.
		 */
		tableHeaderContainer.getElement().getStyle().setWidth(value, unit);
		tableFooterContainer.getElement().getStyle().setWidth(value, unit);
		tableDataContainer.getStyle().setWidth(value, unit);
	}

	public void adjustTableWidth()
	{
		int width = 0;
		for (int i = 0; i < getColumnCount(); i++) {
			int colWidth = tableHeader.ensureTableColElement(i).getOffsetWidth();
			Log.trace("col "+i+" width "+getColumnWidth(i));
			width += colWidth;
		}
		setTableWidth(width, Unit.PX);
	}

	@Override
	protected void doSetColumnWidth(int column, String width) {
		/* tableData.unhideColumn(column);
	  tableHeader.unhideColumn(column);
	  tableFooter.unhideColumn(column);*/
		if (width == null) {
			tableData.ensureTableColElement(column).getStyle().clearWidth();
			tableHeader.ensureTableColElement(column).getStyle().clearWidth();
			tableFooter.ensureTableColElement(column).getStyle().clearWidth();
		} else {
			tableData.ensureTableColElement(column).getStyle().setProperty("width", width);
			tableHeader.ensureTableColElement(column).getStyle().setProperty("width", width);
			tableFooter.ensureTableColElement(column).getStyle().setProperty("width", width);
		}
	}

	@Override
	protected void doSetHeaderVisible(boolean isFooter, boolean isVisible) {
		if (isFooter) {
			headerPanel.setFooterWidget(isVisible ? tableFooterContainer : null);
		} else {
			headerPanel.setHeaderWidget(isVisible ? tableHeaderContainer : null);
		}
	}

	@Override
	protected TableSectionElement getTableBodyElement() {
		return tableData.section;
	}

	@Override
	protected TableSectionElement getTableFootElement() {
		return tableFooter.section;
	}

	@Override
	protected TableSectionElement getTableHeadElement() {
		return tableHeader.section;
	}

	/**
	 * Called when the loading state changes.
	 * 
	 * @param state the new loading state
	 */
	@Override
	protected void onLoadingStateChanged(LoadingState state) {
		//Log.trace("onLoadingStateChanged state: "+state.getClass().getName()+" LOADING? "+(state==LoadingState.LOADING)+" LOADED? "+(state==LoadingState.LOADED));

		Widget message = tableData;
		if (state == LoadingState.LOADING) {
			// Loading indicator.
			message = loadingIndicatorContainer;
		} else if (state == LoadingState.LOADED && getPresenter().isEmpty()) {
			// Empty table.
			message = emptyTableWidgetContainer;
		}

		if (autoAdjust && state == LoadingState.LOADED && previousLoadingState == LoadingState.LOADING) refreshColumnSizes();
		previousLoadingState = state;

		// Switch out the message to display.
		tableDataScroller.setWidget(message);

		// Fire an event.
		super.onLoadingStateChanged(state);
	}

	@Override
	protected void refreshColumnWidths() {
		super.refreshColumnWidths();

		// Hide unused col elements in the colgroup.
		int columnCount = getRealColumnCount();
		tableHeader.hideUnusedColumns(columnCount);
		tableData.hideUnusedColumns(columnCount);
		tableFooter.hideUnusedColumns(columnCount);
	}

	public void showLoader() {
		tableDataScroller.setWidget(loadingIndicatorContainer);
	}

	public void hideLoader() {
		tableDataScroller.setWidget(tableData);
	}
}
