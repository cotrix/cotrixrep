package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.cotrix.action.Action;
import org.cotrix.domain.user.Role;

public class UserPO extends DomainPO {

	private String userName;
	private String fullName;
	
	private final Set<Action> permissions = new LinkedHashSet<Action>();
	private final Set<Role> roles = new LinkedHashSet<Role>();
	
	public UserPO(String id) {
		super(id);
	}
	
	public void setName(String name) {
		valid("full name",name);
		this.userName=name;
	}
	
	public String name() {
		return userName;
	}

	public String fullName() {
		return fullName;
	}
	
	public void setFullName(String name) {
		valid("username",name);
		this.fullName=name;
	}
	
	public void setPermissions(Collection<Action> permissions) {

		notNull("permissions",permissions);
	
		this.permissions.addAll(permissions);
	}
	
	public Collection<Action> permissions() {
		
		return permissions;
		
	}
	
	public void add(Role role) {
		
		notNull("role",role);
		
		this.roles.add(role);
		
	}
	
	public void remove(Role role) {
		
		notNull("role",role);
		
		this.roles.remove(role);
		
	}
	
	public void setRoles(Collection<Role> roles) {
		
		notNull("roles",roles);
		
		for (Role role : roles)
			add(role);
	}
	
	public Collection<Role> roles() {
		return roles;
	}
}
