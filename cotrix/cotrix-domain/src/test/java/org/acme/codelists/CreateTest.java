package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.utils.Constants;
import org.junit.Test;

public class CreateTest {

	//we answer this questions: 
	// are DOs constructed correctly? this splits into:
		//are POs constructed in a correct state?
		//are containers constructed in a correct state?
	// can DO be constructed fluently?
		//can the DSL create the same range of DOs that can be created directly with POs
	
	//--------------------------------------------------------------------
	
	// first, all DOs: we test directly against base ObjectPO class simulating a subclass
	
	
	
	
	/// attributes
	
	@Test
	public void attributesCanBeFluentlyConstructed() {
		
		Attribute a = attribute().name(name).value(value).build();
		
		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(Constants.DEFAULT_TYPE,a.type());
		
		a = attribute().name(name).value(value).ofType(type).build();
		
		assertEquals(type,a.type());
		
		a = attribute().name(name).value(value).ofType(name).in(language).build();
		
		assertEquals(language,a.language());

		//other sentences
		attribute().name(name).value(value).in(language).build();
	}
	
	
	/// codes

	@Test
	public void codesCanBeFluentlyConstructed() {
		
		Code code = code().name(name).build();
		
		assertEquals(name,code.name());
		
		//creation time
		assertEquals(1,code.attributes().size());
		
		code = modifyCode("id").name(name).attributes(a).build();
		
		assertEquals("id",code.id());
		
		//no creation time on delta objects
		assertEquals(1,code.attributes().size());
		assertTrue(asList(code.attributes()).contains(a));
		
		System.out.println(code);
	}
	
	@Test
	public void codesAreCreatedwithATimestamp() {
		
		Code code  = code().name("name").build();
		
		for (Attribute a : code.attributes())
			if (a.name().equals(Constants.CREATION_TIME))
				return;

		fail("creation time attribute is missing");
	}
	
	// codelists
	
	
	@Test
	public void codelistsRejectNullParameters() {
			

		CodelistMS po = new CodelistMS();
		
		try {
			po.codes(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
			
	}
	
	@Test
	public void codelistCanBeFluentlyConstructed() {
		
		Codelist list  = codelist().name(name).build();
		
		assertEquals(name,list.name());
		assertEquals(0,list.codes().size());
		
		list = modifyCodelist("id").name(name).build();
		
		assertEquals("id",list.id());
		
		list= codelist().name(name).attributes(a).build();
		
		assertTrue(asList(list.attributes()).contains(a));
		
		list = codelist().name(name).with(c).build();
		
		assertTrue(asList(list.codes()).contains(c));
		
		list = codelist().name(name).version(v).build();
		
		assertEquals(v,list.version());
		
		//other correct sentences
		codelist().name(name).attributes(a).version(v).build();
		codelist().name(name).with(c).version(v).build();
		codelist().name(name).with(c).attributes(a).build();
		codelist().name(name).with(c).attributes(a).version(v).build();
		
	}

	
	
	@Test
	public void codelistsAreCreatedwithATimestamp() {
		
		Code code  = code().name("name").build();
		Codelist list = codelist().name("name").with(code).build();
		
		for (Attribute a : list.attributes())
			if (a.name().equals(Constants.CREATION_TIME))
				return;

		fail("creation time attribute is missing");
	}
	
}
