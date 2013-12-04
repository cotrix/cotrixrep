package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.trait.Identified;
import org.junit.Test;

public class UpdateTest {

	// we answer the question: can DOs be correctly updated?

	// ############################################## pre-conditions

	// if it works for attributes it will work for all identified DOs

	@Test
	public void failsOnUnidentifiedObjects() {

		try {
		
			update(attr().name(name).build(), attr("1").build());
			
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void failsOnNewAndDeletedObjects() {

		Attribute a = attr("1").name(name).build();

		try {
			
			update(a, attr().name(name2).build());
			
			fail();
		
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		try {
			
			update(a, attr("1").delete());
			
			fail();
			
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void failsWithDifferentIds() {

		try {
		
			update(attr("1").name(name).build(), attr("2").name(name2).build());
		
			fail();
			
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		}

	}

	// ###########################  attributes

	@Test
	public void changesAttributes() {

		Attribute a = attr("1").name(name).value(value).ofType(type).in(language).build();

		update(a, attr("1").name(name2).value(value2).ofType(type2).in(language2).build());

		assertEquals(name2, a.name());
		assertEquals(value2, a.value());
		assertEquals(type2, a.type());
		assertEquals(language2, a.language());
	}
	
	@Test
	public void changesAttributesPartially() {

		Attribute a = attr("1").name(name).value(value).ofType(type).in(language).build();

		update(a, attr("1").build()); //let's test by changing nothing

		assertEquals(name, a.name());
		assertEquals(value, a.value());
		assertEquals(type, a.type());
		assertEquals(language, a.language());
	}

	@Test
	public void cannotErasetNameOfAttributes() {

		Attribute a = attr("1").name(name).build();

		try {
			update(a, attr("1").name(NULL_QNAME).build());
			fail();
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	public void erasesAttributeValueTypeOrLanguage() {

		Attribute a = attr("1").name(name).value(value).ofType(type).in(language).build();

		update(a, attr("1").value(NULL_STRING).ofType(NULL_QNAME).in(NULL_STRING).build());
		
		assertNull(a.value());
		assertNull(a.type());
		assertNull(a.language());
	}
	
	
	//####################################################### codes

	
	@Test
	public void cannotEraseNameOfCodes() {
		
		try {
			
			update(code("1").name(name).build(),code("1").name(NULL_QNAME).build());
			
			fail();
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Test
	public void changesNameOfCodes() {

		Code code = code("1").name(name).build();
		
		update(code,code("1").name(name2).build());
		
		assertEquals(name2,code.name());
		
	}
	
	
	@Test
	public void changesCodeAttributes() {

		Attribute a1 = attr("1").name(name).build();
		Attribute a2 = attr("2").name(name2).build();
		Attribute a3 = attr("3").name(name3).build();
		
		Code code = code("1").name(name).attributes(a1,a2,a3).build();
		
		
		QName newname = q("changed");
		// a change
		Attribute modified = attr("1").name(newname).build();

		// a deletion
		Attribute deleted = attr("2").delete();

		// an addition
		Attribute a4 = attr().name("new").build();

		Code change = code("1").attributes(modified, deleted, a4).build();
		

		update(code,change);

		
		List<Attribute> attributes = elements(code.attributes());
		
		System.out.println(attributes);
		assertEquals(4, attributes.size());
		
		assertEquals(newname, attributes.get(0).name());
		
		assertFalse(attributes.contains(a2));
		
		assertTrue(attributes.contains(a3));
		
		assertTrue(attributes.contains(a4));

	}
	
	@Test
	public void codesAcquireAndChangeUpdateTimeStamp() throws Exception {
		
		Attribute a1 = attr("1").name(name).build();

		Code code = code("1").name(name).attributes(a1).build();
		
		// a change
		Attribute modified = attr("1").name("newname").build();
		
		Code change = code("1").attributes(modified).build();

		update(code,change);
		
		System.out.println(code);
		
		String update_time = null;
		
		for (Attribute a : code.attributes())
			if (a.name().equals(UPDATE_TIME))
				update_time = a.value();

		assertNotNull("update time attribute is missing",update_time);
		
		//let at least one second pass
		Thread.sleep(1000);
		
		modified = attr("1").name("yetanother").build();

		change = code("1").attributes(modified).build();

		update(code,change);
		
		System.out.println(code);
		String new_update_time = null;
		
		for (Attribute a : code.attributes())
			if (a.name().equals(UPDATE_TIME))
				new_update_time = a.value();
		
		
		assertNotNull("update time attribute is missing",new_update_time);
		
		assertFalse(update_time.equals(new_update_time));
	}
	@Test
	public void codelistCanChangeName() {
		
		Codelist list = codelist("1").with(code("2").name("c").build()).version("2.0").build();
		
		Codelist changeset = codelist("1").with(code("2").name("c1").build()).build();
		
		update(list,changeset);
		
		assertEquals("c1",list.codes().iterator().next().name().getLocalPart());
		
	}
	
	
	//helpers
	
	private <T> List<T> elements(Container<? extends T> container) {
		List<T> elements = new ArrayList<T>();
		for (T t : container)
			elements.add(t);
		return elements;
	}

	@SuppressWarnings("all")
	<T> void update(T o, T delta) {

		reveal(o, Identified.Abstract.class).update(reveal(delta, Identified.Abstract.class));
	}
}
