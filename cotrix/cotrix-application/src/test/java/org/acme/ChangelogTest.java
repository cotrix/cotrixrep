package org.acme;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
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
	
	Code origin = code().name("c").attributes(a,same).build();
	Codelist list = codelist().name("l").definitions(def).with(origin).build();
	
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
	public void versionList() {
		
		users.add(user.get());
		
		codelists.add(list);
		
		list = service.bump(list).to("2");
		
		codelists.add(list);
		
	}
	
	@Test
	public void newCodesAreMarked() {
		
		Code code = code().name("newcode").build();
		
		codelists.update(modify(list).with(code).build());

		assertNotNull(list.codes().getFirst(code).attributes().getFirst(NEW));
		
	}
	
	@Test
	public void modifiedCodesAreMarked() {
		
		Code versionedCode = list.codes().getFirst(origin);
		
		Code change = modify(versionedCode).name("modified").build();
		
		codelists.update(modify(list).with(change).build());

		Code changed = list.codes().getFirst(change);
		
		assertNull(NEW.of(changed));
		
		assertNotNull(MODIFIED.of(changed));
		
	}
	
	@Test
	public void codenameChangesAreDetected() {
		
		Code versionedCode = list.codes().getFirst(origin);
		
		Code modified = modify(versionedCode).name("modified").build();
		
		codelists.update(modify(list).with(modified).build());
		
		String val = MODIFIED.of(versionedCode);
		
		System.out.println(val);
		
	}
	
	@Test
	public void attrChangesAreDetected() throws Exception {
		
		Code code = list.codes().getFirst(origin);
		
		Attribute attribute = code.attributes().getFirst(a);
		
		Attribute mattr = modify(attribute).name("aa").in("en").ofType(NAME_TYPE).value("someval").build();
		Attribute nattr = attribute().name("b").value("val").build();	
		
		Code modified = modify(code).name("cc").attributes(mattr,nattr).build();
		
		codelists.update(modify(list).with(modified).build());
		
		System.out.println(MODIFIED.of(code));
	
		Thread.sleep(1000);
		
		User fifi = user().name("fifi").fullName("fifi").noMail().build();
	
		user.set(fifi);
		
		Attribute nattr2 = attribute().name("c").value("val").build();
		
		modified = modify(code).attributes(nattr2).build();
		
		codelists.update(modify(list).with(modified).build());

		System.out.println(MODIFIED.of(code));

	}

	@Test
	public void definitionChangesAreDetected() throws Exception {
		
		Code code = list.codes().getFirst(origin);
		
		AttributeDefinition vdef = list.definitions().getFirst(def);
		
		Thread.sleep(1000);
		
		codelists.update(modify(list).definitions(modify(vdef).name("new").build()).build());
		
		changelog.track(list,false);

		System.out.println(MODIFIED.of(code));
	
		

	}
	
	@Test
	public void definitionRemovalsAreDetected() throws Exception {
		
		Code code = list.codes().getFirst(origin);
		
		AttributeDefinition vdef = list.definitions().getFirst(def);
		
		Thread.sleep(1000);
		
		codelists.update(list.id(),deleteAttrdef(vdef.id()));
		
		changelog.track(list,false);

		System.out.println(MODIFIED.of(code));
	
		

	}
	
}
