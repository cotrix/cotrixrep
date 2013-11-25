package org.cotrix.domain.user;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;

/**
 * A set of permissions and inherited roles over resource of given types. 
 * 
 * @author Fabio Simeoni
 *
 */
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
	 * Returns the type of this role.
	 * @return the type
	 */
	ResourceType type();
	
	/**
	 * Returns a clone of this role associated with a given resource.
	 * @param resource the resource
	 * @return the clone
	 */
	Role on(String resource);

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
	 * If this role is associated with a specific resource, then all the permissions that have the same type as this role are
	 * specialised to the same resource.
	 * 
	 * @return the permissions
	 */
	Collection<Action> permissions();
	
	
	/**
	 * Returns all the roles directly or indirectly inherited by this role.
	 * <P>
	 * If this role is associated with a specific resource, then all the roles that have the same type as this role are
	 * specialised to the same resource.
	 * 
	 * @return the inherited roles
	 */
	Collection<Role> roles(); 

}