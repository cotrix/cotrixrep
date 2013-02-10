package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codes.Code;
import org.cotrix.domain.codes.Codelist;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.containers.Group;
import org.cotrix.domain.versions.SimpleVersion;
import org.junit.Test;

public class CodelistTest {

	@Test
	public void codelistsAreCorrectlyConstructed() {
			
		Codelist list = new Codelist(name); 
		
		assertEquals(0,list.codes().size());
		assertEquals(0,list.attributes().size());
		assertNotNull(list.version());
		
		Group<Code> codes = new Group<Code>();
		codes.add(new Code(name));
		
		list = new Codelist(name,codes);
		assertEquals(codes,list.codes());
		
		Bag<Attribute> attributes = new Bag<Attribute>();
		attributes.add(new Attribute(name, value));
		
		list = new Codelist(name,codes,attributes);
		assertEquals(attributes,list.attributes());
	}
	
	@Test
	public void codelistsAreCorrectlyVersioned() {
			
		//a code list, with a default version scheme but still formally unversioned
		Codelist list = new Codelist(name); 
		
		//a new version of the list
		Codelist list1 = list.copyWithVersion("1");
		
		
		assertEquals("1",list1.version());
		
		//the new version is a fork
		assertFalse(list.equals(list1));
		
		//a list with an initial version in a given scheme (here the same as default)
		list = new Codelist(name,new Group<Code>(),new Bag<Attribute>(),new SimpleVersion("2.0"));
		
		
		assertEquals("2.0",list.version());
		
		//a failed attempt to version the list with an unacceptable number
		try {
			list.copyWithVersion("1.3");
			fail();
		}
		catch(IllegalStateException e){}
		
	}
	
	
	
	@Test
	public void codelistsAreCorrectlyCloned() {
			
		Codelist list = samplelist();

		Codelist clone  = list.copy();
		
		assertEquals(list,clone);
		
	}
	
	private Codelist samplelist() {
		
		Group<Code> codes = new Group<Code>();
		codes.add(new Code(name));
		
		Bag<Attribute> attributes = new Bag<Attribute>();
		attributes.add(new Attribute(name, value));
		
		return new Codelist(name,codes,attributes);
	}

}
