/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.matrix;

import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RolesRow {
	
	protected User user;
	protected List<String> roles;

	/**
	 * @param user
	 * @param roles
	 */
	public RolesRow(User user, List<String> roles) {
		this.user = user;
		this.roles = roles;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	public boolean hasRole(String role) {
		return roles.contains(role);
	}
	
	public void addRole(String role) {
		roles.add(role);
	}

}
