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

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ToggleButtonGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistSidePanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodelistSidePanel> { }
	
	private static Binder uiBinder = GWT.create(Binder.class);
	
	interface Style extends CssResource {
		String buttonBarPanelSelected();
	}

	private enum Bar{LEFT, RIGHT}
	
	@UiField FocusPanel leftBar;
	@UiField FocusPanel rightBar;
	@UiField Style style;
	
	private ToggleButtonGroup leftButtonGroup = new ToggleButtonGroup();
	@UiField ToggleButton attributesButton;
	@UiField ToggleButton linksButton;
	
	private ToggleButtonGroup rightButtonGroup = new ToggleButtonGroup();
	@UiField ToggleButton metadataButton;
	@UiField ToggleButton filtersButton;
	@UiField ToggleButton userButton;
	@UiField ToggleButton linkTypesButton;

	
	@UiField DeckLayoutPanel tools;
	
	@Inject
	@UiField(provided=true) CodelistAttributesPanel attributesPanel;
	
	@Inject
	@UiField(provided=true) CodelistMetadataPanel metadataPanel;
	@UiField FiltersPanel filtersPanel;
	@UiField UserPreferencesPanel userPanel;
	
	@Inject
	@UiField(provided=true) CodelistLinkTypesPanel linkTypesPanel;
	
	@Inject
	@UiField(provided=true) CodelistLinksPanel linksPanel;

	@Inject
	private void init() {
		initWidget(uiBinder.createAndBindUi(this));
		tools.showWidget(attributesPanel);
		
		leftButtonGroup.addButton(attributesButton);
		leftButtonGroup.addButton(linksButton);
		leftButtonGroup.setDown(attributesButton);
		
		rightButtonGroup.addButton(metadataButton);
		rightButtonGroup.addButton(filtersButton);
		rightButtonGroup.addButton(userButton);
		rightButtonGroup.addButton(linkTypesButton);
		rightButtonGroup.setDown(metadataButton);
				
		leftBar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				updateSelection(Bar.LEFT);				
			}
		});
		rightBar.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				updateSelection(Bar.RIGHT);				
			}
		});
		
		updateSelection(Bar.RIGHT);
	}
	
	private void updateSelection(Bar bar) {
		switch (bar) {
			case LEFT: {
				leftBar.addStyleName(style.buttonBarPanelSelected());	
				leftButtonGroup.setEnabled(true);
				ToggleButton button = leftButtonGroup.getLastSelected();
				if (button!=null) {
					Widget panel = getPanel(button);
					tools.showWidget(panel);
				}
				
				rightBar.removeStyleName(style.buttonBarPanelSelected());
				rightButtonGroup.setEnabled(false);
			} break;

			case RIGHT: {
				leftBar.removeStyleName(style.buttonBarPanelSelected());
				leftButtonGroup.setEnabled(false);
				
				rightBar.addStyleName(style.buttonBarPanelSelected());	
				rightButtonGroup.setEnabled(true);
				ToggleButton button = rightButtonGroup.getLastSelected();
				if (button!=null) {
					Widget panel = getPanel(button);
					tools.showWidget(panel);
				}
			} break;
		}
		
	}
	
	@UiHandler({"attributesButton", "metadataButton", "filtersButton","userButton","linkTypesButton","linksButton"})
	protected void onButtonClicked(ClickEvent event)
	{
		Widget panel = getPanel((ToggleButton) event.getSource());
		tools.showWidget(panel);
	}
	
	private Widget getPanel(ToggleButton button) {
		if (button == attributesButton) return attributesPanel;
		if (button == metadataButton) return metadataPanel;
		if (button == filtersButton) return filtersPanel;
		if (button == userButton) return userPanel;
		if (button == linkTypesButton) return linkTypesPanel;
		if (button == linksButton) return linksPanel;
		throw new IllegalArgumentException("Unknwown button "+button);
	}

	/**
	 * @return the attributesPanel
	 */
	public CodelistAttributesPanel getAttributesPanel() {
		return attributesPanel;
	}

	/**
	 * @return the metadataPanel
	 */
	public CodelistMetadataPanel getMetadataPanel() {
		return metadataPanel;
	}

	public HasEditing getLinkTypesPanel() {
		return linkTypesPanel;
	}
	
	public HasEditing getLinksPanel() {
		return linksPanel;
	}

}
