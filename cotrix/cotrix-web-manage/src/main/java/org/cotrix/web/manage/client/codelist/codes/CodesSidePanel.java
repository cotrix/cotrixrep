package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.manage.client.codelist.codes.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesSidePanel extends ResizeComposite {

	interface CodesSidePanelEventBinder extends EventBinder<CodesSidePanel> {}
	
	@Inject
	private SidePanel panel;
	
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
	
		panel.addPanel(resources.attributesSelected(), resources.attributesUnselected(), "Code Attributes", attributesPanel);
		panel.addPanel(resources.linksSelected(), resources.linksUnselected(), "Code Links", linksPanel);
		panel.addPanel(resources.markersSelected(), resources.markersUnselected(), "Code Markers", markersPanel);
		
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
