package org.cotrix.user.utils;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.user.Role;
import org.cotrix.user.RoleModel;
import org.cotrix.user.User;


public class CurrentUser implements User {

	private User test;
	
	public void set(User test) {
		this.test=test;
	}

	public String id() {
		return test.id();
	}

	public String name() {
		return test.name();
	}

	public String fullName() {
		return test.fullName();
	}

	public Collection<Action> permissions() {
		return test.permissions();
	}
	
	@Override
	public Collection<Action> declaredPermissions() {
		return test.declaredPermissions();
	}
	
	@Override
	public boolean can(Action action) {
		return test.can(action);
	}
	
	@Override
	public boolean is(Role binding) {
		return test.is(binding);
	}
	
	@Override
	public Collection<Role> roles() {
		return test.roles();
	}
	
	@Override
	public boolean is(RoleModel model) {
		return test.is(model);
	}
	
	@Override
	public boolean equals(Object arg0) {
		return test.equals(arg0);
	}
	
	@Override
	public int hashCode() {
		return test.hashCode();
	}
	
}
