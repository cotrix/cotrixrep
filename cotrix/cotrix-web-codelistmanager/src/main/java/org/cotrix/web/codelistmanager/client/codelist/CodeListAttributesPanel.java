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

import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSwitchType;
import org.cotrix.web.codelistmanager.client.codelist.event.AttributeSwitchedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.RowSelectedEvent;
import org.cotrix.web.codelistmanager.client.codelist.event.SwitchAttributeEvent;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.share.client.widgets.ImageResourceCell;
import org.cotrix.web.share.shared.UIAttribute;

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
public class CodeListAttributesPanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodeListAttributesPanel> { }

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

	protected ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	private Set<String> attributeAsColumn = new HashSet<String>();
	private Column<UIAttribute, AttributeSwitchState> switchColumn; 

	protected EventBus editorBus;

	protected ListDataProvider<UIAttribute> dataProvider;

	protected AttributeHeader header;

	@Inject
	public CodeListAttributesPanel(@EditorBus EventBus editorBus) {

		this.editorBus = editorBus;
		
		this.dataProvider = new ListDataProvider<UIAttribute>();
		
		header = new AttributeHeader("");
		
		attributesGrid = new AttributesGrid(dataProvider) {
			
			@Override
			public Header<String> getHeader() {
				return header;
			}
		};

		setupColumns();

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
				header.setText(event.getRow().getName());
				attributesGrid.redrawHeaders();
				
				List<UIAttribute> attributes = dataProvider.getList();
				attributes.clear();
				attributes.addAll(event.getRow().getAttributes());
				dataProvider.refresh();
			}
		});

		editorBus.addHandler(AttributeSwitchedEvent.TYPE, new AttributeSwitchedEvent.AttributeSwitchedHandler() {

			@Override
			public void onAttributeSwitched(AttributeSwitchedEvent event) {
				UIAttribute attribute = event.getAttribute();
				Log.trace("onAttributeSwitched attribute: "+attribute+" type: "+event.getSwitchType());

				switch (event.getSwitchType()) {
					case TO_COLUMN: attributeAsColumn.add(attribute.getName()); break;
					case TO_NORMAL: attributeAsColumn.remove(attribute.getName()); break;
				}

				attributesGrid.refreshAttribute(attribute);

			}
		});
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
				return attributeAsColumn.contains(attribute.getName())?AttributeSwitchState.COLUMN:AttributeSwitchState.NORMAL;
			}
		};

		switchColumn.setFieldUpdater(new FieldUpdater<UIAttribute, AttributeSwitchState>() {
			@Override
			public void update(int index, UIAttribute object, AttributeSwitchState value) {
				switchAttribute(object, value);
				// Redraw the modified row.
				attributesGrid.redrawRow(index);
			}
		});
		attributesGrid.insertColumn(0, switchColumn);
		attributesGrid.setColumnWidth(0, 35, Unit.PX);

	}

	

	protected void switchAttribute(final UIAttribute attribute, AttributeSwitchState attributeSwitchState)
	{
		switch (attributeSwitchState) {
			case COLUMN: editorBus.fireEvent(new SwitchAttributeEvent(attribute, AttributeSwitchType.TO_NORMAL)); break;
			case NORMAL: editorBus.fireEvent(new SwitchAttributeEvent(attribute, AttributeSwitchType.TO_COLUMN)); break;
		}
	}

	protected void switchToAttribute(String attributeName)
	{
		Column<UIAttribute, String> column = attributesGrid.getAttributeColumn(attributeName);
		attributeAsColumn.remove(attributeName);
		attributesGrid.removeColumn(column);
		attributesGrid.removeUnusedDataGridColumns();
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
