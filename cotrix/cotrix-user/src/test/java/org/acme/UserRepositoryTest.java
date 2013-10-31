package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.user.dsl.Users.*;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.common.cdi.Current;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class UserRepositoryTest {

	static User joe = user().name("joe").fullName("Joe The Plumber").can(EDIT.on("1")).build();
	
	@Inject
	UserRepository repository;
	
	
	@PostConstruct
	public void before() {
		
		repository.add(joe);
	}
	
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
	public void updateWithIllegalAction() {
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		User changeset = user(bill.id()).can(LOCK.on("1")).build();
		
		repository.update(changeset);
	}
	
	@Test
	public void updateWithLegalAction() {
		
		User bill = user().name("bill").fullName("Bill the Baker").build();
		
		repository.add(bill);
		
		User changeset = user(bill.id()).fullName("Bill the BrickLayer").can(EDIT.on("1")).build();
		
		repository.update(changeset);
		
		User retrieved = repository.lookup(bill.id());

		System.out.println(retrieved);

		assertEquals("Bill the BrickLayer", retrieved.fullName());
		assertTrue(retrieved.permissions().contains(EDIT.on("1")));
		
	}
	
	@Produces @Current
	static User current() {
		return joe;
	}
}
