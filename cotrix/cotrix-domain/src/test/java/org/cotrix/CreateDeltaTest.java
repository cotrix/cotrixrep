package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.traits.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.pos.AttributedPO;
import org.cotrix.domain.pos.ObjectPO;
import org.junit.Test;

public class CreateDeltaTest {

	//we answer this questions: 
	// are Delta DOs constructed correctly? this splits into:
		//are POs constructed in a correct delta state?
		//are containers constructed in a correct delta state?
	//can Delta DO be constructed fluently?
	 //can the DSL create the same range of Delta DOs that can be created directly with POs
	
	//---------------------------------------------------------------------------------------------
	
	// all DOs: we test directly against base ObjectPO class simulating a subclass
	
	@Test
	public void DOsWithoutIdentifiersCannotRepresentChanges() {
	
		ObjectPO po = new ObjectPO(null) {};
		
		//that's ok
		po.setChange(NEW);
		
		//the following is not
		try {
			po.setChange(MODIFIED);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			po.setChange(DELETED);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedDOsCannotBeDeleted() {

		ObjectPO po = new ObjectPO("id") {};
		po.setChange(MODIFIED);
		po.setChange(DELETED);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedDOsCannotBeNew() {

		ObjectPO po = new ObjectPO("id") {};
		po.setChange(MODIFIED);
		po.setChange(NEW);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deletedDOsCannotBeModified() {

		ObjectPO po = new ObjectPO("id") {};
		po.setChange(DELETED);
		po.setChange(MODIFIED);
	}
	
	//all attributed DOs: we work directly against AttributedPO, the first base class that can have delta parameters
	//we test it simulating a subclass
	
	@Test
	public void deltaAttributedDOsCanOnlyTakeDeltaParameters() {
			
		AttributedPO po = new AttributedPO("id") {};

		po.setChange(MODIFIED);
		
		//a non-delta parameter
		BaseBag<Attribute> attributes = attributes(a().with(name).and(value).build()); 

		try {
			po.setAttributes(attributes);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}

	@Test
	public void attributedDOsWithModifiedParametersBecomeModified() {
		
		AttributedPO po = new AttributedPO("id") {};
		
		//a MODIFIED parameter
		Attribute deltaAttribute = a("1").with(name).and(value).as(MODIFIED).build();
		
		BaseBag<Attribute> deltabag = attributes(deltaAttribute); 
		
		
		po.setAttributes(deltabag);
		
		assertTrue(po.change()==MODIFIED);
			
	}
	
	@Test
	public void attributedDOsWithDeletedParametersBecomeModified() {
		
		AttributedPO po = new AttributedPO("id") {};
		
		//a MODIFIED parameter
		Attribute deltaAttribute = a("1").with(name).and(value).as(DELETED).build();
		
		BaseBag<Attribute> deltabag = attributes(deltaAttribute); 
		
		
		po.setAttributes(deltabag);
		
		assertTrue(po.change()==MODIFIED);
			
	}
	
	@Test
	public void attributedDOsWithNewParametersBecomeNew() {
		
		//container cannot 
		
		AttributedPO po = new AttributedPO(null) {};
		
		//a NEW parameter
		Attribute deltaAttribute = a().with(name).and(value).as(NEW).build();
		
		BaseBag<Attribute> deltabag = attributes(deltaAttribute); 
		
		
		po.setAttributes(deltabag);
		
		assertTrue(po.change()==NEW);
			
	}
	
	/// attributes: largely tested as plain DOs
	
	@Test
	public void deltaAttributesCanBeFluentlyConstructed() {
		
		Attribute a = a().with(name).and(value).as(NEW).build();
		
		assertTrue(a.isDelta());
		assertEquals(NEW,a.change());
		
	}
	
	
	///////////// bags
	
	@Test
	public void deltaBagsCanOnlyTakeDeltaObjects() {
			
		BaseBag<Attribute> bag = attributes();

		bag.setChange(MODIFIED);
		
		//a non-delta object
		Attribute a = a().with(name).and(value).build(); 

		try {
			bag.add(a);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void bagsWithNewObjectsBecomeNew() {
			
		BaseBag<Attribute> bag = attributes();
		
		Attribute a = a().with(name).and(value).as(NEW).build();
		
		bag.add(a);
		
		assertTrue(bag.isDelta());
		assertEquals(NEW,bag.change());	
	}
	
	@Test
	public void bagsWithModifiedObjectsBecomeModified() {
			
		BaseBag<Attribute> bag = attributes();
		
		Attribute a = a("1").with(name).and(value).as(MODIFIED).build();
		
		bag.add(a);
		
		assertTrue(bag.isDelta());
		assertEquals(MODIFIED,bag.change());	
	}
	
	@Test
	public void bagsWithDeletedObjectsBecomeModified() {
			
		BaseBag<Attribute> bag = attributes();
		
		Attribute a = a("1").with(name).and(value).as(DELETED).build();
		
		bag.add(a);
		
		assertTrue(bag.isDelta());
		assertEquals(MODIFIED,bag.change());	
	}

	/// codes

	@Test(expected=IllegalArgumentException.class)
	public void modifiedCodesCannotBeMarkedNew() {

		Attribute a = a("1").with(name).and(value).as(MODIFIED).build(); 
				
		code("1").with(name).and(a).as(NEW).build();
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedCodesCannotBeDeleted() {

		Attribute a = a("1").with(name).and(value).as(MODIFIED).build(); 
				
		code("1").with(name).and(a).as(DELETED).build();
	}
	
	@Test
	public void deltaCodesCanBeFluentlyConstructed() {
		
		Code code = code().with(name).as(NEW).build();
		
		assertTrue(code.isDelta());
		assertEquals(NEW,code.change());
	}
	

	
	
	/// groups
	
	
	@Test
	public void deltaGroupsCanOnlyTakeDeltaObjects() {
			
		BaseGroup<Attribute> group = group();

		group.setChange(MODIFIED);
		
		//a non-delta object
		Attribute a = a().with(name).and(value).build(); 

		try {
			group.add(a);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void groupsWithNewObjectsBecomeNew() {
			
		BaseGroup<Attribute> group = group();
		
		Attribute a = a().with(name).and(value).as(NEW).build();
		
		group.add(a);
		
		assertTrue(group.isDelta());
		assertEquals(NEW,group.change());	
	}
	
	@Test
	public void groupsWithModifiedObjectsBecomeModified() {
			
		BaseGroup<Attribute> group = group();
		
		Attribute a = a("1").with(name).and(value).as(MODIFIED).build();
		
		group.add(a);
		
		assertTrue(group.isDelta());
		assertEquals(MODIFIED,group.change());	
	}
	
	@Test
	public void groupsWithDeletedObjectsBecomeModified() {
			
		BaseGroup<Attribute> group = group();
		
		Attribute a = a("1").with(name).and(value).as(DELETED).build();
		
		group.add(a);
		
		assertTrue(group.isDelta());
		assertEquals(MODIFIED,group.change());	
	}
	
	
	
	
	// codelists
	
	@Test
	public void deltaCodelistsCanBeFluentlyConstructed() {
		
		Codelist list  = codelist().with(name).as(NEW).build();
		
		assertTrue(list.isDelta());
		assertEquals(NEW,list.change());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedCodelistsCannotBeMarkedNew() {

		Code c = code("1").with(name).as(MODIFIED).build(); 
				
		codelist("1").with(name).with(c).as(NEW).build();
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedCodelistsCannotBeDeleted() {

		Code c = code("1").with(name).as(MODIFIED).build(); 
		
		codelist("1").with(name).with(c).as(DELETED).build();
		
	}
	
	
	
	
	//code bags
	
	@Test
	public void deltaCodebagsCanBeFluentlyConstructed() {
		
		Codebag bag = codebag().with(name).as(NEW).build();
		
		assertTrue(bag.isDelta());
		assertEquals(NEW,bag.change());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedCodebagsCannotBeMarkedNew() {

		Codelist list = codelist("1").with(name).as(MODIFIED).build(); 
				
		codebag("1").with(name).with(list).as(NEW).build();
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifiedCodebagsCannotBeDeleted() {

		Codelist list = codelist("1").with(name).as(MODIFIED).build(); 
		
		codebag("1").with(name).with(list).as(DELETED).build();
		
	}

}
