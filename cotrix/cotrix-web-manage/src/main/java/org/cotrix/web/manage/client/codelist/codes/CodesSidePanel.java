package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.manage.client.codelist.codes.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.common.side.SidePanelContainer;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import static org.cotrix.web.manage.client.codelist.common.Icons.icons;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesSidePanel extends ResizeComposite {

	interface CodesSidePanelEventBinder extends EventBinder<CodesSidePanel> {}
	
	@Inject
	private SidePanelContainer panel;
	
	@Inject
	private AttributesPanel attributesPanel;

	@Inject
	private LinksPanel linksPanel;
	
	@Inject
	private MarkersPanel markersPanel;
	
	@Inject
	private CotrixManagerResources resources;

	@Inject
	private void init() {
		initWidget(panel);
	
		panel.addPanel(icons.attribute(), icons.attributeDisabled(), "Code Attributes", attributesPanel);
		panel.addPanel(icons.link(), icons.linkDisabled(), "Code Links", linksPanel);
		panel.addPanel(icons.marker(), icons.markerDisabled(), "Code Markers", markersPanel);
		
		panel.showPanel(attributesPanel);
		
		panel.setEmptyPanelMessage("No code selected");
		panel.showEmptyPanel(true);
	}
	
	@Inject
	protected void bind(CodesSidePanelEventBinder binder, @CodelistBus EventBus codelistBus) {
		binder.bindEventHandlers(this, codelistBus);
	}
	
	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		panel.showEmptyPanel(event.getCode() == null);
	}

	public AttributesPanel getAttributesPanel() {
		return attributesPanel;
	}
	
	public HasEditing getLinksPanel() {
		return linksPanel;
	}
}
