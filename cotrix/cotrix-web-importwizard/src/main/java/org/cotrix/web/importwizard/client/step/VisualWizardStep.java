package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;

/**
 * Represents a Wizard step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface VisualWizardStep extends WizardStep {
	
	/**
	 * Gets the step configuration for this step.
	 * @return
	 */
	public WizardStepConfiguration getConfiguration();

}
