/**
 * 
 */
package org.cotrix.web.permissionmanager.shared;

import java.util.Map;

import org.cotrix.web.share.shared.UIUser;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RolesRow implements IsSerializable {
	
	public static final String USER_NAME_FIELD = "NAME";
	
	public static final RolesRow DELETED = new RolesRow(true);
	
	protected UIUser user;
	protected Map<String, RoleState> roles;
	protected boolean loading = false;
	protected boolean deleted = false;
	
	public RolesRow(){}

	/**
	 * @param deleted
	 */
	private RolesRow(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @param user
	 * @param roles
	 */
	public RolesRow(UIUser user, Map<String, RoleState> roles) {
		this.user = user;
		this.roles = roles;
	}

	/**
	 * @return the user
	 */
	public UIUser getUser() {
		return user;
	}
	
	/**
	 * @return the loading
	 */
	public boolean isLoading() {
		return loading;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param loading the loading to set
	 */
	public void setLoading(boolean loading) {
		this.loading = loading;
		for (RoleState state:roles.values()) state.setLoading(loading);
	}

	public boolean hasRole(String role) {
		return roles.containsKey(role);
	}
	
	public RoleState getRoleState(String role) {
		return roles.get(role);
	}
	
	public void removeRole(String role) {
		roles.remove(role);
	}
	
	public boolean noRoles() {
		for (RoleState state:roles.values()) if (state.checked) return false;
		return true;
	}

	/**
	 * @return the roles
	 */
	public Map<String, RoleState> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Map<String, RoleState> roles) {
		this.roles = roles;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		RolesRow other = (RolesRow) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RolesRow [user=");
		builder.append(user);
		builder.append(", roles=");
		builder.append(roles);
		builder.append("]");
		return builder.toString();
	}
}
