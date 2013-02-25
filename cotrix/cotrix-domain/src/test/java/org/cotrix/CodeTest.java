package org.cotrix;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.traits.Change.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.pos.CodePO;
import org.cotrix.domain.simple.SimpleCode;
import org.cotrix.domain.simple.SimpleFactory;
import org.cotrix.domain.spi.Factory;
import org.junit.Test;

public class CodeTest {

	//NOTE: we test DOs independently from DSL and use utility methods to construct POs and make our tests less verbose
	//NOTE: we assume tested dependencies, hence we use pre-prepared ones in fixture
	
	static Factory factory = new SimpleFactory();
	
	@Test
	public void codesAreConstructed() {
			
		CodePO po = po("id",name,attributes(a,a2));
		SimpleCode code = new SimpleCode(po); 
		
		assertEquals("id",code.id());
		assertEquals(attributes(a,a2),code.attributes());
		assertNull(code.change());
		
	}
	
	@Test
	public void codesAreConstructedWithAttributes() {
			
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		attributes.add(a);
		
		BaseBag<Attribute> bag = new BaseBag<Attribute>(attributes);
		
		SimpleCode code = new SimpleCode(po(name,bag)); 
		
		assertEquals(bag, code.attributes());
		
	}
	
	@Test
	public void codesAreCloned() {
			
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		attributes.add(a);
		
		Code code = new SimpleCode(po(name,new BaseBag<Attribute>(attributes))); 
		
		Code clone  = code.copy(factory);
		
		assertEquals(code,clone);
		
	}
	
	@Test
	public void codesAreUpdated() {
		
		Attribute a = a("id").with(name).and(value).build();
		
		SimpleCode code = new SimpleCode(po(name,new BaseBag<Attribute>(asList(a)))); 
		
		String updatedValue = "updated";
		
		//builds delta attribute
		Attribute a1 = a("id").with(name).and(updatedValue).as(MODIFIED).build();
		
		//builds delta code
		CodePO  po = po(code.id(),name,new BaseBag<Attribute>(asList(a1)));
		po.setChange(MODIFIED);
		
		SimpleCode delta  = new SimpleCode(po);
		
		//update code with delta code
		code.update(delta);
		
		assertEquals(1, code.attributes().size());
		assertEquals(updatedValue,a.value());
		
		/////////add
		
		a1 = a("id2").with(name2).and(value).as(NEW).build();
		po.setAttributes(new BaseBag<Attribute>(asList(a1)));
		
		delta  = new SimpleCode(po);
		
		//update code with delta code
		code.update(delta);
		
		assertEquals(2, code.attributes().size());
		
		System.out.println(code);
		
		
	}
	

	private CodePO po(QName name, BaseBag<Attribute> attributes) {
		return po("id",name,attributes);
	}
	
	private CodePO po(String id, QName name, BaseBag<Attribute> attributes) {
		CodePO po = new CodePO(id);
		po.setName(name);
		po.setAttributes(attributes);
		return po;
	}
}
