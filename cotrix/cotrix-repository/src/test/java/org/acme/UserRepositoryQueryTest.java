package org.acme;

import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.UserQueries.*;

import javax.inject.Inject;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.dsl.grammar.UserGrammar;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class UserRepositoryQueryTest extends ApplicationTest {

	@Inject
	UserRepository repository;
	
	@Test
	public void getAllUsers() {
		
		User bill = aUser("bill").build();
		User joe = aUser("joe").build();
		User zoe = aUser("zoe").build();
		
		repository.add(bill);
		repository.add(joe);
		repository.add(zoe);
		
		Iterable<User> users = repository.get(allUsers());
		
		assertEqualUnordered(collect(users),bill,joe,zoe);
	}
	
	@Test
	public void getRoots() {
		
		User bill = aUser("bill").isRoot().build();
		User joe = aUser("joe").build();
		
		repository.add(bill);
		repository.add(joe);
		
		Iterable<User> users = repository.get(usersWith(Roles.ROOT));
		
		assertEqualUnordered(collect(users),bill);
	}
	
	
	@Test
	public void getUsersByRole() {
		
		Role role = aRole().buildAsRoleFor(codelists);
		
		User bill = aUser("bill").is(role.on("1")).build();
		User joe = aUser("joe").is(role.on("2")).build();
		User zoe = aUser("zoe").is(role).build();
		 
		repository.add(bill);
		repository.add(joe);
		repository.add(zoe);
		
		
		Iterable<User> users = repository.get(usersWith(role.on("1")));
		
		assertEqualUnordered(collect(users),bill,zoe);
		
		users = repository.get(usersWith(role));
		
		assertEqualUnordered(collect(users),zoe);
		
		users = repository.get(teamFor("1"));
		
		assertEqualUnordered(collect(users),bill);
		
	}
	
	
	@Test
	public void sortUsers() {
		
		User bill = aUser("bill").build();
		User joe = aUser("joe").build();
		User zoe = aUser("zoe").build();
	
		repository.add(joe);
		repository.add(zoe);
		repository.add(bill);
		
		
		Iterable<User> users = repository.get(allUsers().sort(byName()));
		
		assertEqualOrdered(collect(users),bill,joe,zoe);
		
	}
	
	
	
	//helper
	
	private UserGrammar.ThirdClause aUser(String name) {
		
		return user().name(name).noMail().fullName(name);
	}
	
	private UserGrammar.ThirdClause aRole() {
		
		return user().name("role").noMail().fullName("role");
	}
}
