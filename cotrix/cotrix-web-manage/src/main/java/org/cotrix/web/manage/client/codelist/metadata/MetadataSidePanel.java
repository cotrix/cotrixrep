package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.manage.client.codelist.common.side.SidePanelContainer;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.inject.Inject;

import static org.cotrix.web.manage.client.codelist.common.Icons.icons;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataSidePanel extends ResizeComposite {

	@Inject
	private SidePanelContainer panel;

	@Inject
	private AttributesPanel attributesPanel;

	@Inject
	private LogbookPanel logbookPanel;
	
	@Inject
	private TasksPanel tasksPanel;
	
	@Inject
	private CotrixManagerResources resources;

	@Inject
	private void init() {
		initWidget(panel);
		
			panel.addPanel(icons.attribute(), icons.attributeDisabled(), "Codelist Attributes", attributesPanel);
			panel.addPanel(icons.logBook(), icons.logBookDisabled(), "Codelist Logbook", logbookPanel);
			panel.addPanel(icons.task(), icons.taskDisabled(), "Codelist Tasks", tasksPanel);
			
			panel.showPanel(attributesPanel);
	}

	public AttributesPanel getAttributesPanel() {
		return attributesPanel;
	}
}
