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
	
	public static final NavigationButtonConfiguration BACKWARD = new NavigationButtonConfiguration(WizardAction.BACK, WizardButton.BACK);
	public static final NavigationButtonConfiguration FORWARD = new NavigationButtonConfiguration(WizardAction.NEXT, WizardButton.NEXT);
	public static final NavigationButtonConfiguration IMPORT = new NavigationButtonConfiguration(WizardAction.NEXT, WizardButton.IMPORT);
	public static final NavigationButtonConfiguration NEW_IMPORT = new NavigationButtonConfiguration(WizardAction.NEW_IMPORT, WizardButton.NEW_IMPORT);
	public static final NavigationButtonConfiguration MANAGE = new NavigationButtonConfiguration(WizardAction.MANAGE, WizardButton.MANAGE);

	protected WizardAction action;
	protected WizardButton wizardButton;
	
	/**
	 * @param action
	 * @param wizardButton
	 */
	public NavigationButtonConfiguration(WizardAction action,
			WizardButton wizardButton) {
		this.action = action;
		this.wizardButton = wizardButton;
	}

	/**
	 * @return the action
	 */
	public WizardAction getAction() {
		return action;
	}

	/**
	 * @return the wizardButton
	 */
	public WizardButton getWizardButton() {
		return wizardButton;
	}

}
