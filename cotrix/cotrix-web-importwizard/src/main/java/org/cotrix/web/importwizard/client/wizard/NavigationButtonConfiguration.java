/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

import org.cotrix.web.importwizard.client.ImportWizardView.WizardButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NavigationButtonConfiguration {
	
	public enum ButtonAction {NONE, BACK, NEXT, NEW_IMPORT, MANAGE};
	
	/*public static final NavigationButtonConfiguration NONE = new NavigationButtonConfiguration(ButtonAction.NONE, null);*/
	public static final NavigationButtonConfiguration BACKWARD = new NavigationButtonConfiguration(ButtonAction.BACK, WizardButton.BACK);
	public static final NavigationButtonConfiguration FORWARD = new NavigationButtonConfiguration(ButtonAction.NEXT, WizardButton.NEXT);
	public static final NavigationButtonConfiguration IMPORT = new NavigationButtonConfiguration(ButtonAction.NEXT, WizardButton.IMPORT);
	public static final NavigationButtonConfiguration NEW_IMPORT = new NavigationButtonConfiguration(ButtonAction.NEW_IMPORT, WizardButton.NEW_IMPORT);
	public static final NavigationButtonConfiguration MANAGE = new NavigationButtonConfiguration(ButtonAction.MANAGE, WizardButton.MANAGE);

	protected ButtonAction action;
	protected WizardButton wizardButton;
	
	/**
	 * @param action
	 * @param wizardButton
	 */
	public NavigationButtonConfiguration(ButtonAction action,
			WizardButton wizardButton) {
		this.action = action;
		this.wizardButton = wizardButton;
	}

	/**
	 * @return the action
	 */
	public ButtonAction getAction() {
		return action;
	}

	/**
	 * @return the wizardButton
	 */
	public WizardButton getWizardButton() {
		return wizardButton;
	}

}
