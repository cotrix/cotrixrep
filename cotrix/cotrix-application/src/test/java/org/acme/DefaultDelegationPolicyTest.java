package org.acme;

import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.application.DelegationPolicy;
import org.cotrix.application.impl.delegation.DefaultDelegationPolicy;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class DefaultDelegationPolicyTest {

	static Action doit = action("doit");
	
	//we're testing precisely this impl
	DelegationPolicy policy = new DefaultDelegationPolicy();
	
	@Test(expected=IllegalAccessError.class)
	public void delegatesOnlyOwnPermissions() {
	
		User joe = user().name("joe").email("joe@me.com").build();
		User bill = user().name("bill").email("bil@me.coml").build();

		policy.validateDelegation(joe, bill, doit);

	}
	
	
	
	@Test(expected=IllegalAccessError.class)
	public void doesNotDelegateTemplatesForNonRootUsers() {
		
		User joe = user().name("joe").email("joe@me.com").can(doit).build();
		User bill = user().name("bill").email("bill@me.com").build();
		
		policy.validateDelegation(joe, bill, doit);
	}
	
	@Test
	public void delegatesTemplatesForRootUsers() {
		
		User joe = user().name("joe").email("joe@me.com").isRoot().can(doit).build();
		User bill = user().name("bill").email("bill@me.com").build();
		
		policy.validateDelegation(joe, bill, doit);

	}
	
	@Test
	public void delegatesRolesWithTemplatesOfDifferentTypesForNonRootUsers() {
		
		Action doGeneric = action(application,"generic");
		Role something = user().name("r1").noMail().can(doGeneric).buildAsRoleFor(application);
		
		Action doSpecific = action(codelists,"specific");
		Role somethingElse = user().name("r2").noMail().is(something).can(doSpecific).buildAsRoleFor(codelists);
		
		User joe = user().name("joe").email("joe@me.com").is(somethingElse.on("1")).build();
		User bill = user().name("bill").email("bill@me.com").build();
		
		policy.validateDelegation(joe, bill, somethingElse.on("1"));
		
	}

}
