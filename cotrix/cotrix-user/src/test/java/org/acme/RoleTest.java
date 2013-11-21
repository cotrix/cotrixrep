package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.user.Users.*;

import org.cotrix.action.Action;
import org.cotrix.user.RoleModel;
import org.cotrix.user.User;
import org.cotrix.user.dsl.UserGrammar;
import org.cotrix.user.impl.DefaultRole;
import org.junit.Test;

public class RoleTest {
	
	Action doit = action("doit");
	Action dothat = action("dothat");

	@Test
	public void assignTemplateRole() {

		RoleModel someone = aRole().buildAsModel();
	
		User bill = bill().is(someone).build();
		
		assertTrue(bill.is(someone));
		
	}
	
	@Test
	public void rolesHaveIdentity() {

		RoleModel someone = aRole().can(doit).buildAsModel();
	
		User bill = bill().can(doit).build();
		
		assertFalse(bill.is(someone));
		
	}
	
	@Test
	public void rolesGivePermissions() {

		RoleModel someone = aRole().can(doit).buildAsModel();
	
		User bill = bill().is(someone).build();
		
		//role permissions propagate
		assertTrue(bill.can(doit));
		
	}
	
	@Test
	public void assignInstanceRoles() {

		RoleModel someone = aRole().can(doit).buildAsModel();
		
		User bill = bill().is(someone.on("1")).build();
		
		assertFalse(bill.can(doit));
		
		assertTrue(bill.can(doit.on("1")));
	}
	
	
	@Test
	public void permissionsCanBeDirectOrIndirect() {

		RoleModel someone = aRole().can(doit).buildAsModel();
	
		User bill = bill().is(someone).can(dothat).build();
		
		System.out.println(bill.directPermissions().getClass());
		
		assertEqualSets(bill.directPermissions(),dothat);
		assertEqualSets(bill.permissions(),doit,dothat);
	}
	
	
	@Test
	public void roleFormHierarchies() {

		RoleModel someone = aRole("r1").can(doit).buildAsModel();
		RoleModel someoneElse = aRole("r2").can(dothat).is(someone).buildAsModel();
	
		User bill = bill().is(someoneElse).build();
		
		assertTrue(bill.is(someone));
		assertTrue(bill.is(someoneElse));
		
		//role permissions propagate
		assertTrue(bill.can(doit));
		assertTrue(bill.can(dothat));
		
	}

	@Test
	public void rolesAndPermissionsCanOverlap() {

		RoleModel someone = aRole().can(doit).buildAsModel();
	
		User bill = bill().is(someone).can(doit).build();
		
		assertEqualSets(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesCanOverlap() {

		RoleModel someone = aRole("r1").can(doit).buildAsModel();
		RoleModel someoneElse = aRole("r2").can(doit).buildAsModel();
		
		User bill = bill().is(someone,someoneElse).build();
		
		assertTrue(bill.is(someone));
		assertTrue(bill.is(someoneElse));
		
		assertEqualSets(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesAreNotAddedTwice() {
		
		RoleModel someone = aRole().buildAsModel();
		
		User bill = bill().is(someone,someone).build();
		
		assertEqualSets(bill.roles(),new DefaultRole(someone));
	}
	

	
	
	//helper
	
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").fullName("bill");
	}
	
	
	private UserGrammar.ThirdClause  aRole() {
		return aRole("role");
	}
	
	private UserGrammar.ThirdClause aRole(String name) {
		return user().name(name).fullName(name);
	}
}
