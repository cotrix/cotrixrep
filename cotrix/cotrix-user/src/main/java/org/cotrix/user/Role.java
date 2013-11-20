package org.cotrix.user;

import java.util.Collection;

import org.cotrix.action.Action;

public interface Role {

	/**
	 * Returns the identifier of the resource bound to this role.
	 * 
	 * @return the resource identifier
	 */
	String resource();

	/**
	 * Returns the name of this role.
	 * @return the name
	 */
	String name();

	/**
	 * Returns the description of this role.
	 * @return the description.
	 */
	String description();

	/**
	 * Returns <code>true</code> if this role is a specialisation of another role.
	 * 
	 * @param role the role
	 * @return <code>true</code> if this role is a specialisation of the given role
	 */
	boolean is(Role role);

	/**
	 * Returns the permissions directly or indirectly assigned to this role.
	 * <p>
	 * If this role is associated with a specific resource, then all the permissions are specialised to it.
	 * 
	 * @return the permissions
	 */
	Collection<Action> permissions();

}