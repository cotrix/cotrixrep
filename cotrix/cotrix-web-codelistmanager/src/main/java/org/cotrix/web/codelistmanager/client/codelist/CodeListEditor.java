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
package org.cotrix.web.codelistmanager.client.codelist;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.event.RowSelectedEvent;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;
import org.cotrix.web.share.shared.UIAttribute;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.BodyBuilder;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.TableBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.CellTree.BasicResources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListEditor extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodeListEditor> { }

	interface DataGridResources extends DataGrid.Resources {

		@Source("CodeListEditor.css")
		DataGridStyle dataGridStyle();
		
		@Source("plus.png")
		ImageResource add();
		
		@Source("minus.png")
		ImageResource remove();
	}

	interface DataGridStyle extends DataGrid.Style {

		String groupHeaderCell();
	}


	@UiField(provided = true)
	DataGrid<UICodeListRow> dataGrid;

	@UiField(provided = true)
	SimplePager pager;

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 
	protected DataGridResources resource = GWT.create(DataGridResources.class);

	private final Set<String> showExpanded = new HashSet<String>();

	protected Set<String> attributeAsColumn = new HashSet<String>();
	protected Map<String, Column<UICodeListRow, String>> attributesColumns = new HashMap<String, Column<UICodeListRow,String>>(); 
	protected Map<String, Column<UICodeListRow, String>> switchesColumns = new HashMap<String, Column<UICodeListRow,String>>(); 

