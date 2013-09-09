package org.cotrix.web.importwizard.client.step;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface WizardStep {

	public abstract String getId();

	/**
	 * Notify the step controller that the user want leave it.
	 * @return
	 */
	public abstract boolean leave();

}