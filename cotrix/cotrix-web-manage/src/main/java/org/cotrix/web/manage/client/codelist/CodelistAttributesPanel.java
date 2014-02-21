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
package org.cotrix.web.manage.client.codelist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ImageResourceCell;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.attribute.AttributeFactory;
import org.cotrix.web.manage.client.codelist.attribute.GroupFactory;
import org.cotrix.web.manage.client.codelist.event.AttributeChangedEvent;
import org.cotrix.web.manage.client.codelist.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.codelist.event.GroupSwitchType;
import org.cotrix.web.manage.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.manage.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.manage.client.codelist.event.AttributeChangedEvent.AttributeChangedHandler;
import org.cotrix.web.manage.client.data.CodeAttribute;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.Group;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
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
public class CodelistAttributesPanel extends ResizeComposite implements HasEditing {

	interface Binder extends UiBinder<Widget, CodelistAttributesPanel> { }

	interface Style extends CssResource {
		String headerCode();
		String noAttributes();
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

	protected static ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	private Set<Group> groupsAsColumn = new HashSet<Group>();
	private Column<UIAttribute, AttributeSwitchState> switchColumn; 

	protected EventBus editorBus;

	protected ListDataProvider<UIAttribute> dataProvider;

	protected AttributeHeader header;

	protected DataEditor<UICode> codeEditor;

	protected UICode visualizedCode;

	protected DataEditor<CodeAttribute> attributeEditor;

	@Inject
	public CodelistAttributesPanel(@EditorBus EventBus editorBus) {

		this.editorBus = editorBus;
		this.codeEditor = DataEditor.build(this);
		this.attributeEditor = DataEditor.build(this);

		this.dataProvider = new ListDataProvider<UIAttribute>();

		header = new AttributeHeader();

		attributesGrid = new AttributesGrid(dataProvider, header, "select a code");

		setupColumns();

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));

		updateBackground();
	}

	@Inject
	protected void bind(@CodelistId String codelistId)
	{
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		FeatureBinder.bind(new FeatureToggler() {
			
			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.MINUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_CODELIST);
		
		editorBus.addHandler(CodeSelectedEvent.TYPE, new CodeSelectedEvent.CodeSelectedHandler() {

			@Override
			public void onCodeSelected(CodeSelectedEvent event) {
				updateVisualizedCode(event.getCode());
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
				if (visualizedCode!=null) {
					UIAttribute attribute = group.match(visualizedCode.getAttributes());
					if (attribute!=null) attributesGrid.refreshAttribute(attribute);
				}

			}
		});

		attributesGrid.addAttributeChangedHandler(new AttributeChangedHandler() {

			@Override
			public void onAttributeChanged(AttributeChangedEvent event) {
				Log.trace("updated attribute "+event.getAttribute());
				attributeEditor.updated(new CodeAttribute(visualizedCode, event.getAttribute()));
			}
		});

		editorBus.addHandler(DataEditEvent.getType(UICode.class), new DataEditHandler<UICode>() {

			@Override
			public void onDataEdit(DataEditEvent<UICode> event) {
				if (visualizedCode!=null && visualizedCode.equals(event.getData())) {
					switch (event.getEditType()) {
						case UPDATE: updateVisualizedCode(event.getData()); break;
						case REMOVE: clearVisualizedCode(); break;
						default:
					}

				}
			}
		});
		
		editorBus.addHandler(CodeUpdatedEvent.TYPE, new CodeUpdatedEvent.CodeUpdatedHandler() {
			
			@Override
			public void onCodeUpdated(CodeUpdatedEvent event) {
				updateVisualizedCode(event.getCode());
			}
		});
		
		editorBus.addHandler(DataEditEvent.getType(CodeAttribute.class), new DataEditHandler<CodeAttribute>() {

			@Override
			public void onDataEdit(DataEditEvent<CodeAttribute> event) {
				if (visualizedCode!=null && visualizedCode.equals(event.getData().getCode())) {
					switch (event.getEditType()) {
						case ADD: {
							if (event.getSource() != CodelistAttributesPanel.this) {
								dataProvider.getList().add(event.getData().getAttribute());
								dataProvider.refresh();
							}
						} break;
						case UPDATE: attributesGrid.refreshAttribute(event.getData().getAttribute()); break;
						default:
					}
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		//GWT issue 7188 workaround
		onResize();
	}
	
	protected void addNewAttribute()
	{
		if (visualizedCode!=null) {
			UIAttribute attribute = AttributeFactory.createAttribute();
			visualizedCode.addAttribute(attribute);
			dataProvider.getList().add(attribute);
			dataProvider.refresh();

			attributeEditor.added(new CodeAttribute(visualizedCode, attribute));
			attributesGrid.expand(attribute);
		}
	}

	protected void removeSelectedAttribute()
	{
		if (visualizedCode!=null && attributesGrid.getSelectedAttribute()!=null) {
			UIAttribute selectedAttribute = attributesGrid.getSelectedAttribute();
			dataProvider.getList().remove(selectedAttribute);
			dataProvider.refresh();
			visualizedCode.removeAttribute(selectedAttribute);
			attributeEditor.removed(new CodeAttribute(visualizedCode, selectedAttribute));
		}
	}

	protected void updateVisualizedCode(UICode code)
	{
		visualizedCode = code;
		setHeader(visualizedCode.getName().getLocalPart());
		updateBackground();

		List<UIAttribute> currentAttributes = dataProvider.getList();
		currentAttributes.clear();
		
		Attributes.sortByAttributeType(visualizedCode.getAttributes());
		currentAttributes.addAll(visualizedCode.getAttributes());
		dataProvider.refresh();
		Log.trace("request refresh of "+visualizedCode.getAttributes().size()+" attributes");
	}

	protected void clearVisualizedCode()
	{
		visualizedCode = null;
		setHeader(null);
		updateBackground();

		dataProvider.getList().clear();
		dataProvider.refresh();
	}
	
	protected void updateBackground()
	{
		setStyleName(style.noAttributes(), visualizedCode == null || visualizedCode.getAttributes().isEmpty());
	}

	protected void setHeader(String text)
	{
		header.setText(text);
		attributesGrid.redrawHeaders();
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
		attributesGrid.setColumnWidth(0, 25, Unit.PX);

	}

	protected boolean isInGroupAsColumn(UIAttribute attribute)
	{
		for (Group group:groupsAsColumn) if (group.accept(visualizedCode.getAttributes(), attribute)) return true;
		return false;
	}


	protected void switchAttribute(UIAttribute attribute, AttributeSwitchState attributeSwitchState)
	{
		Group group = GroupFactory.getGroup(attribute);
		Log.trace("calculating position for "+attribute+" in: "+visualizedCode.getAttributes());
		group.calculatePosition(visualizedCode.getAttributes(), attribute);

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
		public AttributeHeader() {
			super(new AbstractCell<String>() {

				@Override
				public void render(com.google.gwt.cell.client.Cell.Context context,
						String value, SafeHtmlBuilder sb) {
					sb.appendHtmlConstant("<span>Attributes</span>");
					if (value!=null) {
						sb.appendHtmlConstant("&nbsp;for&nbsp;<span class=\""+style.headerCode()+"\">");
						sb.append(SafeHtmlUtils.fromString(value));
						sb.appendHtmlConstant("</span>");
					}
				}
			});
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

	@Override
	public void setEditable(boolean editable) {
		attributesGrid.setEditable(editable);
	}

}
