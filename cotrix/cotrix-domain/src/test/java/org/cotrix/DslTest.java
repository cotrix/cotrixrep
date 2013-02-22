package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.common.Delta.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.LanguageAttribute;
import org.cotrix.domain.utils.Constants;
import org.junit.Test;

public class DslTest {

	public void attributeSentences() {
		
		a().with(name).and("v").build();
		a().with(name).and("v").ofType(name).build();
		a().with(name).and("v").ofType(name).in(language).build();
		a().with(name).and("v").in(language).build();
		
		a("1").with(name).and("v").as(NEW).build();
	}
	
	@Test
	public void buildAttribute() {
		
		Attribute a = a().with(name).and(value).build();

		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(Constants.DEFAULT_TYPE,a.type());
		
		a = a("id").with(name).and(value).ofType(type).build();
		
		assertEquals("id",a.id());
		assertEquals(type,a.type());
		
	}
	
	@Test
	public void buildLanguageAttribute() {
			
		Attribute a = a("id").with(name).and(value).in(language).build();
		
		LanguageAttribute langattr = (LanguageAttribute) a;
		
		assertEquals(language,langattr.language());
		
	}
	
	public void codeSentences() {
		
		code().with(name).build();
		code("id").with(name).build();
		code().with(name).and(a).build();
	
	}
	
	@Test
	public void buildCode() {
		
		Code code = code().with(name).build();
		
		assertEquals(name,code.name());
		assertEquals(0,code.attributes().size());
		
		assertEquals(code,ascode(name));
		
		
		////
		
		code = code("12").with(name).and(a).build();
		
		assertEquals("12",code.id());
		assertEquals(1,code.attributes().size());
		assertTrue(code.attributes().contains(a));
		
	}
	
	public void codelistSentences() {
		
		codelist().with(name).build();
		codelist("id").with(name).build();
		
		codelist().with(name).version(v).build();
		codelist().with(name).and(a).build();
		codelist().with(name).and(a).version(v).build();
		
		codelist().with(name).with(c).build();
		codelist().with(name).with(c).version(v).build();
		codelist().with(name).with(c).and(a).build();
		codelist().with(name).with(c).and(a).version(v).build();
		
	}
	
	@Test
	public void buildCodelist() {
		
		Codelist list = codelist().with(name).build();
		
		assertEquals(name,list.name());
		
		list=codelist("1").with(name).with(c).and(a).build();
		
		assertEquals("1",list.id());
		assertTrue(list.attributes().contains(a));
		assertTrue(list.codes().contains(c));
		
		list=codelist("1").with(name).and(a).build();
		
		list=codelist("1").with(name).and(a).version("1.0").build();
		
		assertEquals("1.0",list.version());
		
	}
	
	public void codebagSentences() {
		
		codebag().with(name).build();
		codebag("id").with(name).build();
		
		codebag().with(name).version(v).build();
		codebag().with(name).and(a).build();
		codebag().with(name).and(a).version(v).build();
		
		codebag().with(name).with(cl).build();
		codebag().with(name).with(cl).version(v).build();
		codebag().with(name).with(cl).and(a).build();
		codebag().with(name).with(cl).and(a).version(v).build();
	}
	
	@Test
	public void buildCodebag() {
		
		Codebag bag = codebag().with(name).build();
		
		assertEquals(name,bag.name());
		
		bag = codebag("1").with(name).and(a).build();
		
		assertEquals("1",bag.id());
		assertTrue(bag.attributes().contains(a));
		
		bag=codebag("1").with(name).and(a).version("1.0").build();
		
		assertEquals("1.0",bag.version());
		

	}

}
