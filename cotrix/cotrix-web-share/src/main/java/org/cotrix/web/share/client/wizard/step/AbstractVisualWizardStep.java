/**
 * 
 */
package org.cotrix.web.share.client.wizard.step;

import org.cotrix.web.share.client.wizard.progresstracker.ProgressTracker.ProgressStep;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractVisualWizardStep implements VisualWizardStep {

	protected String id;
	protected VisualStepConfiguration configuration;

	/**
	 * @param id
	 */
	public AbstractVisualWizardStep(String id, ProgressStep label, String title, String subtitle,
			StepButton ... buttons) {
		this.id = id;
		this.configuration = new VisualStepConfiguration(label, title, subtitle, buttons);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return the configuration
	 */
	public VisualStepConfiguration getConfiguration() {
		return configuration;
	}

}
