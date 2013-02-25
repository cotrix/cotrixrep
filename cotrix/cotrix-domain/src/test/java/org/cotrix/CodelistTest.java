package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.pos.CodelistPO;
import org.cotrix.domain.simple.SimpleCodelist;
import org.cotrix.domain.simple.SimpleFactory;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;
import org.junit.Test;

public class CodelistTest {
	
	static final Factory factory = new SimpleFactory();

	@Test
	public void codelistsAreCorrectlyConstructed() {
			
		SimpleCodelist list = new SimpleCodelist(po(name,codes(),attributes(),no_version)); 
		
		assertEquals(0,list.codes().size());
		assertEquals(0,list.attributes().size());
		assertNotNull(list.version());
		
		BaseGroup<Code> codes = codes(c);
		
		list = new SimpleCodelist(po(name,codes,attributes(),no_version));
		assertEquals(codes,list.codes());
		
		BaseBag<Attribute> attributes = attributes(a);
		
		list = new SimpleCodelist(po(name,codes,attributes,no_version));
		assertEquals(attributes,list.attributes());
	}
	
	@Test
	public void codelistsAreCorrectlyVersioned() {
			
		//a code list, with a default version scheme but still formally unversioned
		Codelist list = new SimpleCodelist(po(name,codes(),attributes(),no_version)); 
		
		//a new version of the list
		Codelist list1 = list.bump(factory,"1");
		
		
		assertEquals("1",list1.version());
		
		//the new version is a fork
		assertFalse(list.equals(list1));
		
		//a list with an initial version in a given scheme (here the same as default)
		list = new SimpleCodelist(po(name,codes(),attributes(),new SimpleVersion("2.0")));
		
		
		assertEquals("2.0",list.version());
		
		//a failed attempt to version the list with an unacceptable number
		try {
			list.bump(factory,"1.3");
			fail();
		}
		catch(IllegalStateException e){}
		
	}
	
	
	
	@Test
	public void codelistsAreCorrectlyCloned() {
			
		SimpleCodelist list = samplelist();

		Codelist clone  = list.copy(factory);
		
		assertEquals(list,clone);
		
	}
	
	
	
	private SimpleCodelist samplelist() {
		
		BaseGroup<Code> codes = codes(c);
		
		BaseBag<Attribute> attributes = attributes(a);
		
		return new SimpleCodelist(po(name,codes,attributes,no_version));
	}
	
	
	
	

	private CodelistPO po(QName name,BaseGroup<Code> codes, BaseBag<Attribute> attributes, Version version) {
		
		CodelistPO po = new CodelistPO("id");
		po.setName(name);
		po.setAttributes(attributes);
		po.setCodes(codes);
		po.setVersion(version);
		
		return po;
	}
}
