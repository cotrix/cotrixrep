package org.cotrix.user.impl;

import static org.cotrix.action.Action.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.user.Role;
import org.cotrix.user.RoleModel;

/**
 * A user role.
 * <p>
 * A role is a binding between a {@link RoleModel} and a resource.
 * 
 * 
 * @author Fabio Simeoni
 * 
 */
public class DefaultRole implements Role {

	private final RoleModel model;
	
	private final String resource;

	/**
	 * Creates a role with a given model and no specific resource.
	 * 
	 * @param model the model
	 */
	public DefaultRole(RoleModel model) {
		this(model, any);
	}

	/**
	 * Creates a role with a given model and a given resource.
	 * 
	 * @param model the model
	 * @param resource the resource
	 */
	public DefaultRole(RoleModel model, String resource) {
		
		notNull("model", model);
		notNull("resource", resource);

		this.model = model;
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see org.cotrix.user.Role#resource()
	 */
	@Override
	public String resource() {
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see org.cotrix.user.Role#name()
	 */
	@Override
	public String name() {
		return model.name();
	}
	
	/* (non-Javadoc)
	 * @see org.cotrix.user.Role#description()
	 */
	@Override
	public String description() {
		return model.fullName();
	}

	/* (non-Javadoc)
	 * @see org.cotrix.user.Role#is(org.cotrix.user.Role)
	 */
	@Override
	public boolean is(Role role) {
		return model.is(role);
	}

	/* (non-Javadoc)
	 * @see org.cotrix.user.Role#permissions()
	 */
	@Override
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
		DefaultRole other = (DefaultRole) obj;
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
