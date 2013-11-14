package org.cotrix.web.share.client.wizard.step;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface WizardStep {

	public String getId();

	/**
	 * Notify the step controller that the user want leave it.
	 * @return
	 */
	public boolean leave();

}