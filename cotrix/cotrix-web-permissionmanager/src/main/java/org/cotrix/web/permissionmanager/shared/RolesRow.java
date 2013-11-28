/**
 * 
 */
package org.cotrix.web.permissionmanager.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RolesRow implements IsSerializable {
	
	protected UIUser user;
	protected List<String> roles;
	protected boolean isCurrentUser;
	
	public RolesRow(){}

	/**
	 * @param user
	 * @param roles
	 * @param isCurrentUser
	 */
	public RolesRow(UIUser user, List<String> roles, boolean isCurrentUser) {
		this.user = user;
		this.roles = roles;
		this.isCurrentUser = isCurrentUser;
	}

	/**
	 * @return the user
	 */
	public UIUser getUser() {
		return user;
	}

	public boolean hasRole(String role) {
		return roles.contains(role);
	}
	
	public void addRole(String role) {
		roles.add(role);
	}
	
	public void removeRole(String role) {
		roles.remove(role);
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @return the isCurrentUser
	 */
	public boolean isCurrentUser() {
		return isCurrentUser;
	}

	/**
	 * @param isCurrentUser the isCurrentUser to set
	 */
	public void setCurrentUser(boolean isCurrentUser) {
		this.isCurrentUser = isCurrentUser;
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
		builder.append(", isCurrentUser=");
		builder.append(isCurrentUser);
		builder.append("]");
		return builder.toString();
	}
}
