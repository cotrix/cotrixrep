package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.pos.CodebagPO;
import org.cotrix.domain.simple.SimpleCodebag;
import org.cotrix.domain.simple.SimpleFactory;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;
import org.junit.Test;

public class CodebagTest {

	static Factory factory = new SimpleFactory();
	
	@Test
	public void codebagsAreConstructed() {
			
		SimpleCodebag bag = new SimpleCodebag(po(name,codelists(),attributes(),no_version)); 
		
		assertEquals(0,bag.lists().size());
		assertEquals(0,bag.attributes().size());
		
		BaseGroup<Codelist> lists = codelists(cl);
		
		bag = new SimpleCodebag(po(name,lists,attributes(),no_version));
		assertEquals(lists,bag.lists());
		
		BaseBag<Attribute> attributes = attributes(a);
		
		bag = new SimpleCodebag(po(name,lists,attributes,no_version));
		assertEquals(attributes,bag.attributes());
	}
	
	@Test
	public void codeBagsAreCorrectlyVersioned() {
		
		//a code bag, with a default version scheme but still formally unversioned
		SimpleCodebag bag = new SimpleCodebag(po(name,codelists(),attributes(),no_version)); 
		
		//a new version of the bag
		Codebag versioned = bag.bump(factory,"1");
		
		assertEquals("1",versioned.version());
		
		//the new version is a fork
		assertFalse(bag.equals(versioned));
		
		//a bag with an initial version in a given scheme (here the same as default)
		bag = new SimpleCodebag(po(name,codelists(),attributes(),new SimpleVersion("2.0")));
		
		assertEquals("2.0",bag.version());
		
		//a failed attempt to version the bag with an unacceptable number
		try {
			bag.bump(factory,"1.3");
			fail();
		}
		catch(IllegalStateException e){}
		
	}
	
	@Test
	public void codebagsAreCorrectlyCloned() {
			
		SimpleCodebag bag = samplebag();

		Codebag clone  = bag.copy(factory);
		
		assertEquals(bag,clone);
		
	}
	
	private SimpleCodebag samplebag() {
		
		BaseGroup<Codelist> lists = codelists(cl);
		
		BaseBag<Attribute> attributes = attributes(a);
		attributes.add(a);
		
		return new SimpleCodebag(po(name,lists,attributes,no_version));
	}
	
	private CodebagPO po(QName name,BaseGroup<Codelist> codes, BaseBag<Attribute> attributes, Version version) {
		
		CodebagPO po = new CodebagPO("1");
		po.setName(name);
		po.setAttributes(attributes);
		po.setLists(codes);
		po.setVersion(version);
		
		return po;
	}

}
