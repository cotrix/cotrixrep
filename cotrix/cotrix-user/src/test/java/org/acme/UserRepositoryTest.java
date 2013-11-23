package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.user.Users.*;
import static org.cotrix.user.queries.UserQueries.*;

import org.cotrix.action.Action;
import org.cotrix.user.Role;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.cotrix.user.dsl.UserGrammar;
import org.cotrix.user.memory.MUserRepository;
import org.junit.Before;
import org.junit.Test;

public class UserRepositoryTest {

	UserRepository repository;
	
	@Before
	public void setup() {
	
		//we get a fresh copy to isolate tests
		repository = new MUserRepository();
	}
	
	@Test
	public void addAndRetrieveUser() {
	
		User bill = bill();
		
		repository.add(bill);
		
		User retrieved = repository.lookup(bill.id());
		
		assertEquals(bill,retrieved);
	
	}
	
	
	@Test
	public void deleteUser() {
		
		User bill = bill();
		
		repository.add(bill);
		
		repository.remove(bill.id());
			
		assertNull(repository.lookup(bill.id()));
	}
	
	@Test
	public void updateUser() {
		
		Action doit = action("doit");
		Role role = user().name("role").fullName("role").buildAsRoleFor(application);
		
		User bill = bill();
		
		repository.add(bill);
		
		User changeset = user(bill).can(doit).is(role).build();
		
		update(bill,changeset);
		
		User billAsRetrieved = repository.lookup(bill.id());
		
		assertTrue(billAsRetrieved.can(doit));
		assertTrue(billAsRetrieved.is(role));
	}
	
	@Test
	public void getAllUsers() {
		
		User bill = bill();
		
		repository.add(bill);
		
		Iterable<User> users = repository.queryFor(allUsers());
		
		assertEquals(bill,users.iterator().next());
	}
	
	@Test
	public void getUsersByRole() {
		
		Role role = aRole().buildAsRoleFor(codelists);
		User bill = aUser("bill").is(role.on("1")).build();
		User joe = aUser("joe").is(role.on("2")).build();
		
		repository.add(bill);
		repository.add(joe);
		
		
		Iterable<User> users = repository.queryFor(allUsers().with(roleOn("1",codelists)));
		
		assertEqualSets(gather(users),bill);
		
	}
	
	
	
	//helper
	
	private User bill() {
		
		return aUser("bill").build();
	}
	
	private UserGrammar.ThirdClause aUser(String name) {
		
		return user().name(name).fullName(name);
	}
	
	private UserGrammar.ThirdClause aRole() {
		
		return user().name("role").fullName("role");
	}
	
	
	private void update(User bill, User changeset) {
		
		reveal(bill,User.Private.class).update(reveal(changeset,User.Private.class));
	}
}
