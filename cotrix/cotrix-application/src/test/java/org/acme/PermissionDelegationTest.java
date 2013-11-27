package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.cotrix.action.Action;
import org.cotrix.application.DelegationPolicy;
import org.cotrix.application.PermissionDelegationService;
import org.cotrix.application.impl.DefaultDelegationService;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.user.UserRepository;
import org.cotrix.repository.user.impl.DefaultUserRepository;
import org.cotrix.repository.user.impl.MUserRepository;
import org.junit.Before;
import org.junit.Test;

public class PermissionDelegationTest {

	static Action doit = action("doit");
	
	PermissionDelegationService service;
	
	UserRepository repository;
	
	DelegationPolicy policy;
	
	@Before
	public void setup() {
		
		
		//we dont care about repositories and policies here, so setting up defaults
		repository = new DefaultUserRepository(new MUserRepository());
		
		policy = mock(DelegationPolicy.class);
		
		User current = user().name("current").fullName("current").build();
		
		//we would not want to test the impl, but using cdi to use configured alternative has its own issues
		//also, we're not expecting yet multiple implementations for this service 
		service = new DefaultDelegationService(current,repository,policy);
		
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void doesNotDelegateToUnidentifiedUser() {
		
		User bill = user().name("bill").fullName("bill").build();
		
		service.delegate(doit.on("1")).to(bill);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void doesNotDelegateRoleToUnidentifiedUser() {
		
		User bill = user().name("bill").fullName("bill").build();
		
		service.delegate(aRole()).to(bill);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void doesNotRevokeFromUnidentifiedUser() {
		
		User bill = user().name("bill").fullName("bill").build();
		
		service.revoke(doit.on("1")).from(bill);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void doesNotRevokeRoleFromUnidentifiedUser() {
		
		User bill = user().name("bill").fullName("bill").build();
		
		service.revoke(aRole()).from(bill);
	}
	
	
	
	
	
	
	
	@Test
	public void delegatesExistingPermission() {
		
		User bill = billCan(doit);
		
		service.delegate(doit).to(bill);
		
		assertEquals(1,bill.permissions().size());
			
	}
	
	@Test
	public void delegatesExistingRole() {
		
		Role role = aRole();
		
		User bill = billIs(role);
		
		service.delegate(role).to(bill);
		
		assertEquals(1,bill.roles().size());
			
	}
	
	@Test(expected=IllegalStateException.class)
	public void doesNotRevokeNotExistentPermission() {
		
		User bill = user().name("bill").fullName("bill").build();
		
		repository.add(bill);
		
		service.revoke(doit).from(bill);
			
	}
	

	
	
	
	
	@Test
	public void consultsPolicyForDelegation() {
		
		User bill = bill();
		
		service.delegate(doit).to(bill);
		
		verify(policy).validateDelegation(any(User.class),any(User.class),(Action[]) anyVararg());
		
	}
	
	
	@Test
	public void consultsPolicyForRevocation() {
		
		User bill = billCan(doit);
		
		service.revoke(doit).from(bill);
		
		verify(policy).validateRevocation(any(User.class),any(User.class),(Action[]) anyVararg());
	}
	
	
	
	
	
	
	@Test
	public void persistsDelegation() {
		
		User bill = bill();
		
		service.delegate(doit).to(bill);
		
		User billAsRetrieved = repository.lookup(bill.id());
		
		assertTrue(billAsRetrieved.can(doit));
		
	}
	
	@Test
	public void persistsRoleDelegation() {
		
		Role role = aRole();
		
		User bill = bill();
		
		service.delegate(role).to(bill);
		
		User billAsRetrieved = repository.lookup(bill.id());
		
		assertTrue(billAsRetrieved.is(role));
		
	}
	
	@Test
	public void persistsRevocation() {
		
		User bill = user().name("bill").fullName("bill").can(doit).build();
		
		repository.add(bill);
		
		service.revoke(doit).from(bill);
		
		User billAsRetrieved = repository.lookup(bill.id());
		
		assertFalse(billAsRetrieved.can(doit));
		
	}
	
	@Test
	public void persistsRoleRevocation() {
		
		Role role = aRole();
		
		User bill = billIs(role);
		
		service.revoke(role).from(bill);
		
		User billAsRetrieved = repository.lookup(bill.id());
		
		assertFalse(billAsRetrieved.is(role));
		
	}
	
	
	//helper
	private User bill() {
		
		User bill = user().name("bill").fullName("bill").build();
		
		repository.add(bill);
		
		return bill;
	}
	
	private User billCan(Action ...actions) {
		
		User bill = user().name("bill").fullName("bill").can(actions).build();
		
		repository.add(bill);
		
		return bill;
	}
	
	private User billIs(Role ...roles) {
		
		User bill = user().name("bill").fullName("bill").is(roles).build();
		
		repository.add(bill);
		
		return bill;
	}
	
	private Role aRole() {
		return user().name("role").fullName("role").buildAsRoleFor(application);
	}
	

	
	
}
