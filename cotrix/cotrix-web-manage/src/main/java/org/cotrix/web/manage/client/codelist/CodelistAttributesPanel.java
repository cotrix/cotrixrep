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
import org.cotrix.web.manage.client.codelist.attribute.AttributeChangedEvent;
import org.cotrix.web.manage.client.codelist.attribute.AttributeFactory;
import org.cotrix.web.manage.client.codelist.attribute.AttributeNameSuggestOracle;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGrid;
import org.cotrix.web.manage.client.codelist.attribute.GroupFactory;
import org.cotrix.web.manage.client.codelist.attribute.AttributeChangedEvent.AttributeChangedHandler;
import org.cotrix.web.manage.client.codelist.attribute.RemoveAttributeController;
import org.cotrix.web.manage.client.codelist.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.codelist.event.GroupSwitchType;
import org.cotrix.web.manage.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.manage.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.manage.client.data.CodeAttribute;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.data.event.DataEditEvent;
import org.cotrix.web.manage.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.AttributeGroup;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistAttributesPanel extends ResizeComposite implements HasEditing {

	interface Binder extends UiBinder<Widget, CodelistAttributesPanel> {}
	interface CodelistAttributesPanelEventBinder extends EventBinder<CodelistAttributesPanel> {}

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

	private Set<AttributeGroup> groupsAsColumn = new HashSet<AttributeGroup>();
	private Column<UIAttribute, AttributeSwitchState> switchColumn; 

	@Inject @EditorBus
	protected EventBus editorBus;

	protected ListDataProvider<UIAttribute> dataProvider;

	protected AttributeHeader header;

	protected DataEditor<UICode> codeEditor;

	protected UICode visualizedCode;

	protected DataEditor<CodeAttribute> attributeEditor;

	@Inject
	private CotrixManagerResources resources;

	@Inject
	protected RemoveAttributeController attributeController;

	protected AttributeNameSuggestOracle attributeNameSuggestOracle;

	@Inject
	public CodelistAttributesPanel(AttributeNameSuggestOracle attributeNameSuggestOracle) {

		this.codeEditor = DataEditor.build(this);
		this.attributeEditor = DataEditor.build(this);

		this.dataProvider = new ListDataProvider<UIAttribute>();

		this.attributeNameSuggestOracle = attributeNameSuggestOracle;

		header = new AttributeHeader();

		attributesGrid = new AttributesGrid(dataProvider, header, "select a code", attributeNameSuggestOracle);

		setupColumns();

		// Create the UiBinder.
		Binder uiBinder = GWT.create(Binder.class);
		initWidget(uiBinder.createAndBindUi(this));

		updateBackground();
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId)
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
				attributeController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_CODELIST);

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

		attributesGrid.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedAttributeChanged();
			}
		});
	}

	@Inject
	protected void bind(CodelistAttributesPanelEventBinder binder) {
		binder.bindEventHandlers(this, editorBus);
	}

	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		updateVisualizedCode(event.getCode());
	}

	@EventHandler
	void onCodeUpdated(CodeUpdatedEvent event) {
		updateVisualizedCode(event.getCode());
	}

	@EventHandler
	void onGroupSwitched(GroupSwitchedEvent event) {
		Group group = event.getGroup();

		if (group instanceof AttributeGroup) {
			AttributeGroup attributeGroup = (AttributeGroup) group;
			Log.trace("onAttributeSwitched group: "+attributeGroup+" type: "+event.getSwitchType());

			switch (event.getSwitchType()) {
				case TO_COLUMN: groupsAsColumn.add(attributeGroup); break;
				case TO_NORMAL: groupsAsColumn.remove(attributeGroup); break;
			}
			if (visualizedCode!=null) {
				UIAttribute attribute = attributeGroup.match(visualizedCode.getAttributes());
				if (attribute!=null) attributesGrid.refreshAttribute(attribute);
			}
		}
	}

	private void updateRemoveButtonVisibility(boolean animate) {
		toolBar.setVisible(ItemButton.MINUS, attributeController.canRemove(), animate);
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
			if (Attributes.isSystemAttribute(selectedAttribute)) return; 
			dataProvider.getList().remove(selectedAttribute);
			dataProvider.refresh();
			visualizedCode.removeAttribute(selectedAttribute);
			attributeEditor.removed(new CodeAttribute(visualizedCode, selectedAttribute));
		}
	}

	protected void selectedAttributeChanged()
	{
		if (visualizedCode!=null && attributesGrid.getSelectedAttribute()!=null) {
			UIAttribute selectedAttribute = attributesGrid.getSelectedAttribute();
			attributeController.setAttributeCanBeRemoved(!Attributes.isSystemAttribute(selectedAttribute));
			updateRemoveButtonVisibility(false);
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
					case COLUMN: return renderer.render(resources.tableDisabled());
					case NORMAL: return renderer.render(resources.table());	
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
		for (AttributeGroup group:groupsAsColumn) if (group.accept(visualizedCode.getAttributes(), attribute)) return true;
		return false;
	}


	protected void switchAttribute(UIAttribute attribute, AttributeSwitchState attributeSwitchState)
	{
		AttributeGroup group = GroupFactory.getGroup(attribute);
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
