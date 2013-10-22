package org.cotrix.user.dsl;

import static org.cotrix.common.Utils.*;

import org.cotrix.action.Action;
import org.cotrix.user.User;
import org.cotrix.user.po.UserPO;

public class UserBuilder {

	private final UserPO po;
	
	public UserBuilder(String id) {
		po = new UserPO(id);
		
		//use as default for full name too
		fullName(id);
	}
	
	public UserBuilder can(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			po.permissions().add(action);
		return this;
	}
	
	public UserBuilder fullName(String name) {
		valid("user name", name);
		po.setName(name);
		return this;
	}
	
	public User build() {
		return new User.Private(po);
	}
}
