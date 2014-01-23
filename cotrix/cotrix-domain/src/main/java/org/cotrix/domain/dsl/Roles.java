package org.cotrix.domain.dsl;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.action.ResourceType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.action.ResourceType;
import org.cotrix.action.UserAction;
import org.cotrix.domain.dsl.builder.UserBuilder;
import org.cotrix.domain.dsl.grammar.UserGrammar.ThirdClause;
import org.cotrix.domain.memory.UserMS;
import org.cotrix.domain.user.Role;

public class Roles {

	//constructs a role on top of user-oriented DSL.
	//notes must have a constant identifier (here the very role name) because if they were generated they would be JVM-specific
	//and equality comparisons against the constants below would fail after a restart.
	public static ThirdClause role(String name) {
		return new UserBuilder(new UserMS(name)).name(name).noMail();
	}
	
	// the role of generic users, typically right after sign up: can view and logout
	public static Role USER = role("user").fullName("User Role").can(VIEW, ACCESS_ADMIN_AREA).buildAsRoleFor(application);

	public static Role MANAGER = role("manager").fullName("Manager Role").is(USER)
			.can(IMPORT, MainAction.PUBLISH).buildAsRoleFor(application);
	
	// a user that can edit given codelists.
	public static Role EDITOR = role("editor").fullName("Editor Role").is(USER).can(EDIT).buildAsRoleFor(codelists);

	// a user that can review codelists.
	public static Role REVIEWER = role("reviewer").fullName("Reviewer Role").is(USER,EDITOR).can(LOCK,UNLOCK).buildAsRoleFor(codelists);

	// a user that can review codelists.
	public static Role PUBLISHER = role("publisher").fullName("Publisher Role").is(USER).can(CodelistAction.PUBLISH, MainAction.PUBLISH).buildAsRoleFor(codelists);

	// a user that has all the permissions and roles on given codelists.
	public static Role OWNER = role("owner").fullName("Owner Role").is(USER, EDITOR, REVIEWER,PUBLISHER).can(CodelistAction.values()).buildAsRoleFor(codelists);

	
	
	
	// a user that has all the permissions and roles
	public static Role ROOT = role("root").fullName("Root Role").can(MainAction.values())
			.can(CodelistAction.values()).can(UserAction.values()).is(USER, MANAGER,OWNER).buildAsRoleFor(application);
	
	public static Collection<Role> getBy(ResourceType ... types) {
		List<Role> roles = new ArrayList<Role>();
		for (Role role:predefinedRoles) {
			for (ResourceType type:types) {
				if (role.type() == type) {
					roles.add(role);
					break;
				}
			}
		}
		return roles;
	}
	
	public static Collection<Role> predefinedRoles = Arrays.asList(ROOT,USER,MANAGER,OWNER,REVIEWER,EDITOR,PUBLISHER);

}
