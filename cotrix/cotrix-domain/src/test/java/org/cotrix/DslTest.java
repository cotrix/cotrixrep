package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.LanguageAttribute;
import org.cotrix.domain.codes.Code;
import org.cotrix.domain.codes.Codebag;
import org.cotrix.domain.codes.Codelist;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.versions.SimpleVersion;
import org.junit.Test;

public class DslTest {

	@Test
	public void buildAttribute() {
			
		Attribute attribute = new Attribute(name,value);
		
		Attribute built = a(name, value);
		
		assertEquals(attribute,built);
		
	}
	
	@Test
	public void buildLanguageAttribute() {
			
		LanguageAttribute attribute = new LanguageAttribute(name,value,language);
		
		LanguageAttribute built = a(name, value,language);
		
		assertEquals(attribute,built);
		
	}
	
	
	@Test
	public void buildNewCode() {
		
		Code code = new Code(name);
		
		Code built = ascode(name);
		
		assertEquals(code,built);
		
		Bag<Attribute> attributes = new Bag<Attribute>();
		attributes.add(new Attribute(name,value));
		attributes.add(new Attribute(name2,value));
		code = new Code(name,attributes);
		
		built = code(name).with(a(name,value),a(name2,value)).build();
		
		assertEquals(code,built);
		
	}
	
	@Test
	public void buildCodeFromExistingCode() {
			
		Bag<Attribute> attributes = new Bag<Attribute>();
		attributes.add(new Attribute(name,value));
		attributes.add(new Attribute(name2,value));
		Code code = new Code(name,attributes);
		
		Code built = code(code).with(a(name3,value)).build();
		
		code.attributes().add(new Attribute(name3,value));
		
		assertEquals(code,built);
		
	}
	
	@Test
	public void buildNewCodelist() {
		
		Codelist list = new Codelist(name);
		
		Codelist built = codelist(name).build();
		
		assertEquals(list,built);
		
		list.attributes().add(new Attribute(name,value));
		list.attributes().add(new Attribute(name2,value));
		
		built = codelist(name).with(a(name,value),a(name2,value)).build();
		
		assertEquals(list,built);
		
		list.codes().add(new Code(name));
		list.codes().add(new Code(name2));
		
		built = codelist(name)
							.with(ascode(name),ascode(name2))
							.with(a(name,value),a(name2,value))
							.build();

		assertEquals(list,built);
		
		list = new Codelist(list.name(),list.codes(), list.attributes(), new SimpleVersion("2"));
		
		built = codelist(name)
				.with(ascode(name),ascode(name2))
				.with(a(name,value),a(name2,value))
				.version(new SimpleVersion("2"))
				.build();

	}
	
	@Test
	public void buildCodelistFromExistingCodelist() {
		
		Codelist list = new Codelist(name);
		list.attributes().add(new Attribute(name,value));
		list.codes().add(new Code(name));
		
		Codelist built = codelist(list).build();
		
		assertEquals(list,built);

		list.attributes().add(new Attribute(name2,value));
		list.codes().add(new Code(name2));

		built = codelist(list).with(ascode(name2)).with(a(name2,value)).build();
		
		assertEquals(list,built);
		
	}
	
	@Test
	public void buildNewCodebag() {
		
		Codebag bag = new Codebag(name);
		
		Codebag built = codebag(name).build();
		
		assertEquals(bag,built);
		
		bag.attributes().add(new Attribute(name,value));
		bag.attributes().add(new Attribute(name2,value));
		
		built = codebag(name).with(a(name,value),a(name2,value)).build();
		
		assertEquals(bag,built);
		
		bag.lists().add(new Codelist(name));
		bag.lists().add(new Codelist(name2));
		
		built = codebag(name)
							.with(codelist(name).build(),codelist(name2).build())
							.with(a(name,value),a(name2,value))
							.build();

		assertEquals(bag,built);
		
		bag = new Codebag(bag.name(),bag.lists(), bag.attributes(), new SimpleVersion("2"));
		
		built = codebag(name)
				.with(codelist(name).build(),codelist(name2).build())
				.with(a(name,value),a(name2,value))
				.version(new SimpleVersion("2"))
				.build();

	}

}
