/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NavigationButtonConfiguration {
	
	public static final NavigationButtonConfiguration NONE = new NavigationButtonConfiguration(null);
	public static final NavigationButtonConfiguration DEFAULT_BACKWARD = new NavigationButtonConfiguration("Back");
	public static final NavigationButtonConfiguration DEFAULT_FORWARD = new NavigationButtonConfiguration("Next");

	protected String label;
	
	/**
	 * @param label
	 */
	public NavigationButtonConfiguration(String label) {
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
	
}
