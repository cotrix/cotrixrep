package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.LanguageAttribute;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.pos.AttributePO;
import org.cotrix.domain.pos.AttributedPO;
import org.cotrix.domain.pos.CodebagPO;
import org.cotrix.domain.pos.CodelistPO;
import org.cotrix.domain.pos.ObjectPO;
import org.cotrix.domain.pos.VersionedPO;
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
		
		ObjectPO po = new ObjectPO(null) {};
		
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
			po.setAttributes(null);
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
		
		Attribute a = a().with(name).and(value).build();
		
		assertEquals(name,a.name());
		assertEquals(value,a.value());
		assertEquals(Constants.DEFAULT_TYPE,a.type());
		
		a = a("id").with(name).and(value).ofType(type).build();
		
		assertEquals("id",a.id());
		assertEquals(type,a.type());
		
		a = a().with(name).and(value).ofType(name).in(language).build();
		
		LanguageAttribute langattr = (LanguageAttribute) a;
		
		assertEquals(language,langattr.language());

		//other sentences
		a().with(name).and(value).in(language).build();
	}
	
	
	///////////// bags
	
	@Test
	public void baseBagsCanBeIncrementallyConstructed() {
			
		BaseBag<Attribute> bag = attributes();
		
		assertEquals(0,bag.size());
		assertFalse(bag.contains(name));
		assertTrue(bag.get(name).isEmpty());
		assertTrue(bag.remove(name).isEmpty());
		assertNull(bag.change());
		
		Attribute a = a().with(name).and(value).build();
		
		//add
		
		assertTrue(bag.add(a));
		
		assertTrue(bag.contains(a.name()));
		assertTrue(bag.contains(a));
		assertEquals(1,bag.size());
		
		//duplicate not permitted
		assertFalse(bag.add(a));
		
		Attribute a2 = a().with(name).and(value2).build();
		
		//objects can have same name
		assertTrue(bag.add(a2));

		assertTrue(bag.contains(a2));
		assertEquals(2,bag.size());
		
		//removed
		
		Attribute a3 = a().with(name3).and(value3).build();
		
		bag.add(a3);
		
		List<Attribute> removed = bag.remove(name);

		assertTrue(removed.contains(a));
		assertFalse(bag.contains(a));
		assertTrue(removed.contains(a2));
		assertFalse(bag.contains(a2));
		
		assertFalse(removed.contains(a3));
		assertTrue(bag.contains(a3));
		
		assertEquals(2,removed.size());
		assertEquals(1,bag.size());
	}
	
	@Test
	public void baseBagsRejectNullParameters() {
		
		BaseBag<Attribute> bag = attributes();
		
		try {
			bag.add(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			bag.remove(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			bag.contains((Attribute)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			bag.contains((QName)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			bag.contains(q(null,""));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	
	
	
	/// codes

	@Test
	public void codesCanBeFluentlyConstructed() {
		
		Code code = code().with(name).build();
		
		assertEquals(name,code.name());
		assertEquals(0,code.attributes().size());
		
		assertEquals(code,ascode(name));
		
		
		code = code("id").with(name).and(a).build();
		
		assertEquals("id",code.id());
		assertEquals(1,code.attributes().size());
		assertTrue(code.attributes().contains(a));
	}
	

	
	/// groups
	
	
	@Test
	public void baseGroupsCanBeIncrementallyConstructed() {
			
		BaseGroup<Attribute> group = group();
		
		assertEquals(0,group.size());
		assertFalse(group.contains(name));
		
		try {
			group.get(name);
			fail();
		}
		catch(IllegalStateException e) {}
		
		
		try {
			group.remove(name);
			fail();
		}
		catch(IllegalStateException e) {}
		
		assertNull(group.change());
	
		try {
			group.get(name);
			fail();
		}
		catch(IllegalStateException e) {}
		
		try {
			group.remove(name);
			fail();
		}
		catch(IllegalStateException e) {}

		
		Attribute a = a().with(name).and(value).build();
		
		boolean added = group.add(a);
		
		assertTrue(added);
		
		assertEquals(a,group.iterator().next());
		assertTrue(group.contains(name));
		assertEquals(a, group.get(name));
		
		//reject homonymous objects
		assertFalse(group.add(a));
		
		//remove
		
		Attribute removed = group.remove(name);
		
		assertEquals(a,removed);
		
		assertEquals(0,group.size());
		assertFalse(group.contains(name));
	}
	
	@Test
	public void baseGroupsRejectNullParameters() {
		
		BaseGroup<Attribute> group = group();
		
		try {
			group.add(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			group.remove(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			group.contains((Attribute)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			group.contains((QName)null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			group.contains(q(null,""));
			fail();
		}
		catch(IllegalArgumentException e) {}
		

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
			po.setCodes(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
			
	}
	
	@Test
	public void codelistCanBeFluentlyConstructed() {
		
		Codelist list  = codelist().with(name).build();
		
		assertEquals(name,list.name());
		assertEquals(0,list.codes().size());
		
		list = codelist("id").with(name).build();
		
		assertEquals("id",list.id());
		
		list= codelist().with(name).and(a).build();
		
		assertTrue(list.attributes().contains(a));
		
		list = codelist().with(name).with(c).build();
		
		assertTrue(list.codes().contains(c));
		
		list = codelist().with(name).version(v).build();
		
		assertEquals(v,list.version());
		
		//other correct sentences
		codelist().with(name).and(a).version(v).build();
		codelist().with(name).with(c).version(v).build();
		codelist().with(name).with(c).and(a).build();
		codelist().with(name).with(c).and(a).version(v).build();
		
	}

	
	
	
	//code bags
	
	
	@Test
	public void codebagsRejectNullParameters() {
			

		CodebagPO po = new CodebagPO("id");
		
		try {//null value
			po.setLists(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
			
	}
	
	@Test
	public void codebagsCanBeFluentlyConstructed() {
		
		Codebag bag = codebag().with(name).build();
		
		assertEquals(name,bag.name());
		
		bag = codebag("id").with(name).build();
		
		assertEquals("id",bag.id());
		
		bag = codebag().with(name).and(a).build();
		
		assertTrue(bag.attributes().contains(a));
		
		bag = codebag().with(name).version(v).build();
		
		assertEquals(v,bag.version());
		
		bag = codebag().with(name).with(cl).build();
		
		assertTrue(bag.lists().contains(cl));
		
		//other correct sentences
		codebag().with(name).with(cl).version(v).build();
		codebag().with(name).with(cl).and(a).build();
		codebag().with(name).with(cl).and(a).version(v).build();
	}

}
