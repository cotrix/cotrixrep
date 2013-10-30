/**
 * 
 */
package org.cotrix.web.share.client.wizard.step;

import org.cotrix.web.share.client.wizard.progresstracker.ProgressTracker.ProgressStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class VisualStepConfiguration {
	
	protected ProgressStep label;
	protected String title;
	protected String subtitle;
	protected StepButton[] buttons;
	
	/**
	 * @param label
	 * @param title
	 * @param backwardButton
	 * @param forwardButton
	 */
	public VisualStepConfiguration(ProgressStep label, String title, String subtitle,
			StepButton ... buttons) {
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
	public StepButton[] getButtons() {
		return buttons;
	}

	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(StepButton ... buttons) {
		this.buttons = buttons;
	}
}
