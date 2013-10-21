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
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchType;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.GroupsChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.CodeSelectedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.codelistmanager.client.data.CodeAttributeEditor;
import org.cotrix.web.codelistmanager.client.data.CodeEditor;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.share.client.resources.CotrixSimplePager;
import org.cotrix.web.share.client.widgets.DoubleClickEditTextCell;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
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
	
	@UiField ItemToolbar toolBar;

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 
	protected DataGridResources resource = GWT.create(DataGridResources.class);

	protected Set<Group> groupsAsColumn = new HashSet<Group>();
	protected Map<Group, Column<UICode, String>> groupsColumns = new HashMap<Group, Column<UICode,String>>(); 
	protected Map<String, Column<UICode, String>> switchesColumns = new HashMap<String, Column<UICode,String>>(); 

	private Column<UICode, String> nameColumn;
	
	protected EventBus editorBus;

	protected SingleSelectionModel<UICode> selectionModel;
	
	protected CodelistCodesProvider dataProvider;
	protected HandlerRegistration registration;
	
	protected CodeEditor codeEditor;

	protected CodeAttributeEditor attributeEditor;
	
	@Inject
	public CodelistEditor(@EditorBus EventBus editorBus, CodelistCodesProvider dataProvider, CodeEditor codeEditor, CodeAttributeEditor attributeEditor) {
		this.editorBus = editorBus;
		this.dataProvider = dataProvider;
		this.codeEditor = codeEditor;
		this.attributeEditor = attributeEditor;

		dataGrid = new PatchedDataGrid<UICode>(20, resource, CodelistCodeKeyProvider.INSTANCE);
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label("Empty"));
		dataGrid.setTableWidth(100, Unit.PCT);

		//TODO add sorting

		// Create a Pager to control the table.
		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.setDisplay(dataGrid);

		setupColumns();


		selectionModel = new SingleSelectionModel<UICode>(CodelistCodeKeyProvider.INSTANCE);
		dataGrid.setSelectionModel(selectionModel);

		// Specify a custom table.
		//dataGrid.setTableBuilder(new CustomTableBuilder());
		
		dataProvider.addDataDisplay(dataGrid);

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
		
		bind();
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
				codeEditor.updated(row);
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
				UICode code = selectionModel.getSelectedObject();
				Log.trace("onSelectionChange code: "+code);
				if (code !=null) editorBus.fireEvent(new CodeSelectedEvent(code));
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
		
		codeEditor.addDataEditHandler(new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				Log.trace("onDataEdit row: "+event.getData());
				int index = dataGrid.getVisibleItems().indexOf(event.getData());
				Log.trace("index: "+index);
				if (index>=0) dataGrid.redrawRow(index);
			}
		});
		
		attributeEditor.addDataEditHandler(new DataEditHandler<CodeAttributeEditor.CodeAttribute>() {

			@Override
			public void onDataEdit(DataEditEvent<CodeAttributeEditor.CodeAttribute> event) {
					//dataProvider.refresh();
					refreshCode(event.getData().getCode());
			}
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {
			
			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case MINUS: removeSelectedCode(); break;
					case PLUS: addCode(); break;
				}
			}
		});
	}
	
	protected void refreshCode(UICode code)
	{
		Log.trace("refreshCode code: "+code);
		int row = dataProvider.getCodes().indexOf(code);
		Log.trace("row: "+row);
		if (row>=0) dataGrid.redrawRow(row);
	}
	
	protected void removeSelectedCode()
	{
		UICode code = selectionModel.getSelectedObject();
		if (code!=null) {
			dataProvider.getCodes().remove(code);
			dataProvider.refresh();
			codeEditor.removed(code);
		}
	}
	
	protected void addCode()
	{
		UICode code = new UICode(Document.get().createUniqueId(), "name");
		dataProvider.getCodes().add(0, code);
		dataProvider.refresh();
		selectionModel.setSelected(code, true);
		codeEditor.added(code);
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
				public void update(int index, UICode code, String value) {
					UIAttribute attribute = group.match(code.getAttributes());
					if (attribute!=null) {
						attribute.setValue(value);
						attributeEditor.updated(code, attribute);
					} else {
						attribute = new UIAttribute();
						attribute.setId(Document.get().createUniqueId());
						attribute.setName(group.getName());
						attribute.setLanguage(group.getLanguage());
						attribute.setValue(value);
						code.addAttribute(attribute);
						attributeEditor.added(code, attribute);
					}
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
