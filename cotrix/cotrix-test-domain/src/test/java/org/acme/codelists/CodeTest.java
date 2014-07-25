package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.DomainUtils.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.memory.AttrDefinitionMS;
import org.cotrix.domain.memory.CodeMS;
import org.cotrix.domain.memory.LinkDefinitionMS;
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
	Codelink link = link().instanceOf(linkdef).target(targetcode).build();
	
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
		
		Code.State state = reveal(code).state();
		
		Map<String,Object> context = new HashMap<>();
		
		context.put(def.id(), new AttrDefinitionMS(stateof(def)));
		context.put(linkdef.id(), new LinkDefinitionMS(stateof(linkdef)));
		
		CodeMS clone = new CodeMS(state,context);
		
		assertEquals(state.qname(),clone.qname());
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
		
		assertFalse(code.attributes().contains(attr.qname()));
		assertFalse(code.links().contains(link.qname()));
		
	}
	
}
