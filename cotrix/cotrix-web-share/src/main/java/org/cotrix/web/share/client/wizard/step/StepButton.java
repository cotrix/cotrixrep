/**
 * 
 */
package org.cotrix.web.share.client.wizard.step;

import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.WizardView.WizardButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class StepButton {
	
	protected WizardAction action;
	protected WizardButton button;
	
	/**
	 * @param action
	 * @param button
	 */
	public StepButton(WizardAction action, WizardButton button) {
		this.action = action;
		this.button = button;
	}

	/**
	 * @return the action
	 */
	public WizardAction getAction() {
		return action;
	}

	/**
	 * @return the button
	 */
	public WizardButton getButton() {
		return button;
	}

}
