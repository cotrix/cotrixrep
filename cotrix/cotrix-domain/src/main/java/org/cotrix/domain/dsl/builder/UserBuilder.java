package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.domain.dsl.grammar.UserGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserChangeClause;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserNewClause;
import org.cotrix.domain.po.UserPO;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.user.DefaultRole;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;

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
	
	@Override
	public ThirdClause isRoot() {
		return is(ROOT);
	}
	
	public UserBuilder is(Role ... roles) {
		valid("roles",roles);
		for (Role role : roles)
			po.add(role);
		return this;
	}
	
	@Override
	public UserBuilder is(Collection<Role> roles) {
		return is(roles.toArray(new DefaultRole[0]));
	}
	
	@Override
	public UserChangeClause isNot(Role ... roles) {
		valid("roles",roles);
		for (Role role : roles)
			po.remove(role);
		return this;
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
		return new User.Private(po);
	}
	
	public Role buildAsRoleFor(ResourceType type) {
		return new DefaultRole(new User.Private(po),type);
	}
}
