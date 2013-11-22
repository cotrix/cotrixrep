package org.cotrix.user;

import java.util.Collection;
import java.util.HashSet;

import org.cotrix.action.Action;

/**
 * A structure of roles and permissions of the same type and over the same resource.
 * @author Fabio Simeoni
 *
 */
public class RolesAndPermissions {

	private Collection<String> roles = new HashSet<String>();
	private Collection<Action> permissions = new HashSet<Action>();
	
	
	public Collection<Action> permissions() {
		return permissions;
	}
	
	public Collection<String> roles() {
		return roles;
	}
	
	@Override
	public String toString() {
		return "[roles=" + (roles != null ? roles.toString() : null) + ", permissions="
				+ (permissions != null ? permissions.toString() : null) + "]";
	}
}
