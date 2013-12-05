package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.cotrix.action.Action;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;

public class UserMS extends IdentifiedMS implements User.State {

	private String userName;
	private String fullName;
	private String email;
	
	private final Set<Action> permissions = new LinkedHashSet<Action>();
	private final Set<Role> roles = new LinkedHashSet<Role>();
	
	public UserMS() {
		super(null);
	}
	
	public UserMS(String id) {
		super(id);
	}
	
	public void name(String name) {
		valid("full name",name);
		this.userName=name;
	}
	
	public String name() {
		return userName;
	}
	
	public String email() {
		return email;
	}
	
	public void email(String email) {
		validEmail(email);
		this.email = email;
	}

	public String fullName() {
		return fullName;
	}
	
	public void fullName(String name) {
		valid("username",name);
		this.fullName=name;
	}
	
	public void permissions(Collection<Action> permissions) {

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
	
	public void roles(Collection<Role> roles) {
		
		notNull("roles",roles);
		
		for (Role role : roles)
			add(role);
	}
	
	public Collection<Role> roles() {
		return roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof UserMS))
			return false;
		UserMS other = (UserMS) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	

}
