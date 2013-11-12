package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.user.Users.*;

import org.cotrix.user.DelegationPolicy;
import org.cotrix.user.PermissionDelegationService;
import org.cotrix.user.User;
import org.cotrix.user.UserRepository;
import org.cotrix.user.impl.DefaultDelegationPolicy;
import org.cotrix.user.impl.DefaultDelegationService;
import org.cotrix.user.impl.MUserRepository;
import org.cotrix.user.utils.CurrentUser;
import org.junit.Before;
import org.junit.Test;

public class UseCases {

	static CurrentUser current = new CurrentUser();
	
	PermissionDelegationService service;
	
	UserRepository repository;
	
	@Before
	public void setup() {
		
		//we're testing API support for use cases, not impls: chose convenient ones
		repository = new MUserRepository();
		DelegationPolicy policy = new DefaultDelegationPolicy();
		service = new DefaultDelegationService(current,repository,policy);
		
	}
	
	@Test
	public void importUseCase() {
		
		User root = user().name("guy").fullName("guy").is(ROOT).buildAsModel();

		current.set(root);
		
		User joe = user().name("joe").fullName("joe").build();
		User bill = user().name("bill").fullName("bill").build();
		
		repository.add(joe);
		repository.add(bill);
		
		//root delegates IMPORT to joe
		service.delegate(MANAGER).to(joe);
		
		current.set(joe);
		
		
		assertTrue(joe.is(MANAGER));

		//joe imports codelist "1" and acquires EDIT on it;
		User changeset = user(joe).is(EDITOR.on("1")).build();

		repository.update(changeset);
		
		//joe delegates EDIT on "1" to bill
		service.delegate(EDITOR.on("1")).to(bill);
		
		assertTrue(joe.is(EDITOR.on("1")));
	}
}
