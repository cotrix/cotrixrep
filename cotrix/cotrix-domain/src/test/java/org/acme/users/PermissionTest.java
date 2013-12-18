package org.acme.users;

import static org.junit.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.domain.dsl.grammar.UserGrammar;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class PermissionTest {

	Action doit = action("doit");
	Action dothat = action("dothat");
	
	@Test
	public void permissionTemplate() {
		
		User bill = bill().can(doit).build();
		
		assertEqualSets(bill.permissions(),doit);
		
		assertTrue(bill.can(doit));
		
		assertTrue(bill.can(doit.on("1")));
		
		assertFalse(bill.can(dothat.on("1")));
	}
	
	@Test
	public void permissionsAreNotAddedTwice() {
		
		User bill = bill().can(doit,doit).build();
		
		assertEqualSets(bill.permissions(),doit);
	}
	
	@Test
	public void moreSpecificPermissionsAreRetained() {
		
		//when we add or merge permissions we remove only duplicates, not redundant specifications 
		
		User bill = bill().can(doit.on("1"),doit,doit.on("1")).build();
		
		assertEqualSets(bill.permissions(),doit,doit.on("1"));
	}
	
	//helpers
	
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").email("bill@me.com").fullName("bill");
	}
}
