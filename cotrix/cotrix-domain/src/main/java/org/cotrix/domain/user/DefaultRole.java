package org.cotrix.domain.user;

import static org.cotrix.action.Action.*;
import static org.cotrix.common.Utils.*;

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
		
		return adaptRoles(model.roles());
	}
	
	@Override
	public Collection<Role> directRoles() {
		return adaptRoles(model.directRoles());
	}
	
	@Override
	public boolean is(Role role) {  //does this role imply another?
		
		notNull("role", role);
		
		//equivalent to: is the other role implied by the closure of this one?
		return role.isIn(roles());
	}
	
	@Override
	public boolean isIn(Collection<Role> roles) { //is this role _implied by_ another? 
		
		notNull("roles", roles);
		
		for (Role r : roles) 
			if (r.equals(this) || //is it a match? 
					r.equals(this.on(any)) || //maybe it's resource specific and is template is a match?
					r.is(this)) //maybe is more general?
				return true;

		return false;
	}
	
	@Override
	public Collection<Action> permissions() {
		
		return adaptPermissions(model.permissions());
	}
	
	@Override
	public Collection<Action> directPermissions() {
		
		return adaptPermissions(model.directPermissions());
		
	}
	
	private Collection<Action> adaptPermissions(Collection<Action> permissions) {
		
		Collection<Action> adapted = new HashSet<>();
		
		//specialise inherited permissions of same type to bound resource
		for (Action p : permissions) {
			
			Action a =  p.type() == this.type() ? p.on(resource):p;
			
			adapted.add(a);
		}
		
		return adapted;
	}
	
	private Collection<Role> adaptRoles(Collection<Role> roles) {
		
		Collection<Role> adapted = new HashSet<Role>();
		
		//specialise inherited roles of same type to bound resource
		for (Role r : roles) {
			
			Role adaptedRole = r.type() == this.type()? r.on(this.resource()): r;
		
			adapted.add(adaptedRole);
		}
		
		return adapted;
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
