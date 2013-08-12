/**
 * 
 */
package org.cotrix.web.importwizard.client.step;

import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractWizardStep implements WizardStep {

	protected String id;
	protected WizardStepConfiguration configuration;

	/**
	 * @param id
	 */
	public AbstractWizardStep(String id, String label, String title, String subtitle,
			NavigationButtonConfiguration backwardButton,
			NavigationButtonConfiguration forwardButton) {
		this.id = id;
		this.configuration = new WizardStepConfiguration(label, title, subtitle, backwardButton, forwardButton);
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
