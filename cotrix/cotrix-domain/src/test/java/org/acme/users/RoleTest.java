package org.acme.users;

import static junit.framework.Assert.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.common.Utils;
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

		Role something = aRole("r1").buildAsRoleFor(application);
		Role somethingElse = aRole("r2").buildAsRoleFor(application);
	
		User bill = bill().is(something,somethingElse).build();
		
		System.out.println(bill);
		
		assertTrue(bill.is(something));
		assertTrue(bill.is(somethingElse));
		
		assertEqualSets(bill.directRoles(), something, somethingElse);
		assertEqualSets(bill.roles(), something, somethingElse);
		
	}
	
	@Test
	public void revokeTemplateRole() {

		Role something = aRole("r1").buildAsRoleFor(application);
		Role somethingElse = aRole("r2").buildAsRoleFor(application);
	
		User bill = bill().is(something,somethingElse).build();
		
		User changeset = modifyUser(bill).isNot(something).build();
		
		update(bill, changeset);
		
		assertFalse(bill.is(something));
		assertTrue(bill.is(somethingElse));
		
		assertEqualSets(bill.directRoles(), somethingElse);
		assertEqualSets(bill.roles(),somethingElse);
		
	}
	
	@Test
	public void rolesHaveIdentity() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = bill().can(doit).build();
		
		assertFalse(bill.is(something));
		
	}
	
	@Test
	public void rolesGivePermissions() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = bill().is(something).build();
		
		assertTrue(bill.can(doit));
		
	}
	
	@Test
	public void assignInstanceRoles() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(something.on("1")).build();
		
		assertFalse(bill.can(doit));
		
		assertTrue(bill.can(doit.on("1")));
	}
	
	@Test
	public void revokeInstanceRole() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(something.on("1")).build();
		
		assertTrue(bill.is(something.on("1")));
		
		User changeset = modifyUser(bill).isNot(something.on("1")).build();
		
		update(bill, changeset);
		
		assertFalse(bill.is(something.on("1")));
		
	}
	
	
	@Test
	public void permissionsCanBeDirectOrIndirect() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = bill().is(something).can(dothat).build();
		
		assertEqualSets(bill.directPermissions(),dothat);
		assertEqualSets(bill.permissions(),doit,dothat);
	}
	
	
	@Test
	public void rolesCompose() {

		Role small = aRole("r1").can(doit).buildAsRoleFor(application);
		Role larger = aRole("r2").can(dothat).is(small).buildAsRoleFor(application);
		Role largerStill = aRole("r3").can(dothatToo).is(larger).buildAsRoleFor(application);
	
		User bill = bill().is(largerStill).build();
		
		assertEqualSets(bill.roles(),small,larger,largerStill);
		assertEqualSets(bill.directRoles(),largerStill);
		
		assertTrue(bill.is(small));
		assertTrue(bill.is(larger));
		assertTrue(bill.is(largerStill));
		
		assertTrue(bill.isDirectly(largerStill));
		assertFalse(bill.isDirectly(larger));
		assertFalse(bill.isDirectly(small));
		
		//role permissions propagate
		assertTrue(bill.can(doit));
		assertTrue(bill.can(dothat));
		assertTrue(bill.can(dothatToo));
		
	}
	
	@Test
	public void smallerRolesAreNotAdded() {
		
		
		Role something = aRole("r1").can(doit).buildAsRoleFor(application);
		Role somethingElse = aRole("r2").can(dothat).is(something).buildAsRoleFor(application);
		
		User bill = bill().is(somethingElse).build();
		
		//add smaller role
		
		User changeset = modifyUser(bill).is(something).build();
		
		update(bill, changeset);
		
		assertEqualSets(bill.directRoles(),somethingElse);
	}
	
	@Test
	public void largerRolesReplaceSmallerOnes() {

		Role small = aRole("r1").can(doit).buildAsRoleFor(application);
		Role large = aRole("r2").can(dothat).is(small).buildAsRoleFor(application);
		
		
		User bill = bill().is(large).build();
		
		Role largerStill = aRole("r3").can(dothatToo).is(large).buildAsRoleFor(application);
		
		User changeset = modifyUser(bill).is(largerStill).build();
		
		update(bill, changeset);
		
		assertEqualSets(bill.directRoles(),largerStill);
		
		
	}
	
	
	@Test
	public void revokeRoleFromHiearchy() {

		Role something = aRole("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aRole("somethingElse").can(dothat).is(something).buildAsRoleFor(application);
		
		User bill = bill().is(somethingElse).build();

		User changeset = modifyUser(bill).isNot(something).build();
		
		update(bill, changeset);
		
		assertTrue(bill.is(something));
		
	}
	
	@Test
	public void roleHierarchiesFollowInstantiation() {

		Role something = aRole("role1").can(doit).buildAsRoleFor(codelists);
		Role somethingElse = aRole("role2").can(dothat).is(something).buildAsRoleFor(codelists);
		Role somethingElseStill = aRole("role3").can(dothatToo).is(somethingElse).buildAsRoleFor(codelists);
		
	
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

		Role someone = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = bill().is(someone).can(doit).build();
		
		assertEqualSets(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesCanOverlap() {

		Role someone = aRole("r1").can(doit).buildAsRoleFor(application);
		Role someoneElse = aRole("r2").can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(someone,someoneElse).build();
		
		assertTrue(bill.is(someone));
		assertTrue(bill.is(someoneElse));
		
		assertEqualSets(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesAreNotAddedTwice() {
		
		Role someone = aRole().buildAsRoleFor(application);
		
		User bill = bill().is(someone,someone).build();
		
		assertEqualSets(bill.roles(),someone);
	}
	
	
	
	//helper
	
	private User.Private reveal(User u) {
		return Utils.reveal(u,User.Private.class);
	}
	
	private void update(User u, User changeset) {
		reveal(u).update(reveal(changeset));
	}
	
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").noMail().fullName("bill");
	}
	
	
	private UserGrammar.ThirdClause  aRole() {
		return aRole("role");
	}
	
	private UserGrammar.ThirdClause aRole(String name) {
		return user().name(name).noMail().fullName(name);
	}
}
