package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.user.PredefinedUsers.*;
import static org.cotrix.user.dsl.Users.*;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.common.cdi.Current;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.cotrix.user.utils.CurrentUser;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class UserRepositoryTest {

	static CurrentUser currentUser = new CurrentUser();
	
	@Inject
	UserRepository repository;
	
	@Test
	public void addRetrieveAndDeleteUser() {
		
		User bill = user().name("bill").fullName("Bill the Baker").can(IMPORT).build();
		
		repository.add(bill);
		
		User retrieved = repository.lookup(bill.id());
		
		assertEquals(bill,retrieved);
		
		repository.remove(bill.id());
		
		retrieved = repository.lookup(bill.id());
			
		assertNull(retrieved);
	}
	
	@Test(expected=IllegalAccessError.class)
	public void addIllegalAction() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(EDIT.on("1")).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		User changeset = user(bill.id()).can(LOCK.on("1")).build();
		
		repository.update(changeset);
	}
	
	@Test(expected=IllegalAccessError.class)
	public void addTemplate() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(IMPORT).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		User changeset = user(bill.id()).can(IMPORT).build();
		
		repository.update(changeset);
	}
	
	
	@Test
	public void addTemplateAsRoot() {
		
		currentUser.set(cotrix);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		User changeset = user(bill.id()).can(IMPORT).build();
		
		repository.update(changeset);
	}
	
	@Test
	public void addAction() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(EDIT.on("1")).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		User changeset = user(bill).fullName("Bill the BrickLayer").can(EDIT.on("1")).build();
		
		repository.update(changeset);
		
		User retrieved = repository.lookup(bill.id());

		assertEquals("Bill the BrickLayer", retrieved.fullName());
		assertTrue(retrieved.permissions().contains(EDIT.on("1")));
		
	}
	
	@Test
	public void removeAction() {
		
		User joe = user().name("joe").fullName("Joe The Plumber").can(EDIT.on("1")).build();
		
		currentUser.set(joe);
		
		User bill = user().name("bill").fullName("Bill the Baker").can(EDIT.on("1")).build();
		
		repository.add(bill);
		
		User changeset = user(bill).cannot(EDIT.on("1")).build();
		
		repository.update(changeset);
		
		User retrieved = repository.lookup(bill.id());

		assertTrue(retrieved.permissions().isEmpty());
	
	}
	
	@Produces @Current
	static User current() {
		return currentUser;
	}
}
