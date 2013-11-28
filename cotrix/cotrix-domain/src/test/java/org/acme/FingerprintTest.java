package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Action.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.HashSet;
import java.util.Set;

import org.cotrix.action.Action;
import org.cotrix.domain.dsl.grammar.UserGrammar;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.junit.Test;

public class FingerprintTest {

	Action doit = action(application,"doit");
	Action dothat = action(application,"dothat");
	
	Action doitOnCodes = action(codelists,"doitoncodes");
	Action doThatOnCodes = action(codelists,"dothatoncodes");
	
	
	@Test
	public void fingerprintWithApplicationPermissions() {
		
		User bill = bill().can(doit,dothat).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application),fp.types());
		assertEquals(setOf(any),fp.resources(application));
		assertEquals(setOf(doit,dothat),fp.permissionsOver(any,application));
	}
	
	@Test
	public void fingerprintWithMixedPermissions() {
		
		User bill = bill().can(doit,doitOnCodes.on("1"), doThatOnCodes).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.types());
		assertEquals(setOf(any),fp.resources(application));
		assertEquals(setOf(doit),fp.permissionsOver(any,application));

		assertEquals(setOf(any,"1"),fp.resources(codelists));
		
		//includes permission over all codelists
		assertEquals(setOf(doThatOnCodes,doitOnCodes.on("1")),fp.permissionsOver("1",codelists));
		
		assertEquals(setOf(doThatOnCodes),fp.permissionsOver(any,codelists));

	}
	
	
	
	@Test
	public void fingerprintWithRoles() {
		

		Role something = aUserModel().can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(something).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application),fp.types());
		assertEquals(setOf(something.name()),fp.rolesOver(any,application));
		assertEquals(setOf(doit),fp.permissionsOver(any,application));
		
	}
	
	@Test
	public void fingerprintMixedRoles() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").can(doitOnCodes).buildAsRoleFor(codelists);
		
		User bill = bill().is(something, somethingElse).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.types());
		
		assertEquals(setOf(something.name()),fp.rolesOver(any,application));
		assertEquals(setOf(doit),fp.permissionsOver(any,application));
		
		assertEquals(setOf(somethingElse.name()),fp.rolesOver(any,codelists));
		assertEquals(setOf(doitOnCodes),fp.permissionsOver(any,codelists));
		
	}
	
	@Test
	public void fingerprintWithInstantiatedRoles() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").can(doitOnCodes).buildAsRoleFor(codelists);
		
		User bill = bill().is(something, somethingElse.on("1")).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.types());
		
		assertEquals(setOf(something.name()),fp.rolesOver(any,application));
		assertEquals(setOf(doit),fp.permissionsOver(any,application));
		
		assertEquals(setOf(somethingElse.name()),fp.rolesOver("1",codelists));
		assertEquals(setOf(doitOnCodes.on("1")),fp.permissionsOver("1",codelists));
		
	}
	
	@Test
	public void fingerprintWithRoleHierarchies() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").is(something).can(dothat).buildAsRoleFor(application);
		
		User bill = bill().is(somethingElse).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application),fp.types());
		assertEquals(setOf(something.name(),somethingElse.name()),fp.rolesOver(any,application));
		assertEquals(setOf(doit,dothat),fp.permissionsOver(any,application));
		
	}
	
	@Test
	public void fingerprintWithMixedRoleHierarchies() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").is(something).can(doitOnCodes).buildAsRoleFor(codelists);
		Role somethingElseStill = aUserModel("somethingElseStill").is(somethingElse).can(doThatOnCodes).buildAsRoleFor(codelists);
		
		User bill = bill().is(somethingElseStill.on("1")).build();
		
		FingerPrint fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.types());
		
		assertEquals(setOf(something.name()),fp.rolesOver(any,application));
		assertEquals(setOf(doit),fp.permissionsOver(any,application));
		
		assertEquals(setOf("1"),fp.resources(codelists));
		
		assertEquals(setOf(somethingElse.name(),somethingElseStill.name()),fp.rolesOver("1",codelists));
		assertEquals(setOf(doitOnCodes.on("1"),doThatOnCodes.on("1")),fp.permissionsOver("1",codelists));
		
	}
	
	<T> Set<T> setOf(T... ts) {
		HashSet<T> set = new HashSet<T>();
		for (T t : ts)
			set.add(t);
		return set;
	}
	
	//helpers
	
	private UserGrammar.ThirdClause  aUserModel() {
		return aUserModel("role");
	}
	
	private UserGrammar.ThirdClause aUserModel(String name) {
		return user().name(name).noMail().fullName(name);
	}
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").noMail().fullName("bill");
	}
}
