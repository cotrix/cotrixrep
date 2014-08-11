package org.acme.users;

import static org.cotrix.action.Actions.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.common.Status.*;
import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.action.MainAction;
import org.cotrix.common.CommonUtils;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class UserTest {

	@Test
	public void isBuiltCorrectly() {

		Role model = role("model").can(MainAction.values()).buildAsRoleFor(application);
		
		User joe = user().name("joe").fullName("joe the plummer").email("joe@me.com").
								can(CodelistAction.values()).cannot(LOCK).
								is(model).build();

		assertEquals("joe", joe.name());
		assertEquals("joe@me.com", joe.email());
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
		Role m = role(name).buildAsRoleFor(application);
		User u;

		// new users
		u = user().name(name).fullName("name").email("joe@me.com").build();
		u = user().name(name).fullName("name").email("joe@me.com").can(a).build();
		u = user().name(name).fullName("name").email("joe@me.com").can(a).cannot(a).build();
		u = user().name(name).fullName("name").email("joe@me.com").can(a).is(m).build();

		assertFalse(reveal(u).isChangeset());
		assertNull(reveal(u).status());

		// modified users
		u = modifyUser(u).email("joe@me.com").build();
		u = modifyUser(u).email("joe@me.com").can(a).build();
		u = modifyUser(u).email("joe@me.com").can(a).cannot(a).build();
		u = modifyUser(u).email("joe@me.com").can(a).cannot(a).is(m).build();

		assertTrue(reveal(u).isChangeset());
		assertEquals(MODIFIED, reveal(u).status());

	}

	private User.Private reveal(User u) {
		return CommonUtils.reveal(u, User.Private.class);
	}
}
