package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.memory.IdentifiedMS;
import org.junit.Test;

public class ChangeSetTest {

	@Test
	@SuppressWarnings("all")
	public void mustHaveIdentifiers() {

		IdentifiedMS po = new IdentifiedMS(null) {};

		try {
			po.status(MODIFIED);
			fail();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}
	
	@Test
	public void canBeFluentlyConstructedForAttributes() {

		Attribute a;
		
		//new attributes
		 a =  attr().name(name).build();
		 a =  attr().name(name).value(value).build();
		 a =  attr().name(name).value(value).ofType("type").build();
		 a =  attr().name(name).value(value).ofType("type").in("en").build();
		 a =  attr().name(name).value(value).in("en").build();
		 
		 
		assertFalse(((Attribute.Private) a).isChangeset());
		assertNull(((Attribute.Private) a).status());
			
		//added attributes
		 a = attr().name(name).value(value).build();
		 a = attr().name(name).value(value).ofType("type").build();
		 a = attr().name(name).value(value).ofType("type").in("en").build();
		 a = attr().name(name).value(value).in("en").build();
						  		
		
		//modified attributes
		 a = attr("1").name(name).build();
		 a = attr("1").name(name).value(value).build();
		 a = attr("1").ofType("type").build();
		 a = attr("1").ofType("type").in("en").build();
		 a = attr("1").in("en").build();

		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(MODIFIED, ((Attribute.Private) a).status());		
		
		
		//removed attributes
		a = attr("1").delete();

		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(DELETED, ((Attribute.Private) a).status());		
	}
	

	// codes: largely tested as attriuted DOs

	@Test
	public void canBeFluentlyConstructedForCodes() {
		
		
		Attribute a = attr().name("name").build();
		Attribute added = attr().name("name").build();
		Attribute modified = attr("1").name("name").build();
		Attribute deleted = attr("1").delete();
		
		Code c;

		//new codes
		c = code().name(name).build();
		c = code().name(name).attributes(a).build();

		assertEquals(null,((Code.Private) c).status());
		
		
		//added codes
		 c = code("1").name(name).build();
		 c = code("1").name(name).attributes(added).build();
						 
		
		c = code("1").attributes(modified,added,deleted).build();
		
		//changed
		assertEquals(MODIFIED,((Code.Private) c).status());
		
		c = code("1").delete();

		assertEquals(DELETED,((Code.Private) c).status());
	}
	
	
	// codelists

	@Test
	public void canBeFluentlyConstructedForCodelists() {

		Attribute a = attr().name("name").build();
		Attribute added = attr().name("name").build();
		Attribute modified = attr("1").name("name").build();
		Attribute deleted = attr("1").delete();
		
		Code c = code().name("name").attributes(a).build();
		Code addedCode = code("1").name("name").attributes(added).build();
		Code modifiedCode = code("1").name("name").attributes(added, modified, deleted).build();
		Code deletedCode = code("1").delete();

		Codelist l;
		
		//new codelists
		l = codelist().name(name).build();
		l = codelist().name(name).with(c).build();
		l = codelist().name(name).attributes(a).build();
		l = codelist().name(name).with(c).attributes(a).build();

		assertEquals(null,((Codelist.Private) l).status());
		
		//added codes
		l = codelist("1").name(name).build(); //plus all cases above

		
		l = codelist("1").attributes(modified,added,deleted).build();
		l = codelist("1").with(modifiedCode,addedCode,deletedCode).build();
		l = codelist("1").with(modifiedCode,addedCode,deletedCode).attributes(modified,added,deleted).build();
		
		//changed
		assertEquals(MODIFIED,((Codelist.Private) l).status());
		
		l = codelist("1").delete();

		assertEquals(DELETED,((Codelist.Private) l).status());

	}
}
