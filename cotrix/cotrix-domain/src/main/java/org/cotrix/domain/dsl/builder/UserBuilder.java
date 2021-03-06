package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Roles.*;

import java.util.Collection;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.domain.dsl.grammar.UserGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserChangeClause;
import org.cotrix.domain.dsl.grammar.UserGrammar.UserNewClause;
import org.cotrix.domain.memory.MUser;
import org.cotrix.domain.user.DefaultRole;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.domain.utils.DomainConstants;

public class UserBuilder implements UserNewClause, UserChangeClause {

	private final MUser state;
	
	public UserBuilder(MUser state) {
		this.state = state;
	}
	
	public UserBuilder name(String name) {
		valid("user name",name);
		state.name(name);
		return this;
	}
	
	@Override
	public ThirdClause email(String email) {
		notNull("email",email);
		state.email(email);
		return this;
	}
	
	@Override
	public ThirdClause noMail() {
		return email(DomainConstants.NO_MAIL);
	}
	
	public UserBuilder can(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			state.add(action);
		return this;
	}
	
	@Override
	public ThirdClause isRoot() {
		return is(ROOT);
	}
	
	public UserBuilder is(Role ... roles) {
		valid("roles",roles);
		for (Role role : roles)
			state.add(role);
		return this;
	}
	
	@Override
	public UserBuilder is(Collection<Role> roles) {
		return is(roles.toArray(new DefaultRole[0]));
	}
	
	@Override
	public UserChangeClause isNoLonger(Role ... roles) {
		valid("roles",roles);
		for (Role role : roles)
			state.remove(role);
		return this;
	}
	
	@Override
	public UserChangeClause isNoLonger(Collection<Role> roles) {
		return isNoLonger(roles.toArray(new DefaultRole[0]));
	}
	
	
	@Override
	public UserBuilder can(Collection<Action> actions) {
		
		return can(actions.toArray(new Action[0]));
	}
	
	public UserBuilder cannot(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			state.remove(action);
		return this;
	}
	
	public UserBuilder fullName(String name) {
		valid("user's full name", name);
		state.fullName(name);
		return this;
	}
	
	public User build() {
		return state.entity();
	}
	
	public Role buildAsRoleFor(ResourceType type) {
		return new DefaultRole(new User.Private(state),type);
	}
}
