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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.codelist.attribute.Group;
import org.cotrix.web.codelistmanager.client.codelist.attribute.GroupFactory;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.GroupsChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchType;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.RowSelectedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.codelistmanager.client.data.CodelistRowEditor;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.share.client.widgets.DoubleClickEditTextCell;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.PatchedDataGrid;
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
public class CodelistEditor extends ResizeComposite implements GroupsChangedHandler {

	interface Binder extends UiBinder<Widget, CodelistEditor> { }

	interface DataGridResources extends PatchedDataGrid.Resources {

		@Source("CodelistEditor.css")
		DataGridStyle dataGridStyle();
	}

	interface DataGridStyle extends PatchedDataGrid.Style {

		String groupHeaderCell();
	}


	@UiField(provided = true)
	PatchedDataGrid<UICode> dataGrid;

	@UiField(provided = true)
	SimplePager pager;

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 
	protected DataGridResources resource = GWT.create(DataGridResources.class);

	protected Set<Group> groupsAsColumn = new HashSet<Group>();
	protected Map<Group, Column<UICode, String>> groupsColumns = new HashMap<Group, Column<UICode,String>>(); 
	protected Map<String, Column<UICode, String>> switchesColumns = new HashMap<String, Column<UICode,String>>(); 

	private Column<UICode, String> nameColumn;
	
	protected EventBus editorBus;

	protected SingleSelectionModel<UICode> selectionModel;
	
	protected CodelistCodesDataProvider dataProvider;
	protected HandlerRegistration registration;
	
	protected CodelistRowEditor rowEditor;

	@Inject
	public CodelistEditor(@EditorBus EventBus editorBus, CodelistCodesDataProvider dataProvider, CodelistRowEditor rowEditor) {
		this.editorBus = editorBus;
		this.dataProvider = dataProvider;
		this.rowEditor = rowEditor;

		dataGrid = new PatchedDataGrid<UICode>(20, resource, CodelistCodeKeyProvider.INSTANCE);
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label("Empty"));
		dataGrid.setTableWidth(100, Unit.PCT);

		//TODO add sorting

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(dataGrid);

		setupColumns();


		selectionModel = new SingleSelectionModel<UICode>(CodelistCodeKeyProvider.INSTANCE);
		dataGrid.setSelectionModel(selectionModel);

		// Specify a custom table.
		//dataGrid.setTableBuilder(new CustomTableBuilder());
		
		dataProvider.addDataDisplay(dataGrid);
		
		bind();

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	protected void setupColumns() {

		nameColumn = new Column<UICode, String>(new DoubleClickEditTextCell()) {
			@Override
			public String getValue(UICode object) {
				if (object == null) return "";
				return object.getName();
			}
		};
		
		nameColumn.setFieldUpdater(new FieldUpdater<UICode, String>() {
			
			@Override
			public void update(int index, UICode row, String value) {
				row.setName(value);
				rowEditor.edited(row);
			}
		});

		dataGrid.addColumn(nameColumn, "Code");
	}

	public void showAllAttributesAsColumn()
	{
		if (registration == null) registration = dataProvider.addGroupsChangedHandler(this);
		switchAllGroupsToColumn();
	}
	
	public void showAllAttributesAsNormal()
	{
		if (registration!=null) registration.removeHandler();
		registration = null;
		switchAllGroupsToNormal();
	}
	
	protected void bind()
	{
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				UICode row = selectionModel.getSelectedObject();
				Log.trace("onSelectionChange row: "+row);
				if (row !=null) editorBus.fireEvent(new RowSelectedEvent(row));
			}
		});
		
		editorBus.addHandler(SwitchGroupEvent.TYPE, new SwitchGroupEvent.SwitchAttributeHandler() {
			
			@Override
			public void onSwitchAttribute(SwitchGroupEvent event) {
				Group group = event.getGroup();
				Log.trace("onSwitchAttribute group: "+group+" type: "+event.getSwitchType());
				switch (event.getSwitchType()) {
					case TO_COLUMN: switchToColumn(group); break;
					case TO_NORMAL: switchToNormal(group); break;
				}
			}
		});
		
		rowEditor.addDataEditHandler(new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				Log.trace("onDataEdit row: "+event.getData());
				int index = dataGrid.getVisibleItems().indexOf(event.getData());
				Log.trace("index: "+index);
				if (index>=0) dataGrid.redrawRow(index);
			}
		});
	}

	protected Column<UICode, String> getGroupColumn(final Group group)
	{
		Column<UICode, String> column = groupsColumns.get(group);
		if (column == null) {
			DoubleClickEditTextCell cell = new DoubleClickEditTextCell();
			column = new Column<UICode, String>(cell) {

				@Override
				public String getValue(UICode row) {
					if (row == null) return "";
					return group.getValue(row.getAttributes());
				}
			};
			column.setFieldUpdater(new FieldUpdater<UICode, String>() {

				@Override
				public void update(int index, UICode row, String value) {
					UIAttribute attribute = group.match(row.getAttributes());
					attribute.setValue(value);
					rowEditor.edited(row);
				}
			});
			
			groupsColumns.put(group, column);
		}
		return column;
	}

	protected void switchToColumn(Group group)
	{
		addGroupColumn(group);
		editorBus.fireEvent(new GroupSwitchedEvent(group, GroupSwitchType.TO_COLUMN));
	}
	
	protected void addGroupColumn(Group group)
	{
		if (groupsAsColumn.contains(group)) return;
		Column<UICode, String> column = getGroupColumn(group);
		groupsAsColumn.add(group);

		dataGrid.addColumn(column, group.getLabel());
	}
	
	protected void switchToNormal(Group group)
	{
		removeGroupColumn(group);
		editorBus.fireEvent(new GroupSwitchedEvent(group, GroupSwitchType.TO_NORMAL));
	}
	
	protected void removeGroupColumn(Group group)
	{
		if (!groupsAsColumn.contains(group)) return;
		Column<UICode, String> column = getGroupColumn(group);
		groupsAsColumn.remove(group);
		dataGrid.removeColumn(column);
	}

	@Override
	public void onGroupsChanged(GroupsChangedEvent event) {
		Set<Group> groups = event.getGroups();
		Log.trace("onAttributeSetChanged groups: "+groups);
		
		Set<Group> columnsToRemove = new HashSet<Group>(groupsAsColumn);
		columnsToRemove.removeAll(groups);
		Log.trace("columns to remove: "+columnsToRemove);
		
		for (Group toRemove:columnsToRemove) removeGroupColumn(toRemove);
		
		for (Group group:groups) addGroupColumn(group);
	}
	
	public void switchAllGroupsToColumn() {
		Log.trace("switchAllGroupsToColumn");
		
		Set<Group> groups = GroupFactory.getGroups(dataGrid.getVisibleItems());
		Log.trace("groups: "+groups);
		
		groups.removeAll(groupsAsColumn);	
		Log.trace("attributes to add: "+groups);
		
		for (Group group:groups) switchToColumn(group);
	}
	
	public void switchAllGroupsToNormal() {
		Set<Group> groupsToNormal = new HashSet<Group>(groupsAsColumn);
		for (Group group:groupsToNormal) switchToNormal(group);
	}
}
