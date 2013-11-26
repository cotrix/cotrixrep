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
	
	public RolesRow(){}

	/**
	 * @param user
	 * @param roles
	 */
	public RolesRow(UIUser user, List<String> roles) {
		this.user = user;
		this.roles = roles;
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

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
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
