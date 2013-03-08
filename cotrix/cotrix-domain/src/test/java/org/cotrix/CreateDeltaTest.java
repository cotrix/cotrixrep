package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Attribute.Private;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.po.AttributedPO;
import org.cotrix.domain.po.EntityPO;
import org.cotrix.domain.primitive.PContainer;
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

		EntityPO po = new EntityPO(null) {};

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

		EntityPO po = new EntityPO("id") {};
		
		po.setChange(MODIFIED);

		try {
			po.setChange(DELETED);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// no need to try all combinations, check works

	}

	
	@Test
	public void deltaContainersCanOnlyTakeDeltaObjects() {

		PContainer<Attribute.Private> bag = bag();

		bag.setChange(MODIFIED);

		// a non-delta object
		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).build();

		try {
			bag.update(bag(a));
			fail();
		} catch (IllegalArgumentException e) {
		}

	}
	
	//we can now test implicit change propagation, hence a range of common use cases
	//note that clients cannot set change explicitly on containers (e.g. they cannot be explicitly deleted)
	//we do it here to simulate previous effects of propagation

	@Test
	public void bagsWithNewObjectsBecomeNew() {

		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).as(NEW).build();

		PContainer<Attribute.Private> bag = bag(a);
		
		assertTrue(bag.change()==NEW);
	}

	@Test
	public void bagsWithModifiedObjectsBecomeModified() {

		Attribute.Private a = (Attribute.Private) attr("1").name(name).value(value).as(MODIFIED).build();

		PContainer<Attribute.Private> bag = bag(a);

		assertTrue(bag.change()==MODIFIED);
	}

	@Test
	public void bagsWithDeletedObjectsBecomeModified() {

		Attribute.Private a = (Attribute.Private) attr("1").name(name).value(value).as(DELETED).build();

		PContainer<Attribute.Private> bag = bag(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void newBagsWithNewObjectsRemainNew() {

		Attribute.Private a1 = (Attribute.Private) attr().name(name).value(value).as(NEW).build();
		Attribute.Private a2 = (Attribute.Private) attr().name(name2).value(value2).as(NEW).build();
		
		PContainer<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==NEW);
	}
	
	@Test
	public void newBagsWithModifiedObjectsBecomeModified() {

		Private a1 = (Private)attr().name(name).value(value).as(NEW).build();
		Private a2 = (Private)attr("1").name(name2).value(value2).as(MODIFIED).build();

		PContainer<Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void newBagsWithDeletedObjectsBecomeModified() {

		Attribute.Private a1 = (Attribute.Private)attr().name(name).value(value).as(NEW).build();
		Attribute.Private a2 = (Attribute.Private)attr("1").name(name2).value(value2).as(DELETED).build();
		
		PContainer<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void modifiedBagsWithNewObjectsRemainModified() {

		Attribute.Private a1 = (Attribute.Private)attr("1").name(name).value(value).as(MODIFIED).build();
		Attribute.Private a2 = (Attribute.Private)attr().name(name2).value(value2).as(NEW).build();
		
		PContainer<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void modifiedBagsWithDeletedObjectsRemainModified() {

		Attribute.Private a1 = (Attribute.Private)attr("1").name(name).value(value).as(MODIFIED).build();
		Attribute.Private a2 = (Attribute.Private)attr("2").name(name2).value(value2).as(DELETED).build();
		
		PContainer<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	//complex DOs behave like bags with respect to change propagation, but code cannot be reused, so needs checking
	//we can use attributed DOs base class for these tests

	@Test
	public void deltaDOsCanOnlyTakeDeltaParameters() {

		AttributedPO po = new AttributedPO("id") {};

		po.setChange(MODIFIED);

		// a non-delta parameter
		PContainer<Attribute.Private> attributes = bag();
		
		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).build();
		attributes.update(bag(a));

		try {
			po.setAttributes(attributes);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}

	@Test
	public void DOsWithModifiedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};

		PContainer<Attribute.Private> deltabag = bag();
		deltabag.setChange(MODIFIED);

		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}

	@Test
	public void DOsWithDeletedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};

		PContainer<Attribute.Private> deltabag = bag();
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
		Attribute.Private deltaAttribute = (Attribute.Private) attr().name(name).value(value).as(NEW).build();

		PContainer<Attribute.Private> deltabag = bag(deltaAttribute);

		po.setAttributes(deltabag);

		assertTrue(po.change() == NEW);

	}

	//for convenience of testing, we can simulate the delta parameter propagation by setting one twice
	//we can use the generic attributed PO to test the various use cases
	
	@Test
	public void newDOsWithNewParametersRemainNew() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(NEW);
		
		PContainer<Attribute.Private> deltabag = bag();
		deltabag.setChange(NEW);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == NEW);

	}
	
	
	@Test
	public void newDOsWithModifiedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(NEW);
		
		PContainer<Attribute.Private> deltabag = bag();
		deltabag.setChange(MODIFIED);
		 
		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}
	
	@Test
	public void modifiedDOsWithModifiedParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);
		
		PContainer<Attribute.Private> deltabag = bag();
		deltabag.setChange(MODIFIED);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	
	@Test
	public void modifiedDOsWithDeletedParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);

		PContainer<Attribute.Private> deltabag = bag();
		deltabag.setChange(DELETED);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	@Test
	public void modifiedDOsWithNewParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);

		PContainer<Attribute.Private> deltabag = bag();
		deltabag.setChange(NEW);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
		
	// / attributes: largely tested as generic DOs

	@Test
	public void deltaAttributesCanBeFluentlyConstructed() {

		Attribute.Private a = (Attribute.Private) attr().name(name).value(value).as(NEW).build();

		assertTrue(a.isDelta());
		assertEquals(NEW, a.change());

	}

	// codes: largely tested as attriuted DOs

	@Test
	public void deltaCodesCanBeFluentlyConstructed() {

		Code.Private code = (Code.Private) code().name(name).as(NEW).build();

		assertTrue(code.isDelta());
		assertTrue(code.change() == NEW);

	}

	// codelists

	@Test
	public void deltaCodelistsCanBeFluentlyConstructed() {

		Codelist.Private list = (Codelist.Private) codelist().name(name).as(NEW).build();

		assertTrue(list.isDelta());
		assertEquals(NEW, list.change());

	}

	// code bags

	@Test
	public void deltaCodebagsCanBeFluentlyConstructed() {

		Codebag.Private bag = (Codebag.Private) codebag().name(name).as(NEW).build();

		assertTrue(bag.isDelta());
		assertEquals(NEW, bag.change());

	}

}
