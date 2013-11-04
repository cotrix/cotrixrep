package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.user.Users.*;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.user.PermissionDelegationService;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.cotrix.user.utils.CurrentUser;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class PermissionDelegationTest {

	static CurrentUser currentUser = new CurrentUser();
	
	@Inject
	UserRepository repository;
	
	@Inject
	PermissionDelegationService service;
	
	
	
	@Test(expected=IllegalAccessError.class)
	public void delegateIllegalPermission() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(EDIT.on("1")).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		service.delegate(LOCK.on("1")).to(bill);
	}
	
	@Test(expected=IllegalAccessError.class)
	public void delegateTemplate() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(IMPORT).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		service.delegate(IMPORT).to(bill);
	}
	
	
	@Test
	public void delegateTemplateAsRoot() {
		
		currentUser.set(cotrix);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		service.delegate(IMPORT).to(bill);
		
		User retrieved = repository.lookup(bill.id());

		assertTrue(retrieved.can(IMPORT));
	}
	
	@Test
	public void delegatePermission() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(EDIT.on("1")).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		service.delegate(EDIT.on("1")).to(bill);
		
		User retrieved = repository.lookup(bill.id());

		assertTrue(retrieved.can(EDIT.on("1")));
		
	}
	
	@Test
	public void revokePermission() {
		
		Action action = EDIT.on("1");
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(action).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").can(action).build();
		
		repository.add(bill);
		
		service.revoke(action).from(bill);
		
		User retrieved = repository.lookup(bill.id());

		assertFalse(retrieved.can(action));
	
	}
	
	@Test (expected=IllegalStateException.class)
	public void revokeNotExistentPermission() {
		
		Action action = EDIT.on("1");
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(action).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		service.revoke(action).from(bill);
			
	}
	
	@Test
	public void delegationOnImportUseCase() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").build();
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(joe);
		repository.add(bill);
		
		currentUser.set(cotrix);
		
		//cotrix delegates IMPORT to joe
		service.delegate(IMPORT).to(joe);
		
		
		
		currentUser.set(joe);
		
		//joe imports codelist "1" and acquires EDIT on it;
		User changeset = user(joe).is(EDITOR.on("1")).build();
		
		repository.update(changeset);
		
		//joe delegates EDIT on "1" to bill
		service.delegate(EDIT.on("1")).to(bill);
	}
	
	@Produces @Current
	static User current() {
		return currentUser;
	}
}
