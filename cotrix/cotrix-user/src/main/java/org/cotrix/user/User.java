package org.cotrix.user;

import java.util.Collection;

import org.cotrix.action.Action;

public interface User {

	String id();
	
	String name();
	
	Collection<Action> permissions();
}
