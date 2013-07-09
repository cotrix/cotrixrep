package org.cotrix.web.importwizard.client.step;

/**
 * Represents a Wizard step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface WizardStep {
	
	/**
	 * Checks if the step information are valid or not.
	 * @return
	 */
	public boolean isValid();
	
	/**
	 * Returns the step label.
	 * @return
	 */
	public String getLabel();

}
