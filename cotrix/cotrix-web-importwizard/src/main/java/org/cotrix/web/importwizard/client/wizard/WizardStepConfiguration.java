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
	protected NavigationButtonConfiguration backwardButton;
	protected NavigationButtonConfiguration forwardButton;
	
	/**
	 * @param label
	 * @param title
	 * @param backwardButton
	 * @param forwardButton
	 */
	public WizardStepConfiguration(ProgressStep label, String title, String subtitle,
			NavigationButtonConfiguration backwardButton,
			NavigationButtonConfiguration forwardButton) {
		this.label = label;
		this.title = title;
		this.subtitle = subtitle;
		this.backwardButton = backwardButton;
		this.forwardButton = forwardButton;
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
	 * @return the backwardButton
	 */
	public NavigationButtonConfiguration getBackwardButton() {
		return backwardButton;
	}
	
	/**
	 * @return the fowardButton
	 */
	public NavigationButtonConfiguration getForwardButton() {
		return forwardButton;
	}

	/**
	 * @param backwardButton the backwardButton to set
	 */
	public void setBackwardButton(NavigationButtonConfiguration backwardButton) {
		this.backwardButton = backwardButton;
	}

	/**
	 * @param forwardButton the forwardButton to set
	 */
	public void setForwardButton(NavigationButtonConfiguration forwardButton) {
		this.forwardButton = forwardButton;
	}
}
