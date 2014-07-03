package org.cotrix.test;

import static org.cotrix.test.TestConstants.*;

import java.util.Collection;

import javax.inject.Singleton;

import org.cotrix.action.Action;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;

//it's an average joe but can wrap anyone else during testing (hot user swapping)
@Singleton
public class TestUser implements User {

	private User test = joe;
	
	public void set(User test) {
		this.test=test;
	}
	
	public User get() {
		return test;
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
	
	@Override
	public String email() {
		return test.email();
	}
	
	@Override
	public boolean isRoot() {
		return test.isRoot();
	}

	public Collection<Action> permissions() {
		return test.permissions();
	}
	
	@Override
	public Collection<Action> directPermissions() {
		return test.directPermissions();
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
	public boolean isDirectly(Role role) {
		return test.isDirectly(role);
	}
	
	@Override
	public Collection<Role> directRoles() {
		return test.directRoles();
	}
	
	@Override
	public Collection<Role> roles() {
		return test.roles();
	}
	
	@Override
	public FingerPrint fingerprint() {
		return test.fingerprint();
	}
	
	@Override
	public boolean equals(Object arg0) {
		return test.equals(arg0);
	}
	
	@Override
	public int hashCode() {
		return test.hashCode();
	}
	
	@Override
	public String toString() {
		return test.toString();
	}
	
}
