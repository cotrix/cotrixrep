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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.codelist.attribute.AttributesGroupsProvider;
import org.cotrix.web.codelistmanager.client.codelist.event.CodeSelectedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchType;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupsChangedEvent.GroupsChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.codelistmanager.client.data.CodeAttribute;
import org.cotrix.web.codelistmanager.client.data.DataEditor;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.shared.Group;
import org.cotrix.web.codelistmanager.shared.ManagerUIFeature;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.FeatureToggler;
import org.cotrix.web.share.client.resources.CommonResources;
import org.cotrix.web.share.client.resources.CotrixSimplePager;
import org.cotrix.web.share.client.widgets.DoubleClickEditTextCell;
import org.cotrix.web.share.client.widgets.HasEditing;
import org.cotrix.web.share.client.widgets.ItemToolbar;
import org.cotrix.web.share.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.share.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.share.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.share.client.widgets.StyledSafeHtmlRenderer;
import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UICode;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.Header;
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
public class CodelistEditor extends ResizeComposite implements HasEditing {

	interface Binder extends UiBinder<Widget, CodelistEditor> { }

	interface DataGridResources extends PatchedDataGrid.Resources {

		@Source("CodelistEditor.css")
		DataGridStyle dataGridStyle();
	}

	interface DataGridStyle extends PatchedDataGrid.Style {

		String groupHeaderCell();
		
		String textCell();

		String language();
		
		String closeGroup();
		
		String emptyTableWidget();
	}

	@UiField(provided = true)
	PatchedDataGrid<UICode> dataGrid;

	@UiField(provided = true)
	SimplePager pager;

	@UiField ItemToolbar toolBar;

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 
	protected static DataGridResources resource = GWT.create(DataGridResources.class);

	protected Set<Group> groupsAsColumn = new HashSet<Group>();
	protected Map<Group, Column<UICode, String>> groupsColumns = new HashMap<Group, Column<UICode,String>>(); 
	protected Map<String, Column<UICode, String>> switchesColumns = new HashMap<String, Column<UICode,String>>(); 
	protected List<DoubleClickEditTextCell> editableCells = new ArrayList<DoubleClickEditTextCell>();
	protected boolean editable = true;

	private Column<UICode, String> nameColumn;

	protected EventBus editorBus;

	protected SingleSelectionModel<UICode> selectionModel;

	protected CodelistCodesProvider dataProvider;
	protected HandlerRegistration registration;

	protected DataEditor<UICode> codeEditor;

	protected DataEditor<CodeAttribute> attributeEditor;
	
	protected StyledSafeHtmlRenderer cellRenderer;
	protected StyledSafeHtmlRenderer systemAttributeCell = new StyledSafeHtmlRenderer(CotrixManagerResources.INSTANCE.css().systemProperty());

	@Inject
	protected AttributesGroupsProvider attributesGroupsProvider;
	
