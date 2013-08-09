/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class WizardStepConfiguration {
	
	protected String label;
	protected String title;
	protected NavigationButtonConfiguration backwardButton;
	protected NavigationButtonConfiguration forwardButton;
	
	/**
	 * @param label
	 * @param title
	 * @param backwardButton
	 * @param forwardButton
	 */
	public WizardStepConfiguration(String label, String title,
			NavigationButtonConfiguration backwardButton,
			NavigationButtonConfiguration forwardButton) {
		this.label = label;
		this.title = title;
		this.backwardButton = backwardButton;
		this.forwardButton = forwardButton;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
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
