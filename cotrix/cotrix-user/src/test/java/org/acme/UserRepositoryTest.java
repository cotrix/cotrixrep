package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.user.dsl.Users.*;

import javax.inject.Inject;

import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class UserRepositoryTest {

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
	
}
