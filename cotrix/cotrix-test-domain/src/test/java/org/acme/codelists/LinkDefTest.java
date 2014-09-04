package org.acme.codelists;

import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.common.Ranges.*;
import static org.cotrix.domain.common.Status.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.utils.DomainConstants.*;
import static org.cotrix.domain.values.ValueFunctions.*;
import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.acme.DomainTest;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.memory.MLinkDef;
import org.cotrix.domain.utils.AttributeTemplate;
import org.junit.Test;

public class LinkDefTest extends DomainTest {

	@Test
	public void linksCanBeFluentlyConstructed() {

		Codelist target = someCodelist();
		Attribute a = someAttribute();
		
		LinkDefinition newlink  = linkdef().name(name).target(target).attributes(a).build();
		
		assertEquals(name,newlink.qname());
		assertEquals(target,newlink.target());
		assertTrue(newlink.attributes().contains(a));

		//defaults
		assertEquals(NameLink.INSTANCE,newlink.valueType());
		assertEquals(identity,reveal(newlink).bean().function());
		assertEquals(defaultRange,reveal(newlink).bean().range());
		
		//name-based
		newlink  = linkdef().name(name).target(target).anchorToName().build();
		
		assertEquals(NameLink.INSTANCE,newlink.valueType());
		
		//attribute-based
		Attribute template = attribute().name(q("this")).ofType(q("that")).in("this").build();
		
		newlink  = linkdef().name(name).target(target).anchorTo(template).build();
		
		assertEquals(new AttributeLink(new AttributeTemplate(template)),newlink.valueType());
		
		//link-based
		LinkDefinition linktemplate = linkdef().name(name).target(target).anchorTo(template).build();
		
		newlink  = linkdef().name(name).target(target).anchorTo(linktemplate).build();
		
		assertEquals(new LinkOfLink(linktemplate),newlink.valueType());
		
		
		//value functions
		newlink  = linkdef().name(name).target(target).transformWith(lowercase).build();
		
		assertEquals(lowercase,reveal(newlink).bean().function());
		
		newlink  = linkdef().name(name).target(target).occurs(atmost(5)).build();
		
		assertEquals(atmost(5),reveal(newlink).bean().range());
	}
	
	@Test
	public void changesetCanBeFluentlyConstructed() {

		LinkDefinition link;
		
		//name change
		link = modifyLinkDef("1").name(name).build();
		
		//attribute change
		link =  modifyLinkDef("1").attributes(someAttribute()).build();
		
		//value type change
		link =  modifyLinkDef("1").anchorTo(someTemplate()).build();
		link =  modifyLinkDef("1").anchorToName().build();
		
		LinkDefinition linktemplate = linkdef().name(name).target(someCodelist()).build();
		link =  modifyLinkDef("1").anchorTo(linktemplate).build();
		
		//function change
		link =  modifyLinkDef("1").transformWith(uppercase).build();
		
		//occurrence change
		link =  modifyLinkDef("1").occurs(atmostonce).build();
		
		//full change
		link =  modifyLinkDef("1")
				.name(name)
				.attributes(someAttribute())
				.anchorTo(someTemplate())
				.transformWith(lowercase)
				.occurs(atmostonce)
				.build();
		
		//changed
		assertEquals(MODIFIED,((LinkDefinition.Private) link).status());

	}
	
	@Test
	public void linksCanBeCloned() {
		
		LinkDefinition fullLinkWithoutDefaults = like(linkdef()
							.name(name)
							.target(someCodelist())
							.anchorTo(someTemplate())
							.transformWith(lowercase)
							.occurs(once)
							.attributes(someAttribute()).build());
		
		LinkDefinition.Bean state = reveal(fullLinkWithoutDefaults).bean();
		
		MLinkDef clone = new MLinkDef(state);

		assertEquals(state.qname(),clone.qname());
		assertEquals(state.target().id(),clone.target().id());
		assertEquals(state.valueType(),clone.valueType());
		assertEquals(state.function(),clone.function());
		assertEquals(state.range(),clone.range());
		
		for (Attribute.Bean attr : clone.attributes()) {
			//System.out.println(attr.name());
			assertTrue(clone.attributes().contains(attr.qname()));
		}
		
		assertFalse(clone.id().equals(state.id()));
		
		//can persist a link
		like(new LinkDefinition.Private(clone));
		
	}
	
	@Test
	public void linksCanChangeName() {
		
		LinkDefinition link = like(linkdef().name(name).target(someCodelist()).build());
		
		LinkDefinition changeset = modifyLinkDef(link.id()).name(name2).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(name2,link.qname());
		
	}
	
	@Test
	public void linksCanChangeValueType() {
		
		LinkDefinition link = like(linkdef().name(name).target(someCodelist()).build());
		
		Attribute template = someTemplate();
		
		LinkDefinition changeset = modifyLinkDef(link.id()).anchorTo(template).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(new AttributeLink(new AttributeTemplate(template)),link.valueType());
		
	}
	
	@Test
	public void linksCanChangeValueTypeBackToDefault() {
		
		LinkDefinition linktemplate = like(linkdef().name(name).target(someCodelist()).build());
		
		LinkDefinition link = like(linkdef().name(name).target(someCodelist()).anchorTo(linktemplate).build());
		
		LinkDefinition changeset = modifyLinkDef(link.id()).anchorToName().build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(NameLink.INSTANCE,link.valueType());
		
	}
	
	@Test
	public void linksCanChangeRange() {
		
		LinkDefinition link = like(linkdef().name(name).target(someCodelist()).build());
		
		LinkDefinition changeset = modifyLinkDef(link.id()).occurs(once).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(once,link.range());
		
	}
	
	@Test
	public void linksCanChangeFunction() {
		
		LinkDefinition link = like(linkdef().name(name).target(someCodelist()).build());
		
		LinkDefinition changeset = modifyLinkDef(link.id()).transformWith(custom("'Hello $value'")).build();
		
		reveal(link).update(reveal(changeset));
		
		assertEquals(custom("'Hello $value'"),link.function());
		
	}
	
	
	
	//helpers

	private Codelist someCodelist(QName ... n) {
		
		return like(codelist().name(n.length>0?n[0]:name).build());
	}
	
	private Attribute someAttribute(QName ... n) {
		
		return like(attribute().name(n.length>0?n[0]:name).ofType(q("that")).in("this").build());
	}
	
	private Attribute someTemplate(QName ... n) {
		
		return attribute().name(n.length>0?n[0]:name).ofType(q("that")).in("this").build();
	}


}
