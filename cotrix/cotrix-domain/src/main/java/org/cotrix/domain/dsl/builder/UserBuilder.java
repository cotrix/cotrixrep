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
import org.cotrix.domain.memory.UserMS;
import org.cotrix.domain.trait.Status;
import org.cotrix.domain.user.DefaultRole;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.domain.utils.Constants;

public class UserBuilder implements UserNewClause, UserChangeClause {

	private final UserMS state;
	
	public UserBuilder(String id) {
		
		valid("identifier",id);
		
		state = new UserMS(id);
		state.status(Status.MODIFIED);
	}
	
	public UserBuilder() {
		state = new UserMS(null);
	}
	
	@Override
	public User delete() {
		state.status(DELETED);
		return build();
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
		return email(Constants.NO_MAIL);
	}
	
	public UserBuilder can(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			state.permissions().add(action);
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
	public UserChangeClause isNot(Role ... roles) {
		valid("roles",roles);
		for (Role role : roles)
			state.remove(role);
		return this;
	}
	
	
	@Override
	public UserBuilder can(Collection<Action> actions) {
		
		return can(actions.toArray(new Action[0]));
	}
	
	public UserBuilder cannot(Action ... actions) {
		valid("actions",actions);
		for (Action action : actions)
			state.permissions().remove(action);
		return this;
	}
	
	public UserBuilder fullName(String name) {
		valid("user's full name", name);
		state.fullName(name);
		return this;
	}
	
	public User build() {
		return new User.Private(state);
	}
	
	public Role buildAsRoleFor(ResourceType type) {
		return new DefaultRole(new User.Private(state),type);
	}
}
