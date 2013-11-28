package org.cotrix.domain.dsl;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.action.ResourceType;
import org.cotrix.action.UserAction;
import org.cotrix.domain.user.Role;

public class Roles {

	// the role of generic users, typically right after sign up: can view and logout
	public static Role USER = user().name("user").fullName("User Role").can(VIEW, LOGOUT).buildAsRoleFor(application);

	public static Role MANAGER = user().name("manager").fullName("Manager Role").is(USER)
			.can(IMPORT, MainAction.PUBLISH).buildAsRoleFor(application);
	
	// a user that can edit given codelists.
	public static Role EDITOR = user().name("editor").fullName("Editor Role").is(USER).can(EDIT).buildAsRoleFor(codelists);

	// a user that can review codelists.
	public static Role REVIEWER = user().name("reviewer").fullName("Reviewer Role").is(USER,EDITOR).can(LOCK,UNLOCK).buildAsRoleFor(codelists);

	// a user that can review codelists.
	public static Role PUBLISHER = user().name("publisher").fullName("Publisher Role").is(USER).can(CodelistAction.PUBLISH).buildAsRoleFor(codelists);

	// a user that has all the permissions and roles on given codelists.
	public static Role OWNER = user().name("owner").fullName("Owner Role").is(USER, EDITOR, REVIEWER,PUBLISHER).can(CodelistAction.values()).buildAsRoleFor(codelists);

	
	
	
	// a user that has all the permissions and roles
	public static Role ROOT = user().name("root").fullName("Root Role").can(MainAction.values())
			.can(CodelistAction.values()).can(UserAction.values()).is(USER, MANAGER,OWNER).buildAsRoleFor(application);
	
	public static Collection<Role> getBy(ResourceType type) {
		List<Role> roles = new ArrayList<Role>();
		for (Role role:predefinedRoles) {
			if (role.type() == type) roles.add(role);
		}
		return roles;
	}
	
	public static Collection<Role> predefinedRoles = Arrays.asList(ROOT,USER,MANAGER,OWNER,REVIEWER,EDITOR,PUBLISHER);

}