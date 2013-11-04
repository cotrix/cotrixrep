package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.user.Users.*;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.common.Utils;
import org.cotrix.user.RoleModel;
import org.cotrix.user.User;
import org.junit.Test;

public class UserTest {

	@Test
	public void isBuiltCorrectly() {

		RoleModel model = user().name("model").fullName("test model").can(MainAction.values()).buildAsModel();
		
		User joe = user().name("joe").fullName("joe the plummer").
								can(CodelistAction.values()).cannot(LOCK).
								is(model).build();

		assertEquals("joe", joe.name());
		assertEquals("joe the plummer", joe.fullName());

		for (Action a : CodelistAction.values())
			if (!a.equals(LOCK))
				assertTrue(joe.can(a));
			else
				assertFalse(joe.can(a));
		
		assertTrue(joe.is(model));
		
		for (Action a : MainAction.values())
			assertTrue(joe.can(a));
	}

	@Test
	public void canBeFluentlyConstructed() {

		String name = "name";
		Action a = action("a");
		RoleModel m = user().name(name).fullName(name).buildAsModel();
		User u;

		// new users
		u = user().name(name).fullName(name).build();
		u = user().name(name).fullName(name).can(a).build();
		u = user().name(name).fullName(name).can(a).cannot(a).build();
		u = user().name(name).fullName(name).can(a).is(m).build();

		assertFalse(reveal(u).isChangeset());
		assertNull(reveal(u).status());

		// modified users
		u = user("1").fullName(name).build();
		u = user("1").fullName(name).can(a).build();
		u = user("1").fullName(name).can(a).cannot(a).build();
		u = user("1").fullName(name).can(a).cannot(a).is(m).build();

		assertTrue(reveal(u).isChangeset());
		assertEquals(MODIFIED, reveal(u).status());

		// removed attributes
		u = user("1").delete();

		assertTrue(reveal(u).isChangeset());
		assertEquals(DELETED, reveal(u).status());

	}

	private User.Private reveal(User u) {
		return Utils.reveal(u, User.Private.class);
	}
}
