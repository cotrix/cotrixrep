package org.cotrix.web.manage.client.codelist.link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.EventUtil;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.DoubleClickEditTextCell;
import org.cotrix.web.common.client.widgets.EditableCell;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.StyledSafeHtmlRenderer;
import org.cotrix.web.common.client.widgets.SuggestBoxCell;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGridResources;
import org.cotrix.web.manage.client.codelist.link.CodelistSuggestOracle.CodelistSuggestion;
import org.cotrix.web.manage.client.codelist.link.LinkTypeChangedEvent.LinkTypeChangedHandler;
import org.cotrix.web.manage.client.codelist.link.LinkTypeChangedEvent.HasLinkTypeChangedHandlers;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.BodyBuilder;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.TableBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SuggestCell;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinksTypesGrid extends ResizeComposite implements HasLinkTypeChangedHandlers, HasEditing {

	PatchedDataGrid<UILinkType> dataGrid;

	protected static AttributesGridResources gridResource = GWT.create(AttributesGridResources.class);

	private final Set<String> showExpanded = new HashSet<String>();

	protected Map<String, EnumMap<LinkTypeField, Column<UILinkType, ?>>> linkstypesPropertiesColumns = new HashMap<String, EnumMap<LinkTypeField,Column<UILinkType,?>>>();
	protected List<EditableCell> editableCells = new ArrayList<EditableCell>();
	protected boolean editable = true;

	private Column<UILinkType, String> linktypeNameColumn;

	private ListDataProvider<UILinkType> dataProvider;

	private Header<String> header;

	protected SingleSelectionModel<UILinkType> selectionModel;

	protected StyledSafeHtmlRenderer cellRenderer;
	protected StyledSafeHtmlRenderer systemAttributeCell = new StyledSafeHtmlRenderer(CotrixManagerResources.INSTANCE.css().systemProperty());
	protected SuggestOracle codelistSuggestOracle;

	public LinksTypesGrid(ListDataProvider<UILinkType> dataProvider, Header<String> header, String emptyMessage, SuggestOracle codelistSuggestOracle) {

		this.dataProvider = dataProvider;
		this.header = header;
		this.codelistSuggestOracle = codelistSuggestOracle;

		cellRenderer = new StyledSafeHtmlRenderer(CotrixManagerResources.INSTANCE.propertyGrid().textValue());
		dataGrid = new PatchedDataGrid<UILinkType>(20, gridResource);

		//We need to listen dbclick events in order to enable editing
		EventUtil.sinkEvents(dataGrid, Collections.singleton(BrowserEvents.DBLCLICK));

		dataGrid.setAutoHeaderRefreshDisabled(true);
		Label emptyTableWidget = new Label(emptyMessage);
		emptyTableWidget.setStyleName(CotrixManagerResources.INSTANCE.propertyGrid().emptyTableWidget());
		dataGrid.setEmptyTableWidget(emptyTableWidget);

		setupColumns();

		selectionModel = new SingleSelectionModel<UILinkType>();
		dataGrid.setSelectionModel(selectionModel);

		// Specify a custom table.
		dataGrid.setTableBuilder(new CustomTableBuilder());

		dataProvider.addDataDisplay(dataGrid);

		initWidget(dataGrid);
	}

	private void setupColumns() {

		linktypeNameColumn = new Column<UILinkType, String>(new ClickableTextCell()) {
			@Override
			public String getValue(UILinkType object) {
				return object.getName().getLocalPart();
			}
		};

		linktypeNameColumn.setFieldUpdater(new FieldUpdater<UILinkType, String>() {
			@Override
			public void update(int index, UILinkType object, String value) {

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

		dataGrid.addColumn(linktypeNameColumn, header);
	}

	public UILinkType getSelectedLikeType()
	{
		return selectionModel.getSelectedObject();
	}
	
	public void addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
		selectionModel.addSelectionChangeHandler(handler);
	}

	public void setEditable(boolean editable)
	{
		this.editable = editable;
		for (EditableCell cell:editableCells) cell.setReadOnly(!editable);
	}
	
	protected AbstractEditableCell<String, ?> createDoubleClickEditTextCell(boolean isSystemAttribute)
	{
		String editorStyle = CommonResources.INSTANCE.css().textBox() + " " + CotrixManagerResources.INSTANCE.css().editor();
		DoubleClickEditTextCell cell = new DoubleClickEditTextCell(editorStyle, isSystemAttribute?systemAttributeCell:cellRenderer);
		if (!isSystemAttribute) {
			cell.setReadOnly(!editable);
			editableCells.add(cell);
		}
		return cell;
	}
	
	protected AbstractEditableCell<String, ?> createCodelistSuggestBoxCell(boolean isSystemAttribute)
	{
		
		String editorStyle = CommonResources.INSTANCE.css().textBox() + " " + CotrixManagerResources.INSTANCE.css().editor();
		
		SuggestBoxCell cell = new SuggestBoxCell(editorStyle, isSystemAttribute?systemAttributeCell:cellRenderer, codelistSuggestOracle);
		if (!isSystemAttribute) {
			cell.setReadOnly(!editable);
			editableCells.add(cell);
		}
		return cell;
	}
	

	protected AbstractEditableCell<String, ?> createCell(LinkTypeField field, boolean isSystemAttribute)
	{
		switch (field) {
			//case TARGET_CODELIST: return createCodelistSuggestBoxCell(isSystemAttribute);
			default: return createDoubleClickEditTextCell(isSystemAttribute);
		}
	}

	protected Column<UILinkType, ?> getAttributePropertyColumn(final String name, boolean isSystemAttribute, final LinkTypeField field)
	{
		EnumMap<LinkTypeField, Column<UILinkType, ?>> attributePropertiesColumns = linkstypesPropertiesColumns.get(name);
		if (attributePropertiesColumns == null) {
			attributePropertiesColumns = new EnumMap<LinkTypeField, Column<UILinkType,?>>(LinkTypeField.class);
			linkstypesPropertiesColumns.put(name, attributePropertiesColumns);
		}

		Column<UILinkType, ?> column = attributePropertiesColumns.get(field);
		if (column == null) {
			
			if (field == LinkTypeField.TARGET_CODELIST) {
				
				String editorStyle = CommonResources.INSTANCE.css().textBox() + " " + CotrixManagerResources.INSTANCE.css().editor();
				
				SuggestCell<CodelistSuggestOracle.CodelistSuggestion> cell = new SuggestCell<CodelistSuggestOracle.CodelistSuggestion>(editorStyle, codelistSuggestOracle);
				cell.setReadOnly(false);
				Column<UILinkType, CodelistSuggestOracle.CodelistSuggestion> codelistSuggestionColumn = new Column<UILinkType, CodelistSuggestOracle.CodelistSuggestion>(cell) {
					
					@Override
					public CodelistSuggestion getValue(UILinkType object) {
						return new CodelistSuggestion(object.getTargetCodelist());
					}
				};
				
				codelistSuggestionColumn.setFieldUpdater(new FieldUpdater<UILinkType, CodelistSuggestOracle.CodelistSuggestion>() {

					@Override
					public void update(int index, UILinkType object, CodelistSuggestion value) {
						if (value!=null) object.setTargetCodelist(value.getCodelist());
						else object.setTargetCodelist(null);
					}
				});
				
				column = codelistSuggestionColumn;
				
			} else {
			
			Column<UILinkType, String> fieldColumn = new Column<UILinkType, String>(createCell(field, isSystemAttribute)) {

				@Override
				public String getValue(UILinkType linkType) {
					if (linkType == null) return "";
					switch (field) {
						case NAME: return ValueUtils.getValue(linkType.getName());
						//case TARGET_CODELIST: return ValueUtils.getValue(linkType.getTargetCodelist().getName());
						case VALUE_FUNCTION: return linkType.getValueFunction();
						case VALUE_TYPE: return String.valueOf(linkType.getValueType());
						default: return "";
					}
				}
			};

			fieldColumn.setFieldUpdater(new FieldUpdater<UILinkType, String>() {

				@Override
				public void update(int index, UILinkType attribute, String value) {
					Log.trace("updated attribute "+attribute+" field: "+field+" value: "+value);
					switch (field) {
						case NAME: attribute.getName().setLocalPart(value); break;
						/*case LANGUAGE: attribute.setLanguage(value); break;
						case TYPE: attribute.getType().setLocalPart(value); break;
						case VALUE: attribute.setValue(value); break;*/
					}
					LinkTypeChangedEvent.fire(LinksTypesGrid.this, attribute);
				}
			});
			
			column = fieldColumn;
			
			}

			attributePropertiesColumns.put(field, column);
		}
		return column;
	}

	public void refreshAttribute(UILinkType attribute)
	{
		Log.trace("refreshAttribute attribute: "+attribute);
		List<UILinkType> attributes = dataProvider.getList();
		int index = attributes.indexOf(attribute);
		if (index>=0) {
			attributes.set(index, attribute);
			dataProvider.refresh();
		} else Log.warn("attribute "+attribute+" not found in data provider");
	}

	public void expand(UILinkType attribute)
	{
		showExpanded.add(attribute.getId());
		refreshAttribute(attribute);
	}

	private class CustomTableBuilder extends AbstractCellTableBuilder<UILinkType> {

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

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void buildRowImpl(UILinkType rowValue, int absRowIndex) {
			buildStandarRow(rowValue, absRowIndex);

			if (showExpanded.contains(rowValue.getId())) {

				TableRowBuilder row = startRow();
				TableCellBuilder td = row.startTD().colSpan(dataGrid.getColumnCount()).className(gridResource.dataGridStyle().dataGridCell());

				TableBuilder table = td.startTable();
				buildPropertiesTable(table, rowValue, absRowIndex);

				td.end();
				row.endTR();
			}
		}

		protected void buildPropertiesTable(TableBuilder table, UILinkType rowValue, int absRowIndex)
		{
			Log.trace("buildPropertiesTable for row "+rowValue.getId());

			table.className(CotrixManagerResources.INSTANCE.propertyGrid().table());

			BodyBuilder body = table.startBody();

			UILinkType attribute = rowValue;

			addRow(body, "Name", absRowIndex, rowValue, LinkTypeField.NAME);
			addRow(body, "Codelist", absRowIndex, rowValue, LinkTypeField.TARGET_CODELIST);

			/*addRow(body, "Type", ValueUtils.getValue(attribute.getType()), absRowIndex, rowValue, LinkTypeField.TYPE);

			addRow(body, "Language", attribute.getLanguage(), absRowIndex, rowValue, LinkTypeField.LANGUAGE);

			addRow(body, "Value", attribute.getValue(), absRowIndex, rowValue, LinkTypeField.VALUE);*/

			body.end();
			table.end();
		}

		protected void addRow(BodyBuilder body, String label, int absRowIndex, UILinkType attribute, LinkTypeField field)
		{
			TableRowBuilder tr = body.startTR();

			addCell(tr, label);

			boolean isSystemAttribute = false;//FIXME Attributes.isSystemAttribute(attribute);
			Column<UILinkType, ?> propColumn = getAttributePropertyColumn(attribute.getName().getLocalPart(), isSystemAttribute, field);
			renderCell(tr, absRowIndex, propColumn, attribute);

			tr.end();
		}

		protected void addCell(TableRowBuilder tr, String cellValue)
		{
			cellValue = cellValue!=null?cellValue:"";

			TableCellBuilder td = tr.startTD();
			td.className(CotrixManagerResources.INSTANCE.propertyGrid().header());
			td.startDiv().text(cellValue).end();
			td.end();
		}

		protected void renderCell(TableRowBuilder tr, int absRowIndex, Column<UILinkType, ?> column, UILinkType rowValue)
		{
			TableCellBuilder td = tr.startTD();
			td.className(CotrixManagerResources.INSTANCE.propertyGrid().value());

			Context context = new Context(absRowIndex, -1, "key");
			renderCell(td, context, column, rowValue);

			td.end();
		}

		public void buildStandarRow(UILinkType rowValue, int absRowIndex) {

			// Calculate the row styles.
			SelectionModel<? super UILinkType> selectionModel = cellTable.getSelectionModel();
			boolean isSelected =
					(selectionModel == null || rowValue == null) ? false : selectionModel.isSelected(rowValue);
			boolean isEven = absRowIndex % 2 == 0;
			StringBuilder trClasses = new StringBuilder(isEven ? evenRowStyle : oddRowStyle);
			if (isSelected) {
				trClasses.append(selectedRowStyle);
			}

			// Add custom row styles.
			RowStyles<UILinkType> rowStyles = cellTable.getRowStyles();
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
				Column<UILinkType, ?> column = cellTable.getColumn(curColumn);
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

	public void redrawHeaders() {
		dataGrid.redrawHeaders();		
	}

	public void insertColumn(int beforeIndex, Column<UILinkType, ?> col) {
		dataGrid.insertColumn(beforeIndex, col, header);
	}

	/**
	 * @param column
	 * @param width
	 * @param unit
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#setColumnWidth(int, double, com.google.gwt.dom.client.Style.Unit)
	 */
	public void setColumnWidth(int column, double width, Unit unit) {
		dataGrid.setColumnWidth(column, width, unit);
	}

	/**
	 * @param absRowIndex
	 * @see com.google.gwt.user.cellview.client.AbstractHasData#redrawRow(int)
	 */
	public void redrawRow(int absRowIndex) {
		dataGrid.redrawRow(absRowIndex);
	}

	/**
	 * @param col
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#removeColumn(com.google.gwt.user.cellview.client.Column)
	 */
	public void removeColumn(Column<UILinkType, ?> col) {
		dataGrid.removeColumn(col);
	}

	@Override
	public HandlerRegistration addLinkTypeChangedHandler(LinkTypeChangedHandler handler) {
		return addHandler(handler, LinkTypeChangedEvent.getType());
	}

	public void setSelectedAttribute(UILinkType linkType) {
		selectionModel.setSelected(linkType, true);
	}
}
