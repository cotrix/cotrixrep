package org.cotrix.domain.user;

import static org.cotrix.action.Action.*;
import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;

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

	private final User model;
	
	private final String resource;
	
	private final ResourceType type;

	
	public DefaultRole(User model,ResourceType type) {
		this(model, type, any);
	}


	/**
	 * Creates a role with a given model and a given resource.
	 * 
	 * @param model the model
	 * @param resource the resource
	 */
	public DefaultRole(User model, ResourceType type, String resource) {
		
		notNull("role model", model);
		notNull("type", type);
		notNull("resource", resource);

		this.model = model;
		this.type=type;
		this.resource = resource;
	}
	
	@Override
	public ResourceType type() {
		return type;
	}
	
    @Override
    public Role on(String resource) {
    	return new DefaultRole(model,type,resource);
    }
    
	@Override
	public String resource() {
		return resource;
	}
	
	@Override
	public String name() {
		return model.name();
	}
	
	@Override
	public String description() {
		return model.fullName();
	}

	@Override
	public Collection<Role> roles() {
		Collection<Role> roles = new HashSet<Role>();
		
		//specialise inherited roles of same type to bound resource
		for (Role r : model.roles())
			if (r.type().equals(this.type()))
				roles.add(r.on(this.resource()));
			else
				roles.add(r);
			
		return roles;
	}

	@Override
	public boolean is(Role role) {
		return roles().contains(role);
	}
	
	@Override
	public Collection<Action> permissions() {
		
		Collection<Action> permissions = new ArrayList<Action>();
		
		for (Action p : model.permissions()) {
			
			Action a =  p.type() == this.type() ? p.on(resource):p;
			
			if (permissions.contains(a)) //avoids duplicates
				continue;
			
			permissions.add(a);
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
