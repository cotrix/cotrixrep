package org.cotrix.web.wizard.client.step;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Represents a Wizard step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface VisualWizardStep extends WizardStep {
	
	public void go(final HasWidgets container);
	
	/**
	 * Gets the step configuration for this step.
	 * @return
	 */
	public VisualStepConfiguration getConfiguration();

}
