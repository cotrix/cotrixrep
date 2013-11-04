package org.acme;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.user.Users.*;

import org.cotrix.user.RoleModel;
import org.cotrix.user.User;
import org.junit.Test;

public class PermissionTest {

	@Test
	public void permissionTemplate() {
		
		User joe = user().name("joe").fullName("joe").can(EDIT).build();
		
		assertEquals(asList(EDIT),joe.permissions());
		
		assertTrue(joe.can(EDIT));
		
		assertTrue(joe.can(EDIT.on("1")));
		
		assertFalse(joe.can(LOCK.on("1")));
	}
	
	@Test
	public void permissionsCannotBeAddedTwice() {
		
		User joe = user().name("joe").fullName("joe").can(EDIT,EDIT).build();
		
		assertEquals(asList(EDIT),joe.permissions());
	}
	
	@Test
	public void moreSpecificPermissionsAreRetained() {
		
		//when we add or merge permissions we remove only duplicates, not redundant specifications 
		
		User joe = user().name("joe").fullName("joe").can(EDIT.on("1"),EDIT,EDIT.on("1")).build();
		
		assertEquals(2,joe.permissions().size());
	}
	
	@Test
	public void roleWithTemplates() {

		RoleModel editor = user().name("editor").fullName("editor").can(EDIT).buildAsModel();
	
		RoleModel editor2 = user().name("editor2").fullName("editor").can(EDIT).buildAsModel();
		
		User joe = user().name("joe").fullName("joe").is(editor).build();
		

		//role is assigned
		assertTrue(joe.is(editor));
		
		//roles are more than that permissions
		assertFalse(joe.is(editor2));
		
		//role permissions propagate
		assertTrue(joe.can(EDIT));
		
	}
	
	@Test
	public void roleWithResourceActions() {

		RoleModel editor = user().name("editor").fullName("editor").can(EDIT).buildAsModel();
	
		User joe = user().name("joe").fullName("joe").is(editor.on("1")).build();
		
		assertFalse(joe.can(EDIT));
		assertTrue(joe.can(EDIT.on("1")));
	}
	
	
	@Test
	public void inheritedVsInheritedPermissions() {

		RoleModel editor = user().name("editor").fullName("editor").can(EDIT).buildAsModel();
	
		User joe = user().name("joe").fullName("joe").is(editor.on("1")).can(LOCK).build();
		
		assertEquals(1,joe.declaredPermissions().size());
		assertEquals(2,joe.permissions().size());
	}
	
	@Test
	public void hierarchicalRole() {

		RoleModel role1 = user().name("editor").fullName("editor").can(EDIT,LOCK).buildAsModel();
	
		RoleModel role2 = user().name("editor2").fullName("editor").is(role1).can(VIEW).buildAsModel();
		
		User joe = user().name("joe").fullName("joe").is(role2).build();
		

		assertTrue(joe.is(role1));
		assertTrue(joe.is(role2));
		
		
		//role permissions propagate
		assertTrue(joe.can(EDIT));
		assertTrue(joe.can(VIEW));
		
	}

	@Test
	public void rolesAndPermissionsCanOverlap() {

		RoleModel role = user().name("editor").fullName("editor").can(EDIT).buildAsModel();
	
		User joe = user().name("joe").fullName("joe").is(role).can(EDIT).build();
		
		assertEquals(1,joe.permissions().size());
		
		
	}
	
	@Test
	public void rolesCanOverlap() {

		RoleModel role1 = user().name("editor").fullName("editor").can(EDIT).buildAsModel();
	
		RoleModel role2 = user().name("editor2").fullName("editor").can(EDIT).buildAsModel();
		
		User joe = user().name("joe").fullName("joe").is(role1,role2).build();
		
		assertTrue(joe.is(role1));
		assertTrue(joe.is(role2));
		
		assertEquals(1,joe.permissions().size());
		
		
	}
	
	@Test
	public void rolesCannotBeAddedTwice() {
		
		RoleModel role = user().name("editor").fullName("editor").can(EDIT,LOCK).buildAsModel();
		
		User joe = user().name("joe").fullName("joe").is(role,role).build();
		
		assertEquals(1,joe.roles().size());
	}
}
