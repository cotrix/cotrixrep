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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (checked ? 1231 : 1237);
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + (loading ? 1231 : 1237);
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleState other = (RoleState) obj;
		if (checked != other.checked)
			return false;
		if (enabled != other.enabled)
			return false;
		if (loading != other.loading)
			return false;
		return true;
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
