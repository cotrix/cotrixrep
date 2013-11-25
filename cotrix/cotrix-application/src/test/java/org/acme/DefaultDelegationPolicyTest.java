package org.acme;

import static org.cotrix.action.Actions.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.application.DelegationPolicy;
import org.cotrix.application.impl.DefaultDelegationPolicy;
import org.cotrix.domain.User;
import org.junit.Test;

public class DefaultDelegationPolicyTest {

	static Action doit = action("doit");
	
	//we're testing precisely this impl
	DelegationPolicy policy = new DefaultDelegationPolicy();
	
	@Test(expected=IllegalAccessError.class)
	public void delegatesOnlyOwnPermissions() {
	
		User joe = user().name("joe").fullName("joe").build();
		User bill = user().name("bill").fullName("bill").build();

		policy.validateDelegation(joe, bill, doit);

	}
	
	
	
	@Test(expected=IllegalAccessError.class)
	public void doesNotDelegateTemplatesForNonRootUsers() {
		
		User joe = user().name("joe").fullName("joe").can(doit).build();
		User bill = user().name("bill").fullName("bill").build();
		
		policy.validateDelegation(joe, bill, doit);
	}
	
	
	@Test
	public void delegatesTemplatesForRootUsers() {
		
		User joe = user().name("joe").fullName("joe").isRoot().can(doit).build();
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		policy.validateDelegation(joe, bill, doit);

	}
		
}
