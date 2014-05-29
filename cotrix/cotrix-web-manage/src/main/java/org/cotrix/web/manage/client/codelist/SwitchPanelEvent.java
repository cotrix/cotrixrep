/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SwitchPanelEvent extends GenericEvent {
	
	public static enum TargetPanel {CODES, METADATA};
	
	public static final SwitchPanelEvent CODES = new SwitchPanelEvent(TargetPanel.CODES);
	public static final SwitchPanelEvent METADATA = new SwitchPanelEvent(TargetPanel.METADATA);
	
	private TargetPanel targetPanel;

	private SwitchPanelEvent(TargetPanel targetPanel) {
		this.targetPanel = targetPanel;
	}

	public TargetPanel getTargetPanel() {
		return targetPanel;
	}

}
