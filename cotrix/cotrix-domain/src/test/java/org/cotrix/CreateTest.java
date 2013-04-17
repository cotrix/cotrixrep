package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.LanguageAttribute;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.po.AttributedPO;
import org.cotrix.domain.po.CodebagPO;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.po.NamedPO;
import org.cotrix.domain.po.VersionedPO;
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
	
	@Test
	public void DOsRejectNullParameters() {
		
		NamedPO po = new NamedPO(null) {};
		
		try {
			po.setName(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			po.setChange(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	//all attributed DOs: we workd directly against AttributedPO class simulating a subclass
	
	@Test
	public void attributedDOsRejectNullParameters() {
		
		AttributedPO po = new AttributedPO("id") {};
		
		try {
			po.setAttributes((List<Attribute>)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
	}
	
	/// attributes
	
	@Test
	public void attributesRejectNullParameters() {
			

		AttributePO po = new AttributePO("id");
		
		try {//null value
			po.setValue(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {//null language
			po.setLanguage(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		//type is defaulted
		assertNotNull(po.type());
			
	}

	@Test
	public void attributesCanBeFluentlyConstructed() {
		
		Attribute a = attr().name(name).value(value).build();
		
		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(Constants.DEFAULT_TYPE,a.type());
		
		a = attr("id").name(name).value(value).ofType(type).build();
		
		assertEquals("id",a.id());
		assertEquals(type,a.type());
		
		a = attr().name(name).value(value).ofType(name).in(language).build();
		
		LanguageAttribute langattr = (LanguageAttribute) a;
		
		assertEquals(language,langattr.language());

		//other sentences
		attr().name(name).value(value).in(language).build();
	}
	
	
	/// codes

	@Test
	public void codesCanBeFluentlyConstructed() {
		
		Code code = code().name(name).build();
		
		assertEquals(name,code.name());
		assertEquals(0,code.attributes().size());
		
		assertEquals(code,ascode(name));
		
		
		code = code("id").name(name).attributes(a).build();
		
		assertEquals("id",code.id());
		assertEquals(1,code.attributes().size());
		assertTrue(asList(code.attributes()).contains(a));
	}
	

	
	//versioned
	
	@Test
	public void versionedDOsRejectNullParameters() {
		
		VersionedPO po = new VersionedPO("id");
		
		try {
			po.setVersion(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}

	
	
	// codelists
	
	
	@Test
	public void codelistsRejectNullParameters() {
			

		CodelistPO po = new CodelistPO("id");
		
		try {//null value
			po.setCodes((List<Code>)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
			
	}
	
	@Test
	public void codelistCanBeFluentlyConstructed() {
		
		Codelist list  = codelist().name(name).build();
		
		assertEquals(name,list.name());
		assertEquals(0,list.codes().size());
		
		list = codelist("id").name(name).build();
		
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

	
	
	
	//code bags
	
	
	@Test
	@SuppressWarnings("all")
	public void codebagsRejectNullParameters() {
			

		CodebagPO po = new CodebagPO("id");
		
		try {//null value
			po.setLists((List)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
			
	}
	
	@Test
	public void codebagsCanBeFluentlyConstructed() {
		
		Codebag bag = codebag().name(name).build();
		
		assertEquals(name,bag.name());
		
		bag = codebag("id").name(name).build();
		
		assertEquals("id",bag.id());
		
		bag = codebag().name(name).attributes(a).build();
		
		assertTrue(asList(bag.attributes()).contains(a));
		
		bag = codebag().name(name).version(v).build();
		
		assertEquals(v,bag.version());
		
		bag = codebag().name(name).with(cl).build();
		
		assertTrue(asList(bag.lists()).contains(cl));
		
		//other correct sentences
		codebag().name(name).with(cl).version(v).build();
		codebag().name(name).with(cl).attributes(a).build();
		codebag().name(name).with(cl).attributes(a).version(v).build();
	}
	
}
