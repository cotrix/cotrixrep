package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.user.dsl.Users.*;

import org.cotrix.action.Action;
import org.cotrix.action.CodelistAction;
import org.cotrix.user.User;
import org.junit.Test;

public class UserTest {

	@Test
	public void isBuildCorrectly() {
		
		User joe = user("joe").fullName("joe the plummer").can(CodelistAction.values()).cannot(LOCK).build();
		
		assertEquals("joe", joe.id());
		assertEquals("joe the plummer", joe.name());
		
		for (Action a : CodelistAction.values())
			if (!a.equals(LOCK))
				assertTrue(joe.permissions().contains(a));
			else
				assertFalse(joe.permissions().contains(a));
	}
}
