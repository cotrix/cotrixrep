/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class WizardStepConfiguration {
	
	protected ProgressStep label;
	protected String title;
	protected String subtitle;
	protected NavigationButtonConfiguration[] buttons;
	
	/**
	 * @param label
	 * @param title
	 * @param backwardButton
	 * @param forwardButton
	 */
	public WizardStepConfiguration(ProgressStep label, String title, String subtitle,
			NavigationButtonConfiguration ... buttons) {
		this.label = label;
		this.title = title;
		this.subtitle = subtitle;
		this.buttons = buttons;
	}

	/**
	 * @return the label
	 */
	public ProgressStep getLabel() {
		return label;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return the buttons
	 */
	public NavigationButtonConfiguration[] getButtons() {
		return buttons;
	}

	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(NavigationButtonConfiguration ... buttons) {
		this.buttons = buttons;
	}
}
