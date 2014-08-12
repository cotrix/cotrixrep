package org.acme;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.repository.CodelistActions.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.ChangelogService;
import org.cotrix.application.VersioningService;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.TestUser;
import org.junit.Before;
import org.junit.Test;

public class ChangelogTest extends ApplicationTest {

	AttributeDefinition def = attrdef().name("a").build();
	Attribute a = attribute().instanceOf(def).build();
	Attribute same = attribute().name("same").build();
	
	Code code = code().name("c").attributes(a,same).build();
	Codelist list= codelist().name("l").definitions(def).with(code).build();
	
	Codelist vlist;
	Code vcode;
	
	@Inject
	VersioningService service;

	@Inject
	ChangelogService changelog;
	
	@Inject
	CodelistRepository codelists;
	
	@Inject
	UserRepository users;
	
	@Inject
	TestUser user;

	
	@Before
	public void prepareVersion() {
		
		users.add(user.get());
		
		codelists.add(list);
		
		vlist = service.bump(list).to("2");
		
		codelists.add(vlist);
		
		vcode = vlist.codes().getFirst(code);
		
	}
	
	@Test
	public void codelistsOnlyAreMarkedOnStorage() {
		
		//list is timestamped
		assertTrue(CREATED.isIn(list));
		
		//not so codes
		assertFalse(CREATED.isIn(code));
		
		//same true after versioning
		
		assertTrue(CREATED.isIn(vlist));
		assertFalse(CREATED.isIn(vcode));
		
	}
	
	@Test
	public void newCodesAreTimeStampedOnUpdate() {
		
		Code newcode = code().name("newcode").build();
		
		codelists.update(modify(list).with(newcode));
		
		//timestamped, not marked
		assertTrue(CREATED.isIn(newcode));
		assertTrue(UPDATED_BY.isIn(newcode));
		
		assertFalse(NEW.isIn(newcode));
		
		assertFalse(MODIFIED.isIn(newcode));
		assertFalse(LAST_UPDATED.isIn(newcode));
	}
	
	@Test
	public void newCodesAreTimeStampedAndMarkedOnAllUpdatesToVersions() {
		
		//added to version get both timestamped and marked.		
		Code newcode = code().name("newcode").build();
		
		codelists.update(modify(vlist).with(newcode));

		assertTrue(CREATED.isIn(newcode));
		assertTrue(UPDATED_BY.isIn(newcode));
		assertTrue(NEW.isIn(newcode));
		
		assertFalse(MODIFIED.isIn(newcode));
		assertFalse(LAST_UPDATED.isIn(newcode));
	}
	
	@Test
	public void changesAreTimestampedOnUpdate() {
		
		codelists.update(modify(list).with(modify(code).name("modified")));

		assertTrue(LAST_UPDATED.isIn(code));
		assertTrue(UPDATED_BY.isIn(code));
		
		//they are not marked
		assertFalse(CREATED.isIn(code));
		assertFalse(NEW.isIn(code));
		assertFalse(MODIFIED.isIn(code));
	}
	
	@Test
	public void changesAreTimestampedAndMarkedOnUpdateToVersions() {
		
		codelists.update(modify(vlist).with(modify(vcode).name("modified")));

		assertTrue(LAST_UPDATED.isIn(vcode));
		assertTrue(UPDATED_BY.isIn(vcode));
		assertTrue(MODIFIED.isIn(vcode));
		
		assertFalse(CREATED.isIn(vcode));
		assertFalse(NEW.isIn(vcode));
		
	}
	
	@Test
	public void attrChangesAreMarkedOnUpdateToVersions() throws Exception {
		
		Attribute vattr = vcode.attributes().getFirst(a);
		
		Attribute modattr = modify(vattr).name("aa").in("en").ofType(NAME_TYPE).value("someval").build();
		Attribute newattr = attribute().name("b").value("val").build();	
		
		codelists.update(modify(vlist).with(modify(vcode).name("cc").attributes(modattr,newattr)));
		
		//coarse grained but convenient
		assertNotNull(MODIFIED.of(vcode));
		
		Thread.sleep(1000);
		
		User fifi = user().name("fifi").fullName("fifi").noMail().build();
	
		user.set(fifi);
		
		Attribute newattr2 = attribute().name("c").value("val").build();
		
		codelists.update(modify(vlist).with(modify(vcode).attributes(newattr2)));

		System.out.println(MODIFIED.of(vcode));

	}

	@Test
	public void definitionChangesAreDetected() throws Exception {
		
		AttributeDefinition vdef = vlist.attributeDefinitions().getFirst(def);
		
		Thread.sleep(1000);
		
		codelists.update(modify(vlist).definitions(modify(vdef).name("new")));
		
		changelog.track(vlist,false);

		System.out.println(MODIFIED.of(vcode));
	
		

	}
	
	@Test
	public void definitionRemovalsAreDetected() throws Exception {
		
		AttributeDefinition vdef = vlist.attributeDefinitions().getFirst(def);
		
		Thread.sleep(1000);
		
		codelists.update(vlist.id(),deleteAttrdef(vdef.id()));
		
		changelog.track(vlist,false);

		System.out.println(MODIFIED.of(vcode));
	
		

	}
	
	
}