	@Inject
	public CodelistEditor(@EditorBus EventBus editorBus, CodelistCodesProvider dataProvider) {
		this.editorBus = editorBus;
		this.dataProvider = dataProvider;
		this.codeEditor = DataEditor.build(this);
		this.attributeEditor = DataEditor.build(this);

		cellRenderer = new StyledSafeHtmlRenderer(resource.dataGridStyle().textCell());
		
		dataGrid = new PatchedDataGrid<UICode>(20, resource, CodelistCodeKeyProvider.INSTANCE);
		dataGrid.setAutoHeaderRefreshDisabled(true);
		
		Label emptyTable = new Label("No codes");
		emptyTable.setStyleName(resource.dataGridStyle().emptyTableWidget());
		dataGrid.setEmptyTableWidget(emptyTable);
		
		dataGrid.setTableWidth(100, Unit.PCT);
		dataGrid.setAutoAdjust(true);

		AsyncHandler asyncHandler = new AsyncHandler(dataGrid);
		dataGrid.addColumnSortHandler(asyncHandler);

		// Create a Pager to control the table.
		pager = new SimplePager(TextLocation.CENTER, CotrixSimplePager.INSTANCE, false, 0, true);
		pager.setDisplay(dataGrid);

		setupColumns();

		selectionModel = new SingleSelectionModel<UICode>(CodelistCodeKeyProvider.INSTANCE);
		dataGrid.setSelectionModel(selectionModel);
	
		dataProvider.addDataDisplay(dataGrid);

		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	protected void setupColumns() {
		
		nameColumn = new CodeColumn(createCell(false));

		nameColumn.setSortable(true);

		nameColumn.setFieldUpdater(new FieldUpdater<UICode, String>() {

			@Override
			public void update(int index, UICode row, String value) {
				row.getName().setLocalPart(value);
				codeEditor.updated(row);
			}
		});

		dataGrid.addColumn(nameColumn, "Code");
	}

	public void showAllAttributesAsColumn()
	{
		attributesGroupsProvider.loadGroups();
	}

	public void showAllAttributesAsNormal()
	{
		switchAllGroupsToNormal();
	}

	public void setEditable(boolean editable)
	{
		this.editable = editable;
		for (DoubleClickEditTextCell cell:editableCells) cell.setReadOnly(!editable);
	}

	protected DoubleClickEditTextCell createCell(boolean isSystemAttribute)
	{
		String editorStyle = CommonResources.INSTANCE.css().textBox() + " " + CotrixManagerResources.INSTANCE.css().editor();
		DoubleClickEditTextCell cell = new DoubleClickEditTextCell(editorStyle, isSystemAttribute?systemAttributeCell:cellRenderer);
		if (!isSystemAttribute) {
			cell.setReadOnly(!editable);
			editableCells.add(cell);
		}
		return cell;
	}

	@Inject
	protected void bind(@CodelistId String codelistId)
	{
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.ADD_CODE);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.MINUS, active);
			}
		}, codelistId, ManagerUIFeature.REMOVE_CODE);
		
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

		editorBus.addHandler(DataEditEvent.getType(UICode.class), new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				Log.trace("onDataEdit row: "+event.getData());
				int index = dataGrid.getVisibleItems().indexOf(event.getData());
				Log.trace("index: "+index);
				if (index>=0) dataGrid.redrawRow(index);
			}
		});

		editorBus.addHandler(DataEditEvent.getType(CodeAttribute.class), new DataEditHandler<CodeAttribute>() {

			@Override
			public void onDataEdit(DataEditEvent<CodeAttribute> event) {
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
		
		attributesGroupsProvider.addGroupsChangedHandler(new GroupsChangedHandler() {
			
			@Override
			public void onGroupsChanged(GroupsChangedEvent event) {
				Set<Group> groups = event.getGroups();
				Log.trace("onAttributeSetChanged groups: "+groups);
				setGroups(groups);
			}
		});
	}

	protected void refreshCode(UICode code)
	{
		Log.trace("refreshCode code: "+code);
		int row = dataProvider.getCache().indexOf(code);
		Log.trace("row: "+row);
		if (row>=0) dataGrid.redrawRow(row);
	}

	protected void removeSelectedCode()
	{
		UICode code = selectionModel.getSelectedObject();
		if (code!=null) {
			dataProvider.remove(code);/* getCache().remove(code);
			dataProvider.refresh();*/
			codeEditor.removed(code);
		}
	}

	protected void addCode()
	{
		UICode code = CodeFactory.createCode();
		dataProvider.getCache().add(0, code);
		dataProvider.refresh();
		selectionModel.setSelected(code, true);
		codeEditor.added(code);
	}

	protected Column<UICode, String> getGroupColumn(final Group group)
	{
		Log.trace("getGroupColumn group: "+group);
		Column<UICode, String> column = groupsColumns.get(group);
		if (column == null) {
			column = new GroupColumn(createCell(group.isSystemGroup()), group);
			column.setSortable(true);
			
			column.setFieldUpdater(new FieldUpdater<UICode, String>() {

				@Override
				public void update(int index, UICode code, String value) {
					UIAttribute attribute = group.match(code.getAttributes());
					if (attribute!=null) {
						attribute.setValue(value);
						attributeEditor.updated(new CodeAttribute(code, attribute));
					} else {
						attribute = new UIAttribute();
						attribute.setId(Document.get().createUniqueId());
						attribute.setName(group.getName());
						attribute.setLanguage(group.getLanguage());
						attribute.setValue(value);
						code.addAttribute(attribute);
						attributeEditor.added(new CodeAttribute(code, attribute));
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

		dataGrid.addColumn(column, new GroupHeader(group));
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

	protected void setGroups(Set<Group> groups) {
		
		Set<Group> columnsToRemove = new HashSet<Group>(groupsAsColumn);
		//can't use removeall because based on comparable interface
		for (Group group:groups) columnsToRemove.remove(group);
		Log.trace("columns to remove: "+columnsToRemove);

		for (Group toRemove:columnsToRemove) switchToNormal(toRemove);

		for (Group group:groups) switchToColumn(group);
	}

	protected void switchAllGroupsToNormal() {
		Set<Group> groupsToNormal = new HashSet<Group>(groupsAsColumn);
		for (Group group:groupsToNormal) switchToNormal(group);
	}
	
	public static abstract class CodelistEditorColumn<C> extends Column<UICode, C> {

		public CodelistEditorColumn(Cell<C> cell) {
			super(cell);
		}
	}
	
	public static class CodeColumn extends CodelistEditorColumn<String> {

		public CodeColumn(Cell<String> cell) {
			super(cell);
		}

		@Override
		public String getValue(UICode object) {
			return object.getName().getLocalPart();
		}
	}
	
	public static class GroupColumn extends CodelistEditorColumn<String> {
		
		protected Group group;

		public GroupColumn(Cell<String> cell, Group group) {
			super(cell);
			this.group = group;
		}

		@Override
		public String getValue(UICode row) {
			if (row == null) return "";
			return group.getValue(row.getAttributes());
		}

		public Group getGroup() {
			return group;
		}
	}
	
	protected class GroupHeader extends Header<Group> {

		private Group group;

		/**
		 * Construct a new TextHeader.
		 *
		 * @param text the header text as a String
		 */
		public GroupHeader(Group group) {
			super(new ClickableGroupCell(new SafeHtmlGroupRenderer()));
			this.group = group;
			
			setUpdater(new ValueUpdater<Group>() {

				@Override
				public void update(Group value) {
					switchToNormal(value);
				}
			});
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public boolean onPreviewColumnSortEvent(Context context, Element elem,
				NativeEvent event) {
			System.out.println("onPreviewColumnSortEvent elem "+elem);
			Element element = event.getEventTarget().cast();
			System.out.println("onPreviewColumnSortEvent element.id "+element.getId());
			return !element.getId().equals(SafeHtmlGroupRenderer.CLOSE_IMG_ID);
		}



		/**
		 * Return the header text.
		 */
		@Override
		public Group getValue() {
			return group;
		}
	}

	public class ClickableGroupCell extends AbstractSafeHtmlCell<Group> {


		/**
		 * Construct a new ClickableTextCell that will use a given
		 * {@link SafeHtmlRenderer}.
		 * 
		 * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<Group>} instance
		 */
		public ClickableGroupCell(SafeHtmlRenderer<Group> renderer) {
			super(renderer, CLICK, KEYDOWN);
		}

		@Override
		public void onBrowserEvent(Context context, Element parent, Group value,
				NativeEvent event, ValueUpdater<Group> valueUpdater) {
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if (CLICK.equals(event.getType())) {
				onEnterKeyDown(context, parent, value, event, valueUpdater);
			}
		}

		@Override
		protected void onEnterKeyDown(Context context, Element parent, Group value,
				NativeEvent event, ValueUpdater<Group> valueUpdater) {
			Element element = event.getEventTarget().cast();
			
			if (valueUpdater != null && element.getId().equals(SafeHtmlGroupRenderer.CLOSE_IMG_ID)) {
				valueUpdater.update(value);
			}
		}

		@Override
		protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.append(value);
			}
		}
	}

	static class SafeHtmlGroupRenderer extends AbstractSafeHtmlRenderer<Group> {
		
		static interface GroupHeaderTemplate extends SafeHtmlTemplates {
			
			@Template("<div style=\"height:16px\"><span style=\"vertical-align:middle;padding-right: 7px;\">{0}</span><span style=\"vertical-align:middle;color:black;padding-left:5px;\">{1}</span><img id=\"{4}\" src=\"{2}\" class=\"{3}\" style=\"vertical-align:middle;\"/></div>")
			SafeHtml headerWithLanguage(SafeHtml name, SafeHtml language, SafeUri img, String imgStyle, String imgId);

			@Template("<div style=\"height:16px\"><span style=\"vertical-align:middle;padding-right: 7px;\">{0}</span><img id=\"{3}\"  src=\"{1}\" class=\"{2}\" style=\"vertical-align:middle;\"/></div>")
			SafeHtml header(SafeHtml name, SafeUri img, String imgStyle, String imgId);
		}
		
		static final GroupHeaderTemplate HEADER_TEMPLATE = GWT.create(GroupHeaderTemplate.class);
		public static final String CLOSE_IMG_ID = Document.get().createUniqueId();
		
		@Override
		public SafeHtml render(Group value) {
			SafeHtml name = SafeHtmlUtils.fromString(value.getName().getLocalPart());
			SafeUri img = CotrixManagerResources.INSTANCE.closeSmall().getSafeUri();
			String imgStyle = resource.dataGridStyle().closeGroup();
			if (value.getLanguage()!=null && !value.getLanguage().isEmpty()) {
				/*ImageResource languageImage = LanguageResources.getResource(value.getLanguage());
				if (languageImage != null) return HEADER_TEMPLATE.headerWithLanguageImage(name, languageImage.getSafeUri(), img, imgStyle);
				else */ 
				return HEADER_TEMPLATE.headerWithLanguage(name, SafeHtmlUtils.fromString(value.getLanguage()), img, imgStyle, CLOSE_IMG_ID);
			} else return HEADER_TEMPLATE.header(name, img, imgStyle, CLOSE_IMG_ID);
		}
	}
}
