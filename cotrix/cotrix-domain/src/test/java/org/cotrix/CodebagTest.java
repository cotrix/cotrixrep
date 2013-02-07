package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codes.Codebag;
import org.cotrix.domain.codes.Codelist;
import org.cotrix.domain.common.Bag;
import org.cotrix.domain.common.Group;
import org.cotrix.domain.versions.SimpleVersion;
import org.junit.Test;

public class CodebagTest {

	@Test
	public void codebagsAreCorrectlyConstructed() {
			
		Codebag bag = new Codebag(name); 
		
		assertEquals(0,bag.lists().size());
		assertEquals(0,bag.attributes().size());
		
		Group<Codelist> lists = new Group<Codelist>();
		lists.add(new Codelist(name));
		
		bag = new Codebag(name,lists);
		assertEquals(lists,bag.lists());
		
		Bag<Attribute> attributes = new Bag<Attribute>();
		attributes.add(new Attribute(name, value));
		
		bag = new Codebag(name,lists,attributes);
		assertEquals(attributes,bag.attributes());
	}
	
	@Test
	public void codeBagsAreCorrectlyVersioned() {
			
		Codebag bag = new Codebag(name); 
		
		Codebag versioned = bag.copy("1");
		
		assertEquals("1",versioned.version());
		
		bag = new Codebag(name,new Group<Codelist>(),new Bag<Attribute>(),new SimpleVersion("2.0"));
		
		assertEquals("2.0",bag.version());
		
		try {
			bag.copy("1.3");
			fail();
		}
		catch(IllegalStateException e){}
		
	}
	@Test
	public void codebagsAreCorrectlyCloned() {
			
		Codebag bag = samplebag();

		Codebag clone  = bag.copy();
		
		assertEquals(bag,clone);
		
	}
	
	private Codebag samplebag() {
		
		Group<Codelist> lists = new Group<Codelist>();
		lists.add(new Codelist(name));
		
		Bag<Attribute> attributes = new Bag<Attribute>();
		attributes.add(new Attribute(name, value));
		
		return new Codebag(name,lists,attributes);
	}

}
