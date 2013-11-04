package org.cotrix.user.dsl;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.domain.trait.Status;
import org.cotrix.user.Role;
import org.cotrix.user.RoleModel;
import org.cotrix.user.User;
import org.cotrix.user.dsl.UserGrammar.UserChangeClause;
import org.cotrix.user.dsl.UserGrammar.UserNewClause;
import org.cotrix.user.po.UserPO;

public class UserBuilder implements UserNewClause, UserChangeClause {

	private final UserPO po;
	
	public UserBuilder(String id) {
		
		valid("identifier",id);
		
		po = new UserPO(id);
		po.setChange(Status.MODIFIED);
	}
	
	public UserBuilder() {
		po = new UserPO(null);
	}
	
	@Override
	public User delete() {
		po.setChange(DELETED);
		return build();
	}
	
	public UserBuilder name(String name) {
		valid("user name",name);
		po.setName(name);
		return this;
	}
	
	public UserBuilder can(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			po.permissions().add(action);
		return this;
	}
	
	public UserBuilder is(RoleModel ... models) {
		valid("roles",models);
		for (RoleModel model : models)
			is(new Role(model));
		return this;
	}
	
	public UserBuilder is(Role ... roles) {
		valid("roles",roles);
		for (Role role : roles)
			po.add(role);
		return this;
	}
	
	@Override
	public UserBuilder is(Collection<Role> roles) {
		return is(roles.toArray(new Role[0]));
	}
	
	@Override
	public UserBuilder can(Collection<Action> actions) {
		
		return can(actions.toArray(new Action[0]));
	}
	
	public UserBuilder cannot(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			po.permissions().remove(action);
		return this;
	}
	
	public UserBuilder fullName(String name) {
		valid("user's full name", name);
		po.setFullName(name);
		return this;
	}
	
	public User build() {
		return buildAsModel();
	}
	
	public RoleModel buildAsModel() {
		return new User.Private(po);
	}
}
