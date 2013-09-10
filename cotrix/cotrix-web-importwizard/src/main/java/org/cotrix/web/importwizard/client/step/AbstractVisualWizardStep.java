/**
 * 
 */
package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.progresstracker.ProgressTracker.ProgressStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractVisualWizardStep implements VisualWizardStep {

	protected String id;
	protected WizardStepConfiguration configuration;

	/**
	 * @param id
	 */
	public AbstractVisualWizardStep(String id, ProgressStep label, String title, String subtitle,
			NavigationButtonConfiguration ... buttons) {
		this.id = id;
		this.configuration = new WizardStepConfiguration(label, title, subtitle, buttons);
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
	public WizardStepConfiguration getConfiguration() {
		return configuration;
	}

}
