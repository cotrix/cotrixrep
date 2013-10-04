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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.event.RowSelectedEvent;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
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
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListAttributesPanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodeListAttributesPanel> { }

	interface DataGridResources extends DataGrid.Resources {

		@Source("CodeListAttributesPanel.css")
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
	DataGrid<UIAttribute> dataGrid;

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 
	protected DataGridResources resource = GWT.create(DataGridResources.class);

	private final Set<String> showExpanded = new HashSet<String>();

	protected Set<String> attributeAsColumn = new HashSet<String>();
	protected Map<String, Column<UIAttribute, String>> attributesColumns = new HashMap<String, Column<UIAttribute,String>>(); 

	protected Column<UIAttribute, Boolean> expandAttributePropertiesColumn;
	protected Column<UIAttribute, Boolean> switchColumn; 
	protected Column<UIAttribute, String> attributeNameColumn;
	protected Column<UIAttribute, String> attributeValueColumn;

	protected EventBus editorBus;

	protected ListDataProvider<UIAttribute> dataProvider;
	
	public CodeListAttributesPanel(EventBus editorBus) {
		
		this.editorBus = editorBus;

		dataGrid = new DataGrid<UIAttribute>(20, resource);
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label("Empty"));

		setupColumns();

		// Add a selection model so we can select cells.
		final SelectionModel<UIAttribute> selectionModel = new SingleSelectionModel<UIAttribute>();
		dataGrid.setSelectionModel(selectionModel);

		// Specify a custom table.
		dataGrid.setTableBuilder(new CustomTableBuilder());
		
		dataProvider = new ListDataProvider<UIAttribute>();
		dataProvider.addDataDisplay(dataGrid);
		
		bind();

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	protected void bind()
	{
		editorBus.addHandler(RowSelectedEvent.TYPE, new RowSelectedEvent.RowSelectedHandler() {
			
			@Override
			public void onRowSelected(RowSelectedEvent event) {
				List<UIAttribute> attributes = dataProvider.getList();
				attributes.clear();
				attributes.addAll(event.getRow().getAttributes());
				dataProvider.refresh();
			}
		});
	}

	private void setupColumns() {
		
		
		TextHeader header = new TextHeader("Attributes");

		SafeHtmlRenderer<Boolean> expansionRenderer = new AbstractSafeHtmlRenderer<Boolean>() {
			private BasicResources resources = GWT.create(BasicResources.class);

			@Override
			public SafeHtml render(Boolean object) {
				if (object != null) return renderer.render(object?resources.cellTreeOpenItem():resources.cellTreeClosedItem());
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				return sb.toSafeHtml();
			}
		};

		expandAttributePropertiesColumn = new Column<UIAttribute, Boolean>(new ImageResourceCell(expansionRenderer)) {

			@Override
			public Boolean getValue(UIAttribute object) {
				if (object==null) return false; 
				return showExpanded.contains(object.getId());
			}
		};
		expandAttributePropertiesColumn.setFieldUpdater(new FieldUpdater<UIAttribute, Boolean>() {
			@Override
			public void update(int index, UIAttribute object, Boolean value) {

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
		dataGrid.addColumn(expandAttributePropertiesColumn, header);
		dataGrid.setColumnWidth(0, 35, Unit.PX);
		
		SafeHtmlRenderer<Boolean> switchRenderer = new AbstractSafeHtmlRenderer<Boolean>() {

			@Override
			public SafeHtml render(Boolean object) {
				if (object != null) return renderer.render(object?CotrixManagerResources.INSTANCE.table():CotrixManagerResources.INSTANCE.tableDisabled());
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				return sb.toSafeHtml();
			}
		};
		

		switchColumn = new Column<UIAttribute, Boolean>(new ImageResourceCell(switchRenderer)) {

			@Override
			public Boolean getValue(UIAttribute object) {
				return !attributeAsColumn.contains(object.getName());
			}
		};

		switchColumn.setFieldUpdater(new FieldUpdater<UIAttribute, Boolean>() {
			@Override
			public void update(int index, UIAttribute object, Boolean value) {
				switchAttribute(object);
				// Redraw the modified row.
				dataGrid.redrawRow(index);
			}
		});
		dataGrid.addColumn(switchColumn, header);
		dataGrid.setColumnWidth(1, 35, Unit.PX);
		
		
		attributeNameColumn = new Column<UIAttribute, String>(new TextCell()) {
			@Override
			public String getValue(UIAttribute object) {
				return object.getName();
			}
		};
		
		dataGrid.addColumn(attributeNameColumn, header);
		
		attributeValueColumn = new Column<UIAttribute, String>(new TextCell()) {
			@Override
			public String getValue(UIAttribute object) {
				return object.getValue();
			}
		};
		dataGrid.addColumn(attributeValueColumn, header);
	}

	protected Column<UIAttribute, String> getAttributeColumn(final String name)
	{
		Column<UIAttribute, String> column = attributesColumns.get(name);
		if (column == null) {
			TextCell cell = new TextCell();
			column = new Column<UIAttribute, String>(cell) {

				@Override
				public String getValue(UIAttribute attribute) {
					/*if (object == null) return null;
					UIAttribute attribute = object.getAttribute(name);*/
					if (attribute == null) return null;
					return attribute.getValue();
				}
			};
			attributesColumns.put(name, column);
		}
		return column;
	}

	protected void switchAttribute(final UIAttribute attribute)
	{
		
	}
	
	protected void switchToAttribute(String attributeName)
	{
		Column<UIAttribute, String> column = getAttributeColumn(attributeName);
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

	private class CustomTableBuilder extends AbstractCellTableBuilder<UIAttribute> {

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
		public void buildRowImpl(UIAttribute rowValue, int absRowIndex) {
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

		protected void buildPropertiesTable(TableBuilder table, UIAttribute rowValue, int absRowIndex)
		{
			Log.trace("buildPropertiesTable for row "+rowValue.getId());

			table.style().borderStyle(BorderStyle.SOLID).borderWidth(1, Unit.PX).trustedBorderColor("#8C8C8C")
			.trustedProperty("border-collapse", "collapse").width(200, Unit.PX);

			BodyBuilder body = table.startBody();

			//for (UIAttribute attribute:rowValue.getAttributes()) {
			UIAttribute attribute = rowValue;
				//if (attributeAsColumn.contains(attribute.getName())) continue;

				TableRowBuilder tr = body.startTR();

				/*Column<UIAttribute, String> switchColumn = getSwitchColumn(attribute.getName());
				renderCell(tr, absRowIndex, switchColumn, rowValue);*/
				
				addCell(tr, "Type");
				addCell(tr, attribute.getType());

				/*Column<UIAttribute, String> propColumn = getAttributeColumn(attribute.getName());
				renderCell(tr, absRowIndex, propColumn, rowValue);*/

				tr.end();
				
				tr = body.startTR();

				/*Column<UIAttribute, String> switchColumn = getSwitchColumn(attribute.getName());
				renderCell(tr, absRowIndex, switchColumn, rowValue);*/
				
				addCell(tr, "Language");
				addCell(tr, attribute.getLanguage());


				/*Column<UIAttribute, String> propColumn = getAttributeColumn(attribute.getName());
				renderCell(tr, absRowIndex, propColumn, rowValue);*/

				tr.end();
				
				
			//}
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
		
		protected void renderCell(TableRowBuilder tr, int absRowIndex, Column<UIAttribute, String> column, UIAttribute rowValue)
		{
			TableCellBuilder td = tr.startTD();
			td.style().borderStyle(BorderStyle.SOLID).borderWidth(1, Unit.PX).trustedBorderColor("#C2C2C2")
			.paddingBottom(8, Unit.PX).paddingLeft(10, Unit.PX).paddingRight(8, Unit.PX).paddingTop(8, Unit.PX);

			Context context = new Context(absRowIndex, -1, "key");
			renderCell(td, context, column, rowValue);

			td.end();
		}

		public void buildStandarRow(UIAttribute rowValue, int absRowIndex) {

			// Calculate the row styles.
			SelectionModel<? super UIAttribute> selectionModel = cellTable.getSelectionModel();
			boolean isSelected =
					(selectionModel == null || rowValue == null) ? false : selectionModel.isSelected(rowValue);
			boolean isEven = absRowIndex % 2 == 0;
			StringBuilder trClasses = new StringBuilder(isEven ? evenRowStyle : oddRowStyle);
			if (isSelected) {
				trClasses.append(selectedRowStyle);
			}

			// Add custom row styles.
			RowStyles<UIAttribute> rowStyles = cellTable.getRowStyles();
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
				Column<UIAttribute, ?> column = cellTable.getColumn(curColumn);
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
