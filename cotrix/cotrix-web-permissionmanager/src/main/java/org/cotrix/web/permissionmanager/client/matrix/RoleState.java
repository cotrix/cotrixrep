/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RoleState {
	
	boolean enabled;
	boolean checked;
	
	/**
	 * @param enabled
	 * @param checked
	 */
	public RoleState(boolean enabled, boolean checked) {
		this.enabled = enabled;
		this.checked = checked;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
