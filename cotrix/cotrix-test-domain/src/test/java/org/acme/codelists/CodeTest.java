package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Entities.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MAttrDef;
import org.cotrix.domain.memory.MCode;
import org.cotrix.domain.memory.MLinkDef;
import org.junit.Before;
import org.junit.Test;

public class CodeTest extends DomainTest {

	
	
	Attribute attr = attribute().name(name).build();
	
	AttributeDefinition def = attrdef().name(name2).build();
	Attribute a1 = attribute().instanceOf(def).value("val1").build();
	Attribute a2 = attribute().instanceOf(def).value("val2").build();
	
	Code targetcode = code().name(name2).build();
	Codelist target = codelist().name(name).with(targetcode).build();
	LinkDefinition linkdef = linkdef().name(name).target(target).build();
	Link link = link().instanceOf(linkdef).target(targetcode).build();
	
	Code code = code()
					.name(name)
					.attributes(attr,a1,a2)
					.links(link)
					.build();
	
	@Before
	public void stage() {
		
		target = like(target);
		def = like(def);
		linkdef = like(linkdef);
		code = like(code);
	}
	
	@Test
	public void codesCanBeFluentlyConstructed() {
		
		//minimal
		Code minimal = code().name(name).build();
		
		assertNotNull(minimal.attributes());
		assertNotNull(minimal.links());
		
		//full
		
		assertTrue(code.attributes().contains(attr));
		assertTrue(code.links().contains(link));
		assertTrue(code.attributes().contains(attr));
		
		assertTrue(a1.definition().isShared());
		assertFalse(attr.definition().isShared());
	}
	
	
	@Test
	public void changesetsCanBeFluentlyConstructed() {
		
		//change links
		modify(code).name(name).build();
		
		//change attributes
		modify(code).attributes(attr).build();
		
		//change links
		modify(code).links(link).build();
				
		delete(code);
	}
	
	
	@Test
	public void canBeCloned() {
		
		Code.Bean bean = beanOf(code);
		
		Map<String,Object> context = new HashMap<>();
		
		context.put(def.id(), new MAttrDef(beanOf(def)));
		context.put(linkdef.id(), new MLinkDef(beanOf(linkdef)));
		
		MCode clone = new MCode(bean,context);
		
		assertEquals(bean.qname(),clone.qname());
		assertTrue(clone.attributes().contains(attr.qname()));
		assertTrue(clone.links().contains(link.qname()));
		
		
	}
	
	@Test
	public void canBeUpdated() {
		
		//don't need to re-test containers here
		//the simplest proof that update reaches them will do
		
		Code changeset = modify(code)
							.name(name2)
							.attributes(delete(attr))
							.links(delete(link))
							.build();
		
		reveal(code).update(reveal(changeset));
		
		assertEquals(changeset.qname(),code.qname());
		
		assertFalse(code.attributes().contains(attr));
		assertFalse(code.links().contains(link));
		
	}
	
}
