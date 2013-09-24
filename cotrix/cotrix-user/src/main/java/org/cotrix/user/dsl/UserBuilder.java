package org.cotrix.user.dsl;

import org.cotrix.action.Action;
import org.cotrix.user.User;
import org.cotrix.user.impl.DefaultUser;
import org.cotrix.user.po.UserPO;

public class UserBuilder {

	private final UserPO po;
	
	public UserBuilder(String id, String name) {
		po = new UserPO(id, name);
	}
	
	UserBuilder with(Action ... actions) {
		for (Action action : actions)
			po.permissions().add(action);
		return this;
	}
	
	public User build() {
		return new DefaultUser(po);
	}
}
