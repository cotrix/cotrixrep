package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.domain.dsl.grammar.UserGrammar;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class RoleTest {
	
	Action doit = action(application,"doit");
	Action dothat = action(application,"dothat");
	Action dothatToo = action(application,"dothatToo");
	
	@Test
	public void assignTemplateRole() {

		Role something = aUserModel().buildAsRoleFor(application);
	
		User bill = bill().is(something).build();
		
		assertTrue(bill.is(something));
		
	}
	
	@Test
	public void rolesHaveIdentity() {

		Role something = aUserModel().can(doit).buildAsRoleFor(application);
	
		User bill = bill().can(doit).build();
		
		assertFalse(bill.is(something));
		
	}
	
	@Test
	public void rolesGivePermissions() {

		Role something = aUserModel().can(doit).buildAsRoleFor(application);
	
		User bill = bill().is(something).build();
		
		assertTrue(bill.can(doit));
		
	}
	
	@Test
	public void assignInstanceRoles() {

		Role something = aUserModel().can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(something.on("1")).build();
		
		assertFalse(bill.can(doit));
		
		assertTrue(bill.can(doit.on("1")));
	}
	
	
	@Test
	public void permissionsCanBeDirectOrIndirect() {

		Role something = aUserModel().can(doit).buildAsRoleFor(application);
	
		User bill = bill().is(something).can(dothat).build();
		
		assertEqualSets(bill.directPermissions(),dothat);
		assertEqualSets(bill.permissions(),doit,dothat);
	}
	
	
	@Test
	public void roleFormHierarchies() {

		Role something = aUserModel("role1").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("role2").can(dothat).is(something).buildAsRoleFor(application);
		Role somethingElseStill = aUserModel("role3").can(dothatToo).is(somethingElse).buildAsRoleFor(application);
	
		User bill = bill().is(somethingElseStill).build();
		
		assertEqualSets(bill.roles(),something,somethingElse, somethingElseStill);
		
		assertTrue(bill.is(something));
		assertTrue(bill.is(somethingElse));
		assertTrue(bill.is(somethingElseStill));
		
		//role permissions propagate
		assertTrue(bill.can(doit));
		assertTrue(bill.can(dothat));
		assertTrue(bill.can(dothatToo));
		
	}
	
	@Test
	public void roleHierarchiesFollowInstantiation() {

		Role something = aUserModel("role1").can(doit).buildAsRoleFor(codelists);
		Role somethingElse = aUserModel("role2").can(dothat).is(something).buildAsRoleFor(codelists);
		Role somethingElseStill = aUserModel("role3").can(dothatToo).is(somethingElse).buildAsRoleFor(codelists);
		
	
		User bill = bill().is(somethingElseStill.on("1")).build();
		
		assertEqualSets(bill.roles(),something.on("1"),somethingElse.on("1"), somethingElseStill.on("1"));
		
		assertTrue(bill.is(something.on("1")));
		assertTrue(bill.is(somethingElse.on("1")));
		assertTrue(bill.is(somethingElseStill.on("1")));
		
		//role permissions propagate
		assertTrue(bill.can(doit.on("1")));
		assertTrue(bill.can(dothat.on("1")));
		assertTrue(bill.can(dothatToo.on("1")));
		
	}

	@Test
	public void rolesAndPermissionsCanOverlap() {

		Role someone = aUserModel().can(doit).buildAsRoleFor(application);
	
		User bill = bill().is(someone).can(doit).build();
		
		assertEqualSets(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesCanOverlap() {

		Role someone = aUserModel("r1").can(doit).buildAsRoleFor(application);
		Role someoneElse = aUserModel("r2").can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(someone,someoneElse).build();
		
		assertTrue(bill.is(someone));
		assertTrue(bill.is(someoneElse));
		
		assertEqualSets(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesAreNotAddedTwice() {
		
		Role someone = aUserModel().buildAsRoleFor(application);
		
		User bill = bill().is(someone,someone).build();
		
		assertEqualSets(bill.roles(),someone);
	}
	

	@Test
	public void test() {
		
		
	}
	
	
	
	//helper
	
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").noMail().fullName("bill");
	}
	
	
	private UserGrammar.ThirdClause  aUserModel() {
		return aUserModel("role");
	}
	
	private UserGrammar.ThirdClause aUserModel(String name) {
		return user().name(name).email("some@mail.com").fullName(name);
	}
}
