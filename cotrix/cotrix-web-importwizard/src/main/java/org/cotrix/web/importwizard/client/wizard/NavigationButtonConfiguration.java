/**
 * 
 */
package org.cotrix.web.importwizard.client.wizard;

import org.cotrix.web.importwizard.client.resources.Resources;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NavigationButtonConfiguration {
	
	public enum ButtonAction {NONE, BACK, NEXT, NEW_IMPORT, MANAGE};
	
	public static final NavigationButtonConfiguration NONE = new NavigationButtonConfiguration(null, ButtonAction.NONE, null);
	public static final NavigationButtonConfiguration DEFAULT_BACKWARD = new NavigationButtonConfiguration("Back", ButtonAction.BACK, Resources.INSTANCE.css().grayButton());
	public static final NavigationButtonConfiguration DEFAULT_FORWARD = new NavigationButtonConfiguration("Next", ButtonAction.NEXT, Resources.INSTANCE.css().blueButton());

	protected String label;
	protected ButtonAction action;
	protected String style;

	/**
	 * @param label
	 * @param action
	 * @param style
	 */
	public NavigationButtonConfiguration(String label, ButtonAction action,
			String style) {
		this.label = label;
		this.action = action;
		this.style = style;
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

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
}
