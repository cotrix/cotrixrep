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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.codelistmanager.client.codelist.attribute.Group;
import org.cotrix.web.codelistmanager.client.codelist.attribute.GroupFactory;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeChangedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeChangedEvent.AttributeChangedHandler;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchType;
import org.cotrix.web.codelistmanager.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.RowSelectedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.codelistmanager.client.data.CodeEditor;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.share.client.widgets.ImageResourceCell;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistAttributesPanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodelistAttributesPanel> { }

	interface Style extends CssResource {
		String headerCode();
	}

	enum AttributeSwitchState {
		COLUMN,
		NORMAL;
	}
	
	@UiField
	Style style;

	@UiField(provided = true)
	AttributesGrid attributesGrid;
	
	@UiField
	ItemToolbar toolBar;

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	private Set<Group> groupsAsColumn = new HashSet<Group>();
	private Column<UIAttribute, AttributeSwitchState> switchColumn; 

	protected EventBus editorBus;

	protected ListDataProvider<UIAttribute> dataProvider;

	protected AttributeHeader header;
	
	protected CodeEditor rowEditor;
	
	protected UICode visualizedRow;

	@Inject
	public CodelistAttributesPanel(@EditorBus EventBus editorBus, CodeEditor rowEditor) {

		this.editorBus = editorBus;
		this.rowEditor = rowEditor;
		
		this.dataProvider = new ListDataProvider<UIAttribute>();
		
		header = new AttributeHeader("");
		
		attributesGrid = new AttributesGrid(dataProvider, header);

		setupColumns();

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));
		
		bind();

	}

	protected void bind()
	{
		editorBus.addHandler(RowSelectedEvent.TYPE, new RowSelectedEvent.RowSelectedHandler() {

			@Override
			public void onRowSelected(RowSelectedEvent event) {
				updateVisualizedRow(event.getRow());
			}
		});

		editorBus.addHandler(GroupSwitchedEvent.TYPE, new GroupSwitchedEvent.GroupSwitchedHandler() {

			@Override
			public void onGroupSwitched(GroupSwitchedEvent event) {
				Group group = event.getGroup();
				Log.trace("onAttributeSwitched group: "+group+" type: "+event.getSwitchType());

				switch (event.getSwitchType()) {
					case TO_COLUMN: groupsAsColumn.add(group); break;
					case TO_NORMAL: groupsAsColumn.remove(group); break;
				}
				if (visualizedRow!=null) {
					UIAttribute attribute = group.match(visualizedRow.getAttributes());
					if (attribute!=null) attributesGrid.refreshAttribute(attribute);
				}

			}
		});
		
		attributesGrid.addAttributeChangedHandler(new AttributeChangedHandler() {
			
			@Override
			public void onAttributeChanged(AttributeChangedEvent event) {
				rowEditor.edited(visualizedRow);
			}
		});
		
		
		rowEditor.addDataEditHandler(new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				if (visualizedRow!=null && visualizedRow.equals(event.getData())) {
					updateVisualizedRow(event.getData());
				}
			}
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {
			
			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewAttribute(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});
	}
	
	protected void addNewAttribute()
	{
		if (visualizedRow!=null) {
			UIAttribute attribute = new UIAttribute();
			attribute.setId(Document.get().createUniqueId());
			attribute.setName("attribute");
			attribute.setValue("value");
			visualizedRow.addAttribute(attribute);
			dataProvider.getList().add(attribute);
			dataProvider.refresh();
			
			rowEditor.edited(visualizedRow);
			attributesGrid.expand(attribute);
		}
	}
	
	protected void removeSelectedAttribute()
	{
		if (visualizedRow!=null && attributesGrid.getSelectedAttribute()!=null) {
			UIAttribute selectedAttribute = attributesGrid.getSelectedAttribute();
			dataProvider.getList().remove(selectedAttribute);
			dataProvider.refresh();
			visualizedRow.removeAttribute(selectedAttribute);
			rowEditor.edited(visualizedRow);
		}
	}
	
	protected void updateVisualizedRow(UICode row)
	{
		visualizedRow = row;
		header.setText(visualizedRow.getName());
		attributesGrid.redrawHeaders();
		
		List<UIAttribute> currentAttributes = dataProvider.getList();
		currentAttributes.clear();
		currentAttributes.addAll(visualizedRow.getAttributes());
		dataProvider.refresh();
	}

	private void setupColumns() {

		SafeHtmlRenderer<AttributeSwitchState> switchRenderer = new AbstractSafeHtmlRenderer<AttributeSwitchState>() {

			@Override
			public SafeHtml render(AttributeSwitchState state) {
				switch (state) {
					case COLUMN: return renderer.render(CotrixManagerResources.INSTANCE.tableDisabled());
					case NORMAL: return renderer.render(CotrixManagerResources.INSTANCE.table());	
					default: return SafeHtmlUtils.EMPTY_SAFE_HTML;

				}
			}
		};

		switchColumn = new Column<UIAttribute, AttributeSwitchState>(new ImageResourceCell<AttributeSwitchState>(switchRenderer)) {

			@Override
			public AttributeSwitchState getValue(UIAttribute attribute) {
				return isInGroupAsColumn(attribute)?AttributeSwitchState.COLUMN:AttributeSwitchState.NORMAL;
			}
		};

		switchColumn.setFieldUpdater(new FieldUpdater<UIAttribute, AttributeSwitchState>() {
			@Override
			public void update(int index, UIAttribute attribute, AttributeSwitchState value) {
				switchAttribute(attribute, value);
				// Redraw the modified row.
				attributesGrid.redrawRow(index);
			}
		});
		attributesGrid.insertColumn(0, switchColumn);
		attributesGrid.setColumnWidth(0, 35, Unit.PX);

	}

	protected boolean isInGroupAsColumn(UIAttribute attribute)
	{
		for (Group group:groupsAsColumn) if (group.accept(visualizedRow.getAttributes(), attribute)) return true;
		return false;
	}
	

	protected void switchAttribute(UIAttribute attribute, AttributeSwitchState attributeSwitchState)
	{
		Group group = GroupFactory.getGroup(attribute);
		Log.trace("calculating position for "+attribute+" in: "+visualizedRow.getAttributes());
		group.calculatePosition(visualizedRow.getAttributes(), attribute);
	
		switch (attributeSwitchState) {
			case COLUMN: editorBus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_NORMAL)); break;
			case NORMAL: editorBus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_COLUMN)); break;
		}
	}

	protected class AttributeHeader extends Header<String> {

		private String text;

		/**
		 * Construct a new TextHeader.
		 *
		 * @param text the header text as a String
		 */
		public AttributeHeader(String text) {
			super(new AbstractCell<String>() {

				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context,
						String value, SafeHtmlBuilder sb) {
					sb.appendHtmlConstant("<span>Attributes:</span>");
					sb.appendHtmlConstant("<span class=\""+style.headerCode()+"\">");
					sb.append(SafeHtmlUtils.fromString(value));
					sb.appendHtmlConstant("</span>");
				}
			});
			this.text = text;
		}

		/**
		 * Return the header text.
		 */
		@Override
		public String getValue() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}


	}

}
