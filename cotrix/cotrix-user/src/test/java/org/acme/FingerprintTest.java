package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.Action.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.user.Users.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.user.Role;
import org.cotrix.user.RolesAndPermissions;
import org.cotrix.user.User;
import org.cotrix.user.dsl.UserGrammar;
import org.junit.Test;

public class FingerprintTest {

	Action doit = action(application,"doit");
	Action dothat = action(application,"dothat");
	
	Action doitOnCodes = action(codelists,"doitoncodes");
	Action doThatOnCodes = action(codelists,"dothatoncodes");
	
	
	@Test
	public void fingerprintWithApplicationPermissions() {
		
		User bill = bill().can(doit,dothat).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application),fp.keySet());
		assertEquals(setOf(any),fp.get(application).keySet());
		assertEquals(setOf(doit,dothat),fp.get(application).get(any).permissions());
	}
	
	@Test
	public void fingerprintWithMixedPermissions() {
		
		User bill = bill().can(doit,doitOnCodes.on("1"), doThatOnCodes).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.keySet());
		assertEquals(setOf(any),fp.get(application).keySet());
		assertEquals(setOf(doit),fp.get(application).get(any).permissions());

		assertEquals(setOf(any,"1"),fp.get(codelists).keySet());
		assertEquals(setOf(doitOnCodes.on("1")),fp.get(codelists).get("1").permissions());
		assertEquals(setOf(doThatOnCodes),fp.get(codelists).get(any).permissions());

	}
	
	
	
	@Test
	public void fingerprintWithRoles() {
		

		Role something = aUserModel().can(doit).buildAsRoleFor(application);
		
		User bill = bill().is(something).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application),fp.keySet());
		assertEquals(setOf(something.name()),fp.get(application).get(any).roles());
		assertEquals(setOf(doit),fp.get(application).get(any).permissions());
		
	}
	
	@Test
	public void fingerprintMixedRoles() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").can(doitOnCodes).buildAsRoleFor(codelists);
		
		User bill = bill().is(something, somethingElse).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.keySet());
		
		assertEquals(setOf(something.name()),fp.get(application).get(any).roles());
		assertEquals(setOf(doit),fp.get(application).get(any).permissions());
		
		assertEquals(setOf(somethingElse.name()),fp.get(codelists).get(any).roles());
		assertEquals(setOf(doitOnCodes),fp.get(codelists).get(any).permissions());
		
	}
	
	@Test
	public void fingerprintWithInstantiatedRoles() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").can(doitOnCodes).buildAsRoleFor(codelists);
		
		User bill = bill().is(something, somethingElse.on("1")).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.keySet());
		
		assertEquals(setOf(something.name()),fp.get(application).get(any).roles());
		assertEquals(setOf(doit),fp.get(application).get(any).permissions());
		
		assertEquals(setOf(somethingElse.name()),fp.get(codelists).get("1").roles());
		assertEquals(setOf(doitOnCodes.on("1")),fp.get(codelists).get("1").permissions());
		
	}
	
	@Test
	public void fingerprintWithRoleHierarchies() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").is(something).can(dothat).buildAsRoleFor(application);
		
		User bill = bill().is(somethingElse).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application),fp.keySet());
		assertEquals(setOf(something.name(),somethingElse.name()),fp.get(application).get(any).roles());
		assertEquals(setOf(doit,dothat),fp.get(application).get(any).permissions());
		
	}
	
	@Test
	public void fingerprintWithMixedRoleHierarchies() {
		

		Role something = aUserModel("something").can(doit).buildAsRoleFor(application);
		Role somethingElse = aUserModel("somethingElse").is(something).can(doitOnCodes).buildAsRoleFor(codelists);
		Role somethingElseStill = aUserModel("somethingElseStill").is(somethingElse).can(doThatOnCodes).buildAsRoleFor(codelists);
		
		User bill = bill().is(somethingElseStill.on("1")).build();
		
		Map<ResourceType,Map<String,RolesAndPermissions>> fp = bill.fingerprint();
		
		//System.out.println(fp);
		
		assertEquals(setOf(application,codelists),fp.keySet());
		
		assertEquals(setOf(something.name()),fp.get(application).get(any).roles());
		assertEquals(setOf(doit),fp.get(application).get(any).permissions());
		
		assertEquals(setOf("1"),fp.get(codelists).keySet());
		
		assertEquals(setOf(somethingElse.name(),somethingElseStill.name()),fp.get(codelists).get("1").roles());
		assertEquals(setOf(doitOnCodes.on("1"),doThatOnCodes.on("1")),fp.get(codelists).get("1").permissions());
		
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
		return user().name(name).fullName(name);
	}
	private UserGrammar.ThirdClause bill() {
		return user().name("bill").fullName("bill");
	}
}
