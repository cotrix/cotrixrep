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

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListSidePanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodeListSidePanel> { }
	
	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField Button attributesButton;
	@UiField Button metadataButton;
	
	@UiField DeckLayoutPanel tools;
	@UiField CodeListAttributesPanel attributesPanel;
	@UiField CodeListMetadataPanel metadataPanel;

	public CodeListSidePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		tools.showWidget(attributesPanel);
	}
	
	@UiFactory
	protected CodeListAttributesPanel createCodeListAttributesPanel()
	{
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListAttributesPanel();
	}
	
	@UiFactory
	protected CodeListMetadataPanel createCodeListMetadataPanel()
	{
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListMetadataPanel();
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

}
