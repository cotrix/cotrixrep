package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.traits.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.pos.AttributedPO;
import org.cotrix.domain.pos.ObjectPO;
import org.cotrix.domain.primitives.BaseBag;
import org.junit.Test;

public class CreateDeltaTest {

	// we answer this questions: are Delta DOs constructed correctly? this splits into:
		// are POs constructed in a correct delta state? here we test POs.
		// are containers constructed in a correct delta state?
	// can Delta DO be constructed fluently?
		// can the DSL create the same range of Delta DOs that can be created directly with POs? here we test builders.

	// ---------------------------------------------------------------------------------------------

	// all DOs: we test directly against base ObjectPO class simulating a subclass

	@Test
	public void DOsWithoutIdentifiersCannotRepresentChanges() {

		ObjectPO po = new ObjectPO(null) {};

		// the following is not
		try {
			po.setChange(MODIFIED);
			fail();
		} catch (IllegalArgumentException e) {
		}

		try {
			po.setChange(DELETED);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}

	@Test
	public void changeTypeCannotExplicitlyChange() {

		ObjectPO po = new ObjectPO("id") {};
		
		po.setChange(MODIFIED);

		try {
			po.setChange(DELETED);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// no need to try all combinations, check works

	}

	///////////// bags (share change-handling code with groups, so we test both here
	
	@Test
	public void deltaBagsCanOnlyTakeDeltaObjects() {

		BaseBag<Attribute> bag = attributes();

		bag.setChange(MODIFIED);

		// a non-delta object
		Attribute a = a().with(name).and(value).build();

		try {
			bag.add(a);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}
	
	//we can now test implicit change propagation, hence a range of common use cases
	//note that clients cannot set change explicitly on containers (e.g. they cannot be explicitly deleted)
	//we do it here to simulate previous effects of propagation

	@Test
	public void bagsWithNewObjectsBecomeNew() {

		BaseBag<Attribute> bag = attributes();

		Attribute a = a().with(name).and(value).as(NEW).build();

		bag.add(a);

		assertTrue(bag.change()==NEW);
	}

	@Test
	public void bagsWithModifiedObjectsBecomeModified() {

		BaseBag<Attribute> bag = attributes();

		Attribute a = a("1").with(name).and(value).as(MODIFIED).build();

		bag.add(a);

		assertTrue(bag.change()==MODIFIED);
	}

	@Test
	public void bagsWithDeletedObjectsBecomeModified() {

		BaseBag<Attribute> bag = attributes();

		Attribute a = a("1").with(name).and(value).as(DELETED).build();

		bag.add(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void newBagsWithNewObjectsRemainNew() {

		BaseBag<Attribute> bag = attributes();
		bag.setChange(NEW);
		
		Attribute a = a("1").with(name).and(value).as(NEW).build();

		bag.add(a);

		assertTrue(bag.change()==NEW);
	}
	
	@Test
	public void newBagsWithModifiedObjectsBecomeModified() {

		BaseBag<Attribute> bag = attributes();
		bag.setChange(NEW);
		
		Attribute a = a("1").with(name).and(value).as(MODIFIED).build();

		bag.add(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void newBagsWithDeletedObjectsBecomeModified() {

		BaseBag<Attribute> bag = attributes();
		bag.setChange(NEW);
		
		Attribute a = a("1").with(name).and(value).as(DELETED).build();

		bag.add(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void modifiedBagsWithNewObjectsRemainModified() {

		BaseBag<Attribute> bag = attributes();
		bag.setChange(MODIFIED);
		
		Attribute a = a("1").with(name).and(value).as(NEW).build();

		bag.add(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void modifiedBagsWithDeletedObjectsRemainModified() {

		BaseBag<Attribute> bag = attributes();
		bag.setChange(MODIFIED);
		
		Attribute a = a("1").with(name).and(value).as(DELETED).build();

		bag.add(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	//complex DOs behave like bags with respect to change propagation, but code cannot be reused, so needs checking
	//we can use attributed DOs base class for these tests

	@Test
	public void deltaDOsCanOnlyTakeDeltaParameters() {

		AttributedPO po = new AttributedPO("id") {};

		po.setChange(MODIFIED);

		// a non-delta parameter
		BaseBag<Attribute> attributes = attributes();
		
		attributes.add(a().with(name).and(value).build());

		try {
			po.setAttributes(attributes);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}

	@Test
	public void DOsWithModifiedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};

		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(MODIFIED);

		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}

	@Test
	public void DOsWithDeletedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};

		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(DELETED);

		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}

	@Test
	public void DOsWithNewParametersBecomeNew() {

		// container cannot

		AttributedPO po = new AttributedPO(null) {
		};

		// a NEW parameter
		Attribute deltaAttribute = a().with(name).and(value).as(NEW).build();

		BaseBag<Attribute> deltabag = attributes(deltaAttribute);

		po.setAttributes(deltabag);

		assertTrue(po.change() == NEW);

	}

	//for convenience of testing, we can simulate the delta parameter propagation by setting one twice
	//we can use the generic attributed PO to test the various use cases
	
	@Test
	public void newDOsWithNewParametersRemainNew() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(NEW);
		
		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(NEW);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == NEW);

	}
	
	
	@Test
	public void newDOsWithModifiedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(NEW);
		
		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(MODIFIED);
		 
		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}
	
	@Test
	public void modifiedDOsWithModifiedParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);
		
		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(MODIFIED);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	
	@Test
	public void modifiedDOsWithDeletedParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);

		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(DELETED);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	@Test
	public void modifiedDOsWithNewParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);

		BaseBag<Attribute> deltabag = attributes();
		deltabag.setChange(NEW);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
		
	// / attributes: largely tested as generic DOs

	@Test
	public void deltaAttributesCanBeFluentlyConstructed() {

		Attribute a = a().with(name).and(value).as(NEW).build();

		assertTrue(a.isDelta());
		assertEquals(NEW, a.change());

	}

	// codes: largely tested as attriuted DOs

	@Test
	public void deltaCodesCanBeFluentlyConstructed() {

		Code code = code().with(name).as(NEW).build();

		assertTrue(code.isDelta());
		assertTrue(code.change() == NEW);

	}

	// codelists

	@Test
	public void deltaCodelistsCanBeFluentlyConstructed() {

		Codelist list = codelist().with(name).as(NEW).build();

		assertTrue(list.isDelta());
		assertEquals(NEW, list.change());

	}

	// code bags

	@Test
	public void deltaCodebagsCanBeFluentlyConstructed() {

		Codebag bag = codebag().with(name).as(NEW).build();

		assertTrue(bag.isDelta());
		assertEquals(NEW, bag.change());

	}

}
