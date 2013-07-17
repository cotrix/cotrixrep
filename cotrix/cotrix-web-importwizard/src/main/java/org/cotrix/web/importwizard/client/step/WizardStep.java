package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.Presenter;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;

/**
 * Represents a Wizard step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface WizardStep extends Presenter {
	
	public String getId();
	
	/**
	 * Checks if the step is complete.
	 * @return
	 */
	public boolean isComplete();
	
	/**
	 * Gets the step configuration for this step.
	 * @return
	 */
	public WizardStepConfiguration getConfiguration();

}
