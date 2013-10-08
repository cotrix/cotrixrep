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

import static com.google.gwt.dom.client.BrowserEvents.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSetChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSwitchType;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSwitchedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.RowSelectedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.SwitchAttributeEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSetChangedEvent.AttributeSetChangedHandler;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;
import org.cotrix.web.share.shared.UIAttribute;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListEditor extends ResizeComposite implements AttributeSetChangedHandler {

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

	protected Set<String> attributeAsColumn = new HashSet<String>();
	protected Map<String, Column<UICodeListRow, String>> attributesColumns = new HashMap<String, Column<UICodeListRow,String>>(); 
	protected Map<String, Column<UICodeListRow, String>> switchesColumns = new HashMap<String, Column<UICodeListRow,String>>(); 

	private Column<UICodeListRow, String> nameColumn;
	
	protected EventBus editorBus;

	protected SingleSelectionModel<UICodeListRow> selectionModel;

	@Inject
	public CodeListEditor(@EditorBus EventBus editorBus, CodeListRowDataProvider dataProvider) {
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
		
		dataProvider.addDataDisplay(dataGrid);
		
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
		
		editorBus.addHandler(SwitchAttributeEvent.TYPE, new SwitchAttributeEvent.SwitchAttributeHandler() {
			
			@Override
			public void onSwitchAttribute(SwitchAttributeEvent event) {
				UIAttribute attribute = event.getAttribute();
				Log.trace("onSwitchAttribute attribute: "+attribute+" type: "+event.getSwitchType());
				switch (event.getSwitchType()) {
					case TO_COLUMN: switchToColumn(attribute); break;
					case TO_NORMAL: switchToNormal(attribute); break;
				}
			}
		});
	}

	private void setupColumns() {

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

	protected void switchToColumn(final UIAttribute attribute)
	{
		final String attributeName = attribute.getName();
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
				switchToNormal(attribute);				
			}
		});
		
		//ResizableHeader<UICodeListRow> resizableHeader = new ResizableHeader<UICodeListRow>(attributeName, dataGrid, column);
		dataGrid.addColumn(column, header);
		//dataGrid.setColumnWidth(dataGrid.getColumnCount()-1, 1, Unit.EM);
		//dataGrid.clearTableWidth();
		editorBus.fireEvent(new AttributeSwitchedEvent(attribute, AttributeSwitchType.TO_COLUMN));
	}
	
	protected void switchToNormal(UIAttribute attribute)
	{
		String attributeName = attribute.getName();
		Column<UICodeListRow, String> column = getAttributeColumn(attributeName);
		attributeAsColumn.remove(attributeName);
		dataGrid.removeColumn(column);
		removeUnusedDataGridColumns(dataGrid);
		
		editorBus.fireEvent(new AttributeSwitchedEvent(attribute, AttributeSwitchType.TO_NORMAL));
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

	@Override
	public void onAttributeSetChanged(AttributeSetChangedEvent event) {
		
	}
}
