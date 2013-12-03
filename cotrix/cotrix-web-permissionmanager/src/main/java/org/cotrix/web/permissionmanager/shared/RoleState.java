/**
 * 
 */
package org.cotrix.web.permissionmanager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RoleState implements IsSerializable {
	
	boolean enabled;
	boolean checked;
	boolean loading;
	
	public RoleState() {}
	
	/**
	 * @param enabled
	 * @param checked
	 */
	public RoleState(boolean enabled, boolean checked, boolean loading) {
		this.enabled = enabled;
		this.checked = checked;
		this.loading = loading;
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

	/**
	 * @return the loading
	 */
	public boolean isLoading() {
		return loading;
	}

	/**
	 * @param loading the loading to set
	 */
	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleState [enabled=");
		builder.append(enabled);
		builder.append(", checked=");
		builder.append(checked);
		builder.append(", loading=");
		builder.append(loading);
		builder.append("]");
		return builder.toString();
	}
}
