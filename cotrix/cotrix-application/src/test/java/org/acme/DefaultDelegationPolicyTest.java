package org.acme;

import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.application.DelegationPolicy;
import org.cotrix.application.impl.delegation.DefaultDelegationPolicy;
import org.cotrix.domain.dsl.grammar.UserGrammar.ThirdClause;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class DefaultDelegationPolicyTest {

	static Action doit = action("doit");
	
	//we're testing precisely this impl
	DelegationPolicy policy = new DefaultDelegationPolicy();
	
	@Test(expected=IllegalAccessError.class)
	public void delegatesOnlyOwnPermissions() {
	
		User joe = user().name("joe").fullName("joe").email("joe@me.com").build();
		User bill = user().name("bill").fullName("bill").email("bil@me.coml").build();

		policy.validateDelegation(joe, bill, doit);

	}
	
	
	
	@Test(expected=IllegalAccessError.class)
	public void doesNotDelegateTemplatesForNonRootUsers() {
		
		User joe = someuser("joe").can(doit).build();
		User bill = someuser("bill").build();
		
		policy.validateDelegation(joe, bill, doit);
	}
	
	@Test
	public void delegatesTemplatesForRootUsers() {
		
		User joe = someuser("joe").isRoot().can(doit).build();
		User bill = someuser("bill").build();
		
		policy.validateDelegation(joe, bill, doit);

	}
	
	@Test
	public void delegatesRolesWithTemplatesOfDifferentTypesForNonRootUsers() {
		
		Action doGeneric = action(application,"generic");
		Role something = someuser("r1").can(doGeneric).buildAsRoleFor(application);
		
		Action doSpecific = action(codelists,"specific");
		Role somethingElse = someuser("r2").is(something).can(doSpecific).buildAsRoleFor(codelists);
		
		User joe = someuser("joe").is(somethingElse.on("1")).build();
		User bill = someuser("bill").build();
		
		policy.validateDelegation(joe, bill, somethingElse.on("1"));
		
	}

	ThirdClause someuser(String n) {
		return user().name(n).fullName(n).noMail();
	}
}
