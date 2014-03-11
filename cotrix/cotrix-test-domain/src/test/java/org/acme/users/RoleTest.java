package org.acme.users;

import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.action.Action;
import org.cotrix.common.Utils;
import org.cotrix.domain.dsl.grammar.UserGrammar;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class RoleTest extends DomainTest {
	
	Action doit = action(application,"doit");
	Action dothat = action(application,"dothat");
	Action dothatToo = action(application,"dothatToo");
	
	@Test
	public void assignTemplateRole() {

		Role something = role("r1").buildAsRoleFor(application);
		Role somethingElse = role("r2").buildAsRoleFor(application);
	
		User bill = like(bill().is(something,somethingElse).build());
		
		assertTrue(bill.is(something));
		assertTrue(bill.is(somethingElse));
		
		assertEqualUnordered(bill.directRoles(), something, somethingElse);
		assertEqualUnordered(bill.roles(), something, somethingElse);
		
	}
	
	@Test
	public void revokeTemplateRole() {

		Role something = role("r1").can(doit).buildAsRoleFor(application);
		Role somethingElse = role("r2").can(dothat).buildAsRoleFor(application);
	
		User bill = like(bill().is(something,somethingElse).build());
		
		User changeset = modifyUser(bill).isNoLonger(something).build();
		
		update(bill, changeset);
		
		assertFalse(bill.is(something));
		assertTrue(bill.is(somethingElse));
		
		assertEqualUnordered(bill.directRoles(), somethingElse);
		assertEqualUnordered(bill.roles(),somethingElse);
		
	}
	
	@Test
	public void rolesHaveIdentity() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = like(bill().can(doit).build());
		
		assertFalse(bill.is(something));
		
	}
	
	@Test
	public void assigningRolesGivesPermissions() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = like(bill().is(something).build());
		
		assertTrue(bill.can(doit));
		
	}
	
	@Test
	public void revokingRolesRemovesPermissions() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = like(bill().is(something).build());
		
		System.out.println(bill);
		
		assertTrue(bill.can(doit));
		
		System.out.println(bill);
		
		User changeset = modifyUser(bill).isNoLonger(something).build();
		
		System.out.println(bill);
		
		update(bill, changeset);
		
		System.out.println(bill);
		
		assertFalse(bill.can(doit));
		
		
	}
	
	@Test
	public void assignInstanceRoles() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
		
		User bill = like(bill().is(something.on("1")).build());
		
		assertFalse(bill.can(doit));
		
		assertTrue(bill.can(doit.on("1")));
	}
	
	@Test
	public void revokeInstanceRole() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
		
		User bill = like(bill().is(something.on("1")).build());
		
		assertTrue(bill.is(something.on("1")));
		
		User changeset = modifyUser(bill).isNoLonger(something.on("1")).build();
		
		update(bill, changeset);
		
		assertFalse(bill.is(something.on("1")));
		
	}
	
	
	@Test
	public void permissionsCanBeDirectOrIndirect() {

		Role something = aRole().can(doit).buildAsRoleFor(application);
	
		User bill = like(bill().is(something).can(dothat).build());
		
		assertEqualUnordered(bill.directPermissions(),dothat);
		assertEqualUnordered(bill.permissions(),doit,dothat);
	}
	
	
	@Test
	public void rolesCompose() {

		Role small = role("r1").can(doit).buildAsRoleFor(application);
		Role larger = role("r2").can(dothat).is(small).buildAsRoleFor(application);
		Role largerStill = role("r3").can(dothatToo).is(larger).buildAsRoleFor(application);
	
		User bill = like(bill().is(largerStill).build());
		
		assertEqualUnordered(bill.roles(),small,larger,largerStill);
		assertEqualUnordered(bill.directRoles(),largerStill);
		
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
		
		
		Role something = role("r1").can(doit).buildAsRoleFor(application);
		Role somethingElse = role("r2").can(dothat).is(something).buildAsRoleFor(application);
		
		User bill = like(bill().is(somethingElse).build());
		
		//add smaller role
		
		System.out.println("UPDATING");
		User changeset = modifyUser(bill).is(something).build();
		
		update(bill, changeset);
		
		assertEqualUnordered(bill.directRoles(),somethingElse);
	}
	
	@Test
	public void largerRolesReplaceSmallerOnes() {

		Role small = role("r1").can(doit).buildAsRoleFor(application);
		Role large = role("r2").can(dothat).is(small).buildAsRoleFor(application);
		
		
		User bill = like(bill().is(large).build());
		
		Role largerStill = role("r3").can(dothatToo).is(large).buildAsRoleFor(application);
		
		User changeset = modifyUser(bill).is(largerStill).build();
		
		update(bill, changeset);
		
		assertEqualUnordered(bill.directRoles(),largerStill);
		
		
	}
	
	
	@Test
	public void revokeRoleFromHierarchy() {

		Role something = role("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = role("somethingElse").can(dothat).is(something).buildAsRoleFor(application);
		
		User bill = like(bill().is(somethingElse).build());

		User changeset = modifyUser(bill).isNoLonger(something).build();
		
		update(bill, changeset);
		
		assertTrue(bill.is(something));
		
	}
	
	@Test
	public void roleHierarchiesFollowInstantiation() {

		Role something = role("role1").can(doit).buildAsRoleFor(codelists);
		Role somethingElse = role("role2").can(dothat).is(something).buildAsRoleFor(codelists);
		Role somethingElseStill = role("role3").can(dothatToo).is(somethingElse).buildAsRoleFor(codelists);
		
	
		User bill = like(bill().is(somethingElseStill.on("1")).build());
		
		assertEqualUnordered(bill.roles(),something.on("1"),somethingElse.on("1"), somethingElseStill.on("1"));
		
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
	
		User bill = like(bill().is(someone).can(doit).build());
		
		assertEqualUnordered(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesCanOverlap() {

		Role someone = role("r1").can(doit).buildAsRoleFor(application);
		Role someoneElse = role("r2").can(doit).buildAsRoleFor(application);
		
		User bill = like(bill().is(someone,someoneElse).build());
		
		assertTrue(bill.is(someone));
		assertTrue(bill.is(someoneElse));
		
		assertEqualUnordered(bill.permissions(),doit);
		
		
	}
	
	@Test
	public void rolesAreNotAddedTwice() {
		
		Role someone = aRole().buildAsRoleFor(application);
		
		User bill = like(bill().is(someone,someone).build());
		
		assertEqualUnordered(bill.roles(),someone);
	}
	
	@Test
	public void rolesCanBeReplaced() {
		
		Role someone = role("r1").buildAsRoleFor(application);
		//show hierarchy does not matter, replacemnt is replacemente
		Role someoneElse =role("r2").is(someone).buildAsRoleFor(application);
		
		User bill = like(bill().is(someoneElse).build());
		
		User changeset = modifyUser(bill).isNoLonger(bill.directRoles()).is(someone).build();
		
		update(bill,changeset);
		
		assertEqualUnordered(bill.directRoles(),someone);
	}
	
	
	
	//helper
	
	private User.Private reveal(User u) {
		return Utils.reveal(u,User.Private.class);
	}
	
	private void update(User u, User changeset) {
		reveal(u).update(reveal(changeset));
	}
	
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").fullName("bill").noMail();
	}
	
	
	private UserGrammar.ThirdClause  aRole() {
		return role("role");
	}
}
