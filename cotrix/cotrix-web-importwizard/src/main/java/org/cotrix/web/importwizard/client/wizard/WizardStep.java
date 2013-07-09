/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface WizardStep {
	
	/**
	 * Returns the step identified.
	 * @return
	 */
	public String getId();
	
	/**
	 * Returns the step label.
	 * @return
	 */
	public String getLabel();
	
	
	/**
	 * Returns the step title.
	 * @return
	 */
	public String getTitle();
	
	public NavigationButtonConfiguration getFowardButton();

}
