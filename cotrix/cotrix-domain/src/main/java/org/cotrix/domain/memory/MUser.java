package org.cotrix.domain.memory;

import static org.cotrix.common.CommonUtils.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cotrix.action.Action;
import org.cotrix.domain.common.Status;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;

public class MUser extends MIdentified implements User.State {

	private String userName;
	
	private String fullName;
	
	private String email;
	
	private Set<Action> permissions = new HashSet<Action>();
	
	private Set<Role> roles = new HashSet<Role>();
	
	
	//----------------------------------------------------
	
	public MUser() {}
	
	public MUser(String id) {
		super(id);
	}
	
	public MUser(String id, Status status) {
		super(id,status);
	}
	
	//----------------------------------------------------
	
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
	
		this.permissions = new HashSet<>(permissions);
	}
	
	public Collection<Action> permissions() {
		
		synchronized (permissions) {
			return new HashSet<Action>(permissions);
		}
		
		
	}
	
	public void add(Action permission) {
		
		notNull("permission",permission);
		
		synchronized (permissions) {
			this.permissions.add(permission);
		}
		
	}

	public void add(Role role) {
		
		notNull("role",role);
		
		synchronized (roles) {
			this.roles.add(role);
		}
		
	}
	
	public void remove(Role role) {
		
		notNull("role",role);
		
		synchronized (roles) {
			this.roles.remove(role);
		}
		
	}
	
	public void remove(Action permission) {
		
		notNull("role",permission);
		
		synchronized (permissions) {
			this.permissions.remove(permission);
		}
		
		
	}
	
	public void roles(Collection<Role> roles) {
		
		notNull("roles",roles);
		
		synchronized (roles) {
			this.roles.clear();
		}
		
		for (Role role : roles)
			add(role);
	}
	
	public Collection<Role> roles() {
		synchronized (roles) {
			return new HashSet<Role>(roles);			
		}
	}
	
	@Override
	public User.Private entity() {
		return new User.Private(this);
	}


	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof User.State))
			return false;
		User.State other = (User.State) obj;
		if (email == null) {
			if (other.email() != null)
				return false;
		} else if (!email.equals(other.email()))
			return false;
		if (fullName == null) {
			if (other.fullName() != null)
				return false;
		} else if (!fullName.equals(other.fullName()))
			return false;
		if (permissions == null) {
			if (other.permissions() != null)
				return false;
		} else if (!permissions.equals(other.permissions()))
			return false;
		if (roles == null) {
			if (other.roles() != null)
				return false;
		} else if (!roles.equals(other.roles()))
			return false;
		if (userName == null) {
			if (other.name() != null)
				return false;
		} else if (!userName.equals(other.name()))
			return false;
		
		return true;
	}
	
	

}
