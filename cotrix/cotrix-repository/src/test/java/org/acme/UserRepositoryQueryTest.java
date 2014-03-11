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
	public void sortUsersByName() {
		
		User bill = aUser("bill").build();
		User joe = aUser("joe").build();
		User zoe = aUser("zoe").build();
	
		repository.add(joe);
		repository.add(zoe);
		repository.add(bill);
		
		
		Iterable<User> users = repository.get(allUsers().sort(byName()));
		
		assertEqualOrdered(collect(users),bill,joe,zoe);
		
	}
	
	@Test
	public void sortUsersByFullName() {
		
		User y = user().name("a").fullName("y").noMail().build();
		User z = user().name("b").fullName("z").noMail().build();
		User x = user().name("c").fullName("x").noMail().build();
	
		repository.add(y);
		repository.add(z);
		repository.add(x);
		
		
		Iterable<User> users = repository.get(allUsers().sort(byFullName()));
		
		assertEqualOrdered(collect(users),x,y,z);
		
	}
	
	
	
	//helper
	
	private UserGrammar.ThirdClause aUser(String name) {
		
		return user().name(name).fullName(name).noMail();
	}
	
	private UserGrammar.ThirdClause aRole() {
		
		return user().name("role").fullName("role").noMail();
	}
}
