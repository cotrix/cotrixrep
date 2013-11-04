package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.user.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.common.Utils;
import org.cotrix.user.User;
import org.junit.Test;

public class UserTest {

	@Test
	public void isBuildCorrectly() {

		User joe = user("joe").fullName("joe the plummer").can(CodelistAction.values()).cannot(LOCK).build();

		assertEquals("joe", joe.id());
		assertEquals("joe the plummer", joe.fullName());

		for (Action a : CodelistAction.values())
			if (!a.equals(LOCK))
				assertTrue(joe.permissions().contains(a));
			else
				assertFalse(joe.permissions().contains(a));
	}

	@Test
	public void canBeFluentlyConstructed() {

		String name = "name";
		Action a = action("a");
		User u;

		// new users
		u = user().name(name).fullName(name).build();
		u = user().name(name).fullName(name).can(a).build();
		u = user().name(name).fullName(name).can(a).cannot(a).build();

		assertFalse(reveal(u).isChangeset());
		assertNull(reveal(u).status());

		// modified users
		u = user("1").fullName(name).build();
		u = user("1").fullName(name).can(a).build();
		u = user("1").fullName(name).can(a).cannot(a).build();

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
