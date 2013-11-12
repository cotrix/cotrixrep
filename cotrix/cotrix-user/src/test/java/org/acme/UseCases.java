package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.user.Users.*;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.common.Utils;
import org.cotrix.common.cdi.Current;
import org.cotrix.repository.utils.UuidGenerator;
import org.cotrix.user.PermissionDelegationService;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.cotrix.user.impl.DefaultDelegationPolicy;
import org.cotrix.user.impl.DefaultDelegationService;
import org.cotrix.user.impl.MUserRepository;
import org.cotrix.user.utils.CurrentUser;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(CdiRunner.class)
@AdditionalClasses({DefaultDelegationService.class, DefaultDelegationPolicy.class, MUserRepository.class, UuidGenerator.class})
public class UseCases {

	private static Logger log = LoggerFactory.getLogger("test");
	
	static CurrentUser current = new CurrentUser();
	
	@Inject
	PermissionDelegationService service;

	//we get a new, clean repository for each test
	//the same repo is injected in service
	@Inject @Dependent
	UserRepository repository;
	
	UserRepository tempUsers = new MUserRepository();
	
	@Test
	public void createUser() {
		
		User root = user().name("root").fullName("root").isRoot().build();
				
		log.info("bill signs in and confirms its requests...");

		User bill = user().name("bill").fullName("bill").is(USER).build();

		log.info("bill is added to some temp storage of sign in requests...");
		
		tempUsers.add(bill);
		
		log.info("root logs in...");
		
		current.set(root);
		
		log.info("root sees pending user request...");
		
		User billRetrieved = tempUsers.lookup(bill.id());
		
		User billConfirmed = Utils.reveal(billRetrieved,User.Private.class).copy();
		
		log.info("root approves bill...");
		
		assertTrue(root.can(CREATEUSER));
		
		repository.add(billConfirmed);
		
		log.info("bill is added to repository...");
		
		
	}
	
//	@Produces @ProducesAlternative @ApplicationScoped 
//	public UserRepository repo() {
//		return new MUserRepository();
//	}
	
//	@Before
//	public void setup() {
//		
//		//we're testing API support for use cases, not impls: chose convenient ones
//		repository = new MUserRepository();
//		DelegationPolicy policy = new DefaultDelegationPolicy();
//		service = new DefaultDelegationService(current,repository,policy);
//		
//	}
//	
//	@Test
//	public void importUseCase() {
//		
//		User root = user().name("guy").fullName("guy").is(ROOT).buildAsModel();
//
//		current.set(root);
//		
//		User joe = user().name("joe").fullName("joe").build();
//		User bill = user().name("bill").fullName("bill").build();
//		
//		repository.add(joe);
//		repository.add(bill);
//		
//		//root delegates IMPORT to joe
//		service.delegate(MANAGER).to(joe);
//		
//		current.set(joe);
//		
//		
//		assertTrue(joe.is(MANAGER));
//
//		//joe imports codelist "1" and acquires EDIT on it;
//		User changeset = user(joe).is(EDITOR.on("1")).build();
//
//		repository.update(changeset);
//		
//		//joe delegates EDIT on "1" to bill
//		service.delegate(EDITOR.on("1")).to(bill);
//		
//		assertTrue(joe.is(EDITOR.on("1")));
//	}
	
	
	@Produces @Current
	public static User current() {
		return current;
	}
	
}
