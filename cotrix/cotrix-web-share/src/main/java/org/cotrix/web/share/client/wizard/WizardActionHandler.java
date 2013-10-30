/**
 * 
 */
package org.cotrix.web.share.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface WizardActionHandler {
	
	public boolean handle(WizardAction action, WizardController controller);

}
