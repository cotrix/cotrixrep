/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NavigationButtonConfiguration {
	
	public enum ButtonAction {NONE, BACK, NEXT, NEW_IMPORT, MANAGE};
	
	public static final NavigationButtonConfiguration NONE = new NavigationButtonConfiguration(null, ButtonAction.NONE);
	public static final NavigationButtonConfiguration DEFAULT_BACKWARD = new NavigationButtonConfiguration("Back", ButtonAction.BACK);
	public static final NavigationButtonConfiguration DEFAULT_FORWARD = new NavigationButtonConfiguration("Next", ButtonAction.NEXT);

	protected String label;
	protected ButtonAction action;

	/**
	 * @param label
	 * @param action
	 */
	public NavigationButtonConfiguration(String label, ButtonAction action) {
		this.label = label;
		this.action = action;
	}

	public String getLabel()
	{
		return label;
	}

	/**
	 * @return the action
	 */
	public ButtonAction getAction() {
		return action;
	}
	
}
