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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
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

	protected ToggleButtonGroup buttonGroup = new ToggleButtonGroup();
	@UiField ToggleButton attributesButton;
	@UiField ToggleButton metadataButton;
	@UiField ToggleButton filtersButton;
	@UiField ToggleButton userButton;
	@UiField ToggleButton linkTypesButton;
	@UiField ToggleButton linksButton;
	
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
		
		buttonGroup.addButton(attributesButton);
		buttonGroup.addButton(metadataButton);
		buttonGroup.addButton(filtersButton);
		buttonGroup.addButton(userButton);
		buttonGroup.addButton(linkTypesButton);
		buttonGroup.addButton(linksButton);
		
		metadataButton.setDown(true);
		tools.showWidget(metadataPanel);
	}
	
	@UiHandler("attributesButton")
	protected void onAttributesButtonClicked(ClickEvent event)
	{
		tools.showWidget(attributesPanel);
	}
	
	@UiHandler("metadataButton")
	protected void onMetadataButtonClicked(ClickEvent event)
	{
		tools.showWidget(metadataPanel);
	}
	
	@UiHandler("filtersButton")
	protected void onFiltersButtonClicked(ClickEvent event)
	{
		tools.showWidget(filtersPanel);
	}
	
	@UiHandler("userButton")
	protected void onUserButtonClicked(ClickEvent event)
	{
		tools.showWidget(userPanel);
	}
	
	@UiHandler("linkTypesButton")
	protected void onLinkTypesButtonClicked(ClickEvent event)
	{
		tools.showWidget(linkTypesPanel);
	}
	
	@UiHandler("linksButton")
	protected void onLinkTsButtonClicked(ClickEvent event)
	{
		tools.showWidget(linksPanel);
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
