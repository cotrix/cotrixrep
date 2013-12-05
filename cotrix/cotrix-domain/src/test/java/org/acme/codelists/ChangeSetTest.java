package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.junit.Test;

public class ChangeSetTest {

	@Test
	public void canBeFluentlyConstructedForAttributes() {

		Attribute a;
		
		//new attributes
		 a =  attribute().name(name).build();
		 a =  attribute().name(name).value(value).build();
		 a =  attribute().name(name).value(value).ofType("type").build();
		 a =  attribute().name(name).value(value).ofType("type").in("en").build();
		 a =  attribute().name(name).value(value).in("en").build();
		 
		 
		assertFalse(((Attribute.Private) a).isChangeset());
		assertNull(((Attribute.Private) a).status());
			
		//added attributes
		 a = attribute().name(name).value(value).build();
		 a = attribute().name(name).value(value).ofType("type").build();
		 a = attribute().name(name).value(value).ofType("type").in("en").build();
		 a = attribute().name(name).value(value).in("en").build();
						  		
		
		//modified attributes
		 a = modifyAttribute("1").name(name).build();
		 a = modifyAttribute("1").name(name).value(value).build();
		 a = modifyAttribute("1").ofType("type").build();
		 a = modifyAttribute("1").ofType("type").in("en").build();
		 a = modifyAttribute("1").in("en").build();

		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(MODIFIED, ((Attribute.Private) a).status());		
		
		
		//removed attributes
		a = deleteAttribute("1");

		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(DELETED, ((Attribute.Private) a).status());		
	}
	

	// codes: largely tested as attriuted DOs

	@Test
	public void canBeFluentlyConstructedForCodes() {
		
		
		Attribute a = attribute().name("name").build();
		Attribute added = attribute().name("name").build();
		Attribute modified = modifyAttribute("1").name("name").build();
		Attribute deleted = deleteAttribute("1");
		
		Code c;

		//new codes
		c = code().name(name).build();
		c = code().name(name).attributes(a).build();

		assertEquals(null,((Code.Private) c).status());
		
		
		//added codes
		 c = modifyCode("1").name(name).build();
		 c = modifyCode("1").name(name).attributes(added).build();
						 
		
		c = modifyCode("1").attributes(modified,added,deleted).build();
		
		//changed
		assertEquals(MODIFIED,((Code.Private) c).status());
		
		c = deleteCode("1");

		assertEquals(DELETED,((Code.Private) c).status());
	}
	
	
	// codelists

	@Test
	public void canBeFluentlyConstructedForCodelists() {

		Attribute a = attribute().name("name").build();
		Attribute added = attribute().name("name").build();
		Attribute modified = modifyAttribute("1").name("name").build();
		Attribute deleted = deleteAttribute("1");
		
		Code c = code().name("name").attributes(a).build();
		Code addedCode = modifyCode("1").name("name").attributes(added).build();
		Code modifiedCode = modifyCode("1").name("name").attributes(added, modified, deleted).build();
		Code deletedCode = deleteCode("1");

		Codelist l;
		
		//new codelists
		l = codelist().name(name).build();
		l = codelist().name(name).with(c).build();
		l = codelist().name(name).attributes(a).build();
		l = codelist().name(name).with(c).attributes(a).build();

		assertEquals(null,((Codelist.Private) l).status());
		
		//added codes
		l = modifyCodelist("1").name(name).build(); //plus all cases above

		
		l =  modifyCodelist("1").attributes(modified,added,deleted).build();
		l =  modifyCodelist("1").with(modifiedCode,addedCode,deletedCode).build();
		l =  modifyCodelist("1").with(modifiedCode,addedCode,deletedCode).attributes(modified,added,deleted).build();
		
		//changed
		assertEquals(MODIFIED,((Codelist.Private) l).status());

	}
}
