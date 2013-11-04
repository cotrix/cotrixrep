package org.cotrix.user;

import static org.cotrix.action.Action.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.action.Action;

/**
 * A user role.
 * <p>
 * A role is a binding between a {@link RoleModel} and a resource.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class Role {

	private final RoleModel model;
	private final String resource;

	/**
	 * Creates a role with a given model and no specific resource.
	 * 
	 * @param model the model
	 */
	public Role(RoleModel model) {
		this(model, any);
	}

	/**
	 * Creates a role with a given model and a given resource.
	 * 
	 * @param model the model
	 * @param resource the resource
	 */
	public Role(RoleModel model, String resource) {
		notNull("model", model);
		notNull("resource", resource);

		this.model = model;
		this.resource = resource;
	}

	/**
	 * Returns the identifier of the resource bound to this role.
	 * 
	 * @return the resource identifier
	 */
	public String resource() {
		return resource;
	}
	
	/**
	 * Returns the name of this role.
	 * @return the name
	 */
	public String name() {
		return model.name();
	}
	
	/**
	 * Returns the description of this role.
	 * @return the description.
	 */
	public String description() {
		return model.fullName();
	}

	/**
	 * Returns <code>true</code> if this role is a specialisation of another role.
	 * 
	 * @param role the role
	 * @return <code>true</code> if this role is a specialisation of the given role
	 */
	public boolean is(Role role) {
		return model.is(role);
	}

	/**
	 * Returns the permissions directly or indirectly assigned to this role.
	 * <p>
	 * If this role is associated with a specific resource, then all the permissions are specialised to it.
	 * 
	 * @return the permissions
	 */
	public Collection<Action> permissions() {
		
		Collection<Action> permissions = new ArrayList<Action>();
		for (Action p : model.permissions()) {
			
			if (permissions.contains(p)) //avoids duplicates
				continue;
				
			permissions.add(resource==any?p:p.on(resource)); //instantiates only if needed
		}
		return permissions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[role=" + model + ", resource=" + resource + "]";
	}

}
