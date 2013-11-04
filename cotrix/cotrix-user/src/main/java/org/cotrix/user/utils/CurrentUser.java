package org.cotrix.user.utils;

import java.util.Collection;

import org.cotrix.action.Action;
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
	public boolean equals(Object arg0) {
		return test.equals(arg0);
	}
	
	@Override
	public int hashCode() {
		return test.hashCode();
	}
	
}