//	private Column<UICodeListRow, String> codeColumn;
	private Column<UICodeListRow, String> nameColumn;
	private Column<UICodeListRow, Boolean> expandAttributesColumn;
	
	protected EventBus editorBus;

	protected SingleSelectionModel<UICodeListRow> selectionModel;

	//TODO use injection
	public CodeListEditor(EventBus editorBus) {
		this.editorBus = editorBus;

		dataGrid = new DataGrid<UICodeListRow>(20, resource, CodeListRowKeyProvider.INSTANCE);
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label("Empty"));

		//TODO add sorting

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(dataGrid);

		setupColumns();


		selectionModel = new SingleSelectionModel<UICodeListRow>(CodeListRowKeyProvider.INSTANCE);
		dataGrid.setSelectionModel(selectionModel);

		// Specify a custom table.
		//dataGrid.setTableBuilder(new CustomTableBuilder());
		
		bind();

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	protected void bind()
	{
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				UICodeListRow row = selectionModel.getSelectedObject();
				if (row !=null) editorBus.fireEvent(new RowSelectedEvent(row));
			}
		});
	}

	public void setDataProvider(CodeListRowDataProvider dataProvider)
	{
		dataProvider.addDataDisplay(dataGrid);
	}

	private void setupColumns() {

		/*SafeHtmlRenderer<Boolean> expansionRenderer = new AbstractSafeHtmlRenderer<Boolean>() {
			private BasicResources resources = GWT.create(BasicResources.class);

			@Override
			public SafeHtml render(Boolean object) {
				if (object != null) return renderer.render(object?resources.cellTreeOpenItem():resources.cellTreeClosedItem());
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				return sb.toSafeHtml();
			}
		};

		expandAttributesColumn = new Column<UICodeListRow, Boolean>(new ImageResourceCell(expansionRenderer)) {

			@Override
			public Boolean getValue(UICodeListRow object) {
				if (object==null) return false; 
				return showExpanded.contains(object.getId());
			}
		};
		expandAttributesColumn.setFieldUpdater(new FieldUpdater<UICodeListRow, Boolean>() {
			@Override
			public void update(int index, UICodeListRow object, Boolean value) {

				Log.trace("expand "+index+" "+object.getId());

				if (showExpanded.contains(object.getId())) {
					showExpanded.remove(object.getId());
				} else {
					showExpanded.add(object.getId());
				}

				// Redraw the modified row.
				dataGrid.redrawRow(index);
			}
		});
		dataGrid.addColumn(expandAttributesColumn);
		dataGrid.setColumnWidth(0, 35, Unit.PX);*/

	/*	codeColumn = new Column<UICodeListRow, String>(new TextCell()) {
			@Override
			public String getValue(UICodeListRow object) {
				return object.getCode();
			}
		};

		dataGrid.addColumn(codeColumn, "Code");*/
		//dataGrid.setColumnWidth(1, 2000, Unit.PX);

		nameColumn = new Column<UICodeListRow, String>(new TextCell()) {
			@Override
			public String getValue(UICodeListRow object) {
				return object.getName();
			}
		};

		dataGrid.addColumn(nameColumn, "Code");
		//dataGrid.setColumnWidth(2, 1000, Unit.PX);
	}

	protected Column<UICodeListRow, String> getAttributeColumn(final String name)
	{
		Column<UICodeListRow, String> column = attributesColumns.get(name);
		if (column == null) {
			TextCell cell = new TextCell();
			column = new Column<UICodeListRow, String>(cell) {

				@Override
				public String getValue(UICodeListRow object) {
					if (object == null) return null;
					UIAttribute attribute = object.getAttribute(name);
					if (attribute == null) return null;
					return attribute.getValue();
				}
			};
			attributesColumns.put(name, column);
		}
		return column;
	}

	protected void switchToColumn(final String attributeName)
	{
		Column<UICodeListRow, String> column = getAttributeColumn(attributeName);
		attributeAsColumn.add(attributeName);
		
		SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
			@Override
			public SafeHtml render(String object) {
				 SafeHtmlBuilder sb = new SafeHtmlBuilder();
			     sb.appendEscaped(attributeName);
				 renderer.render(resource.remove(), sb);
			     return sb.toSafeHtml();
			}
		};
		
		Header<String> header = new Header<String>(new ClickableTextCell(anchorRenderer)) {
			
			@Override
			public String getValue() {
				return attributeName;
			}
		};
		header.setUpdater(new ValueUpdater<String>() {

			@Override
			public void update(String value) {
				switchToAttribute(attributeName);				
			}
		});
		
		//ResizableHeader<UICodeListRow> resizableHeader = new ResizableHeader<UICodeListRow>(attributeName, dataGrid, column);
		dataGrid.addColumn(column, header);
		//dataGrid.setColumnWidth(dataGrid.getColumnCount()-1, 1, Unit.EM);
		//dataGrid.clearTableWidth();
	}
	
	protected void switchToAttribute(String attributeName)
	{
		Column<UICodeListRow, String> column = getAttributeColumn(attributeName);
		attributeAsColumn.remove(attributeName);
		dataGrid.removeColumn(column);
		removeUnusedDataGridColumns(dataGrid);
	}
	
	/**
	 * Workaround issue #6711
	 * https://code.google.com/p/google-web-toolkit/issues/detail?id=6711
	 * @param dataGrid
	 */
	public static void removeUnusedDataGridColumns(DataGrid<?> dataGrid) {
		int columnCount = dataGrid.getColumnCount();
		NodeList<Element> colGroups = dataGrid.getElement().getElementsByTagName("colgroup");

		for (int i = 0; i < colGroups.getLength(); i++) {
			Element colGroupEle = colGroups.getItem(i);
			NodeList<Element> colList = colGroupEle.getElementsByTagName("col");

			for (int j = colList.getLength()-1; j >= columnCount; j--) {
				colGroupEle.removeChild(colList.getItem(j));
			}
		}
	}

	protected Column<UICodeListRow, String> getSwitchColumn(final String name)
	{
		Column<UICodeListRow, String> column = switchesColumns.get(name);
		if (column == null) {
			SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
				@Override
				public SafeHtml render(String object) {
					return renderer.render(resource.add());
				}
			};
			column = new Column<UICodeListRow, String>(new ClickableTextCell(anchorRenderer)){

				@Override
				public String getValue(UICodeListRow object) {
					return name;
				}
			};

			column.setFieldUpdater(new FieldUpdater<UICodeListRow, String>() {
				@Override
				public void update(int index, UICodeListRow object, String value) {
					switchToColumn(name);
					// Redraw the modified row.
					dataGrid.redrawRow(index);
				}
			});
			/*TextCell cell = new TextCell();
			column = new Column<UICodeListRow, String>(cell) {

				@Override
				public String getValue(UICodeListRow object) {
					if (object == null) return null;
					UIAttribute attribute = object.getAttribute(name);
					if (attribute == null) return null;
					return attribute.getValue();
				}
			};*/
			switchesColumns.put(name, column);
		}
		return column;
	}


	private class CustomTableBuilder extends AbstractCellTableBuilder<UICodeListRow> {

		private final String evenRowStyle;
		private final String oddRowStyle;
		private final String selectedRowStyle;
		private final String cellStyle;
		private final String evenCellStyle;
		private final String oddCellStyle;
		private final String selectedCellStyle;
		private final String firstColumnStyle;
		private final String lastColumnStyle;

		public CustomTableBuilder() {
			super(dataGrid);

			// Cache styles for faster access.
			Style style = dataGrid.getResources().style();
			evenRowStyle = style.evenRow();
			oddRowStyle = style.oddRow();
			selectedRowStyle = " " + style.selectedRow();
			cellStyle = style.cell() + " " + style.evenRowCell();
			selectedCellStyle = " " + style.selectedRowCell();
			evenCellStyle = " " + style.evenRowCell();
			oddCellStyle = " " + style.oddRowCell();
			firstColumnStyle = " " + style.firstColumn();
			lastColumnStyle = " " + style.lastColumn();
		}



		@Override
		public void buildRowImpl(UICodeListRow rowValue, int absRowIndex) {
			buildStandarRow(rowValue, absRowIndex);

			if (showExpanded.contains(rowValue.getId())) {

				TableRowBuilder row = startRow();
				TableCellBuilder td = row.startTD().colSpan(dataGrid.getColumnCount()).className(resource.dataGridStyle().dataGridCell());
				td.style().trustedBackgroundColor("#efefef").endStyle();

				TableBuilder table = td.startTable();
				buildPropertiesTable(table, rowValue, absRowIndex);

				td.end();
				row.endTR();
			}
		}

		protected void buildPropertiesTable(TableBuilder table, UICodeListRow rowValue, int absRowIndex)
		{
			Log.trace("buildPropertiesTable for row "+rowValue.getId());

			table.style().borderStyle(BorderStyle.SOLID).borderWidth(1, Unit.PX).trustedBorderColor("#8C8C8C")
			.trustedProperty("border-collapse", "collapse").width(200, Unit.PX);

			BodyBuilder body = table.startBody();

			for (UIAttribute attribute:rowValue.getAttributes()) {

				if (attributeAsColumn.contains(attribute.getName())) continue;

				TableRowBuilder tr = body.startTR();

				Column<UICodeListRow, String> switchColumn = getSwitchColumn(attribute.getName());
				renderCell(tr, absRowIndex, switchColumn, rowValue);
	
				addCell(tr, attribute.getName());
				addCell(tr, attribute.getType());
				addCell(tr, attribute.getLanguage());

				Column<UICodeListRow, String> propColumn = getAttributeColumn(attribute.getName());
				renderCell(tr, absRowIndex, propColumn, rowValue);

				tr.end();
			}
			body.end();
			table.end();
		}

		protected void addCell(TableRowBuilder tr, String cellValue)
		{
			cellValue = cellValue!=null?cellValue:"";

			TableCellBuilder td = tr.startTD();

			td.style().borderStyle(BorderStyle.SOLID).borderWidth(1, Unit.PX).trustedBorderColor("#C2C2C2")
			.paddingBottom(8, Unit.PX).paddingLeft(10, Unit.PX).paddingRight(8, Unit.PX).paddingTop(8, Unit.PX)
			.fontWeight(FontWeight.BOLD);
			td.startDiv().text(cellValue).end();
			td.end();
		}
		
		protected void renderCell(TableRowBuilder tr, int absRowIndex, Column<UICodeListRow, String> column, UICodeListRow rowValue)
		{
			TableCellBuilder td = tr.startTD();
			td.style().borderStyle(BorderStyle.SOLID).borderWidth(1, Unit.PX).trustedBorderColor("#C2C2C2")
			.paddingBottom(8, Unit.PX).paddingLeft(10, Unit.PX).paddingRight(8, Unit.PX).paddingTop(8, Unit.PX);

			Context context = new Context(absRowIndex, -1, "key");
			renderCell(td, context, column, rowValue);

			td.end();
		}

		public void buildStandarRow(UICodeListRow rowValue, int absRowIndex) {

			// Calculate the row styles.
			SelectionModel<? super UICodeListRow> selectionModel = cellTable.getSelectionModel();
			boolean isSelected =
					(selectionModel == null || rowValue == null) ? false : selectionModel.isSelected(rowValue);
			boolean isEven = absRowIndex % 2 == 0;
			StringBuilder trClasses = new StringBuilder(isEven ? evenRowStyle : oddRowStyle);
			if (isSelected) {
				trClasses.append(selectedRowStyle);
			}

			// Add custom row styles.
			RowStyles<UICodeListRow> rowStyles = cellTable.getRowStyles();
			if (rowStyles != null) {
				String extraRowStyles = rowStyles.getStyleNames(rowValue, absRowIndex);
				if (extraRowStyles != null) {
					trClasses.append(" ").append(extraRowStyles);
				}
			}

			// Build the row.
			TableRowBuilder tr = startRow();
			tr.className(trClasses.toString());

			// Build the columns.
			int columnCount = cellTable.getColumnCount();
			for (int curColumn = 0; curColumn < columnCount; curColumn++) {
				Column<UICodeListRow, ?> column = cellTable.getColumn(curColumn);
				// Create the cell styles.
				StringBuilder tdClasses = new StringBuilder(cellStyle);
				tdClasses.append(isEven ? evenCellStyle : oddCellStyle);
				if (curColumn == 0) {
					tdClasses.append(firstColumnStyle);
				}
				if (isSelected) {
					tdClasses.append(selectedCellStyle);
				}
				// The first and last column could be the same column.
				if (curColumn == columnCount - 1) {
					tdClasses.append(lastColumnStyle);
				}

				// Add class names specific to the cell.
				Context context = new Context(absRowIndex, curColumn, cellTable.getValueKey(rowValue));
				String cellStyles = column.getCellStyleNames(context, rowValue);
				if (cellStyles != null) {
					tdClasses.append(" " + cellStyles);
				}

				// Build the cell.
				HorizontalAlignmentConstant hAlign = column.getHorizontalAlignment();
				VerticalAlignmentConstant vAlign = column.getVerticalAlignment();
				TableCellBuilder td = tr.startTD();
				td.className(tdClasses.toString());
				if (hAlign != null) {
					td.align(hAlign.getTextAlignString());
				}
				if (vAlign != null) {
					td.vAlign(vAlign.getVerticalAlignString());
				}

				// Add the inner div.
				DivBuilder div = td.startDiv();
				div.style().outlineStyle(OutlineStyle.NONE).endStyle();

				// Render the cell into the div.
				renderCell(div, context, column, rowValue);

				// End the cell.
				div.endDiv();
				td.endTD();
			}

			// End the row.
			tr.endTR();
		}
	}


	/**
	 * An {@link AbstractCell} used to render an {@link ImageResource}.
	 */
	public class ImageResourceCell extends AbstractCell<Boolean> {
		private SafeHtmlRenderer<Boolean> renderer;

		/**
		 * Construct a new ImageResourceCell.
		 */
		public ImageResourceCell(SafeHtmlRenderer<Boolean> renderer) {
			super(CLICK, KEYDOWN);
			this.renderer = renderer;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void onBrowserEvent(Context context, Element parent, Boolean value,
				NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if (CLICK.equals(event.getType())) {
				System.out.println("CLICK EVENT vu: "+valueUpdater);
				onEnterKeyDown(context, parent, value, event, valueUpdater);
			}
		}

		@Override
		protected void onEnterKeyDown(Context context, Element parent, Boolean value,
				NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
			if (valueUpdater != null) {
				System.out.println("UPDATING");
				valueUpdater.update(value);
			}
		}

		@Override
		public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.append(renderer.render(value));
			}
		}

	}
}
