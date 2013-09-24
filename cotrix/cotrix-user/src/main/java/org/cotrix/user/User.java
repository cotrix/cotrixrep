package org.cotrix.user;

import java.util.List;

import org.cotrix.action.Action;

public interface User {

	String id();
	
	String name();
	
	List<Action> permissions();
}
