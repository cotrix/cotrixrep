package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.trait.Identified;
import org.junit.Test;

public class UpdateTest {

	// ###########################  attributes

	@Test
	public void changesAttributes() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();

		update(a, modifyAttribute(a.id()).name(name2).value(value2).ofType(type2).in(language2).build());

		assertEquals(name2, a.name());
		assertEquals(value2, a.value());
		assertEquals(type2, a.type());
		assertEquals(language2, a.language());
	}
	
	@Test
	public void changesAttributesPartially() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();

		update(a, modifyAttribute(a.id()).build()); //let's test by changing nothing

		assertEquals(name, a.name());
		assertEquals(value, a.value());
		assertEquals(type, a.type());
		assertEquals(language, a.language());
	}

	@Test
	public void cannotErasetNameOfAttributes() {

		Attribute a = attribute().name(name).build();

		try {
			update(a, modifyAttribute(a.id()).name(NULL_QNAME).build());
			fail();
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	public void erasesAttributeValueTypeOrLanguage() {

		Attribute a = attribute().name(name).value(value).ofType(type).in(language).build();

		update(a, modifyAttribute(a.id()).value(NULL_STRING).ofType(NULL_QNAME).in(NULL_STRING).build());
		
		assertNull(a.value());
		assertNull(a.type());
		assertNull(a.language());
	}
	
	
	//####################################################### codes

	
	@Test
	public void cannotEraseNameOfCodes() {
		
		try {
			
			Code code = code().name(name).build();
			
			update(code,modifyCode(code.id()).name(NULL_QNAME).build());
			
			fail();
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Test
	public void changesNameOfCodes() {

		Code code = code().name(name).build();
		
		update(code,modifyCode(code.id()).name(name2).build());
		
		assertEquals(name2,code.name());
		
	}
	
	
	@Test
	public void changesCodeAttributes() {

		Attribute a1 = attribute().name(name).build();
		Attribute a2 = attribute().name(name2).build();
		Attribute a3 = attribute().name(name3).build();
		
		Code code = code().name(name).attributes(a1,a2,a3).build();
		
		
		QName newname = q("changed");
		// a change
		Attribute modified = modifyAttribute(a1.id()).name(newname).build();

		// a deletion
		Attribute deleted = deleteAttribute(a2.id());

		// an addition
		Attribute a4 = attribute().name("new").build();

		Code change = modifyCode(code.id()).attributes(modified, deleted, a4).build();
		

		update(code,change);

		
		NamedContainer<? extends Attribute> attributes = code.attributes();
		
		System.out.println(attributes);
		
		assertEquals(5, attributes.size());
		
		assertTrue(attributes.contains(newname));
		
		assertFalse(attributes.contains(a2));
		
		assertTrue(attributes.contains(a3));
		
		assertTrue(attributes.contains(a4));

	}
	
	@Test
	public void codesAcquireAndChangeUpdateTimeStamp() throws Exception {
		
		Attribute a1 = attribute().name(name).build();

		Code code = code().name(name).attributes(a1).build();
		
		// a change
		Attribute modified = modifyAttribute(a1.id()).name("newname").build();
		
		Code change = modifyCode(code.id()).attributes(modified).build();

		update(code,change);
		
		String update_time = null;
		
		for (Attribute a : code.attributes())
			if (a.name().equals(UPDATE_TIME))
				update_time = a.value();

		assertNotNull("update time attribute is missing",update_time);
		
		//let at least one second pass
		Thread.sleep(1000);
		
		modified = modifyAttribute(a1.id()).name("yetanother").build();

		change = modifyCode(code.id()).attributes(modified).build();

		update(code,change);
		
		String new_update_time = null;
		
		for (Attribute a : code.attributes())
			if (a.name().equals(UPDATE_TIME))
				new_update_time = a.value();
		
		
		assertNotNull("update time attribute is missing",new_update_time);
		
		assertFalse(update_time.equals(new_update_time));
	}
	@Test
	public void codelistCanChangeName() {
		
		Code c1 = code().name("c").build();
		
		Codelist list = codelist().name("cl").with(c1).version("2.0").build();
		
		Code c2 = code().name("c1").build();
		
		Codelist changeset = modifyCodelist(list.id()).with(c2).build();
		
		update(list,changeset);
		
		assertEquals("c",list.codes().iterator().next().name().getLocalPart());
		
	}
	
	
	//helpers
	@SuppressWarnings("all")
	<T> void update(T o, T delta) {

		reveal(o, Identified.Abstract.class).update(reveal(delta, Identified.Abstract.class));
	}
}
