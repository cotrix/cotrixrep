package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.inject.Inject;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataSidePanel extends ResizeComposite {

	@Inject
	private SidePanel panel;

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
		
			panel.addPanel(resources.attributesSelected(), resources.attributesUnselected(), "Codelist Attributes", attributesPanel);
			panel.addPanel(resources.logbookSelected(), resources.logbookUnselected(), "Codelist Logbook", logbookPanel);
			panel.addPanel(resources.tasksSelected(), resources.tasksUnselected(), "Codelist Tasks", tasksPanel);
			
			panel.showPanel(attributesPanel);
	}

	public AttributesPanel getAttributesPanel() {
		return attributesPanel;
	}
}
