package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Change.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.Container;
import org.cotrix.domain.po.AttributedPO;
import org.cotrix.domain.po.EntityPO;
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

		Container.Private<Attribute.Private> bag = bag();

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

		Attribute.Private a = (Attribute.Private) attr().add().name(name).value(value).build();

		Container.Private<Attribute.Private> bag = bag(a);
		
		assertTrue(bag.change()==NEW);
	}

	@Test
	public void bagsWithModifiedObjectsBecomeModified() {

		Attribute.Private a = (Attribute.Private) attr("1").modify().name(name).value(value).build();

		Container.Private<Attribute.Private> bag = bag(a);

		assertTrue(bag.change()==MODIFIED);
	}

	@Test
	public void bagsWithDeletedObjectsBecomeModified() {

		Attribute.Private a = (Attribute.Private) attr("1").delete();

		Container.Private<Attribute.Private> bag = bag(a);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void newBagsWithNewObjectsRemainNew() {

		Attribute.Private a1 = (Attribute.Private) attr().add().name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private) attr().add().name(name2).value(value2).build();
		
		Container.Private<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==NEW);
	}
	
	@Test
	public void newBagsWithModifiedObjectsBecomeModified() {

		Attribute.Private a1 = (Attribute.Private) attr().add().name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private) attr("1").modify().name(name2).value(value2).build();

		Container.Private<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void newBagsWithDeletedObjectsBecomeModified() {

		Attribute.Private a1 = (Attribute.Private) attr().add().name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private) attr("1").delete();
		
		Container.Private<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void modifiedBagsWithNewObjectsRemainModified() {

		Attribute.Private a1 = (Attribute.Private) attr("1").modify().name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private) attr().add().name(name2).value(value2).build();
		
		Container.Private<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	@Test
	public void modifiedBagsWithDeletedObjectsRemainModified() {

		Attribute.Private a1 = (Attribute.Private)attr("1").modify().name(name).value(value).build();
		Attribute.Private a2 = (Attribute.Private)attr("2").delete();
		
		Container.Private<Attribute.Private> bag = bag(a1,a2);

		assertTrue(bag.change()==MODIFIED);
	}
	
	//complex DOs behave like bags with respect to change propagation, but code cannot be reused, so needs checking
	//we can use attributed DOs base class for these tests

	@Test
	public void deltaDOsCanOnlyTakeDeltaParameters() {

		AttributedPO po = new AttributedPO("id") {};

		po.setChange(MODIFIED);

		// a non-delta parameter
		Container.Private<Attribute.Private> attributes = bag();
		
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

		Container.Private<Attribute.Private> deltabag = bag();
		deltabag.setChange(MODIFIED);

		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}

	@Test
	public void DOsWithDeletedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};

		Container.Private<Attribute.Private> deltabag = bag();
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
		Attribute.Private deltaAttribute = (Attribute.Private) attr().add().name(name).value(value).build();

		Container.Private<Attribute.Private> deltabag = bag(deltaAttribute);

		po.setAttributes(deltabag);

		assertTrue(po.change() == NEW);

	}

	//for convenience of testing, we can simulate the delta parameter propagation by setting one twice
	//we can use the generic attributed PO to test the various use cases
	
	@Test
	public void newDOsWithNewParametersRemainNew() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(NEW);
		
		Container.Private<Attribute.Private> deltabag = bag();
		deltabag.setChange(NEW);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == NEW);

	}
	
	
	@Test
	public void newDOsWithModifiedParametersBecomeModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(NEW);
		
		Container.Private<Attribute.Private> deltabag = bag();
		deltabag.setChange(MODIFIED);
		 
		po.setAttributes(deltabag);

		assertTrue(po.change() == MODIFIED);

	}
	
	@Test
	public void modifiedDOsWithModifiedParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);
		
		Container.Private<Attribute.Private> deltabag = bag();
		deltabag.setChange(MODIFIED);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	
	@Test
	public void modifiedDOsWithDeletedParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);

		Container.Private<Attribute.Private> deltabag = bag();
		deltabag.setChange(DELETED);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	@Test
	public void modifiedDOsWithNewParametersRemainModified() {

		AttributedPO po = new AttributedPO("id") {};
		po.setChange(MODIFIED);

		Container.Private<Attribute.Private> deltabag = bag();
		deltabag.setChange(NEW);

		po.setAttributes(deltabag);
		
		assertTrue(po.change() == MODIFIED);

	}
	
	
	@Test
	public void codelistWithNewAttribute() {

		Attribute a = attr().add().name("name").value("val").build();
	
		Codelist.Private delta = (Codelist.Private) codelist("12").modify().attributes(a).build();
	
		assertEquals(MODIFIED,delta.change());
	}
	
	
	@Test
	public void codelistWithNewCode() {

		Code c = code().add().name("name").build();
	
		Codelist.Private delta = (Codelist.Private) codelist("12").modify().with(c).build();
	
		assertEquals(MODIFIED,delta.change());
	}
	
	
	
	
		
	@Test
	public void deltaAttributesCanBeFluentlyConstructed() {

		Attribute a;
		
		//new attributes
		 a =  attr().name(name).build();
		 a =  attr().name(name).value(value).build();
		 a =  attr().name(name).value(value).ofType("type").build();
		 a =  attr().name(name).value(value).ofType("type").in("en").build();
		 a =  attr().name(name).value(value).in("en").build();
		 
		 
		assertFalse(((Attribute.Private) a).isChangeset());
		assertNull(((Attribute.Private) a).change());
			
		//added attributes
		 a = attr().add().name(name).value(value).build();
		 a = attr().add().name(name).value(value).ofType("type").build();
		 a = attr().add().name(name).value(value).ofType("type").in("en").build();
		 a = attr().add().name(name).value(value).in("en").build();
						  
		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(NEW, ((Attribute.Private) a).change());		
		
		//modified attributes
		 a = attr("1").modify().name(name).build();
		 a = attr("1").modify().name(name).value(value).build();
		 a = attr("1").modify().ofType("type").build();
		 a = attr("1").modify().ofType("type").in("en").build();
		 a = attr("1").modify().in("en").build();

		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(MODIFIED, ((Attribute.Private) a).change());		
		
		
		//removed attributes
		a = attr("1").delete();

		assertTrue(((Attribute.Private) a).isChangeset());
		assertEquals(DELETED, ((Attribute.Private) a).change());		
	}
	

	// codes: largely tested as attriuted DOs

	@Test
	public void deltaCodesCanBeFluentlyConstructed() {
		
		
		Attribute a = attr().name("name").build();
		Attribute added = attr().add().name("name").build();
		Attribute modified = attr("1").modify().name("name").build();
		Attribute deleted = attr("1").delete();
		
		Code c;

		//new codes
		c = code().name(name).build();
		c = code().name(name).attributes(a).build();

		assertEquals(null,((Code.Private) c).change());
		
		
		//added codes
		 c = code("1").add().name(name).build();
		 c = code("1").add().name(name).attributes(added).build();
						  
		assertEquals(NEW,((Code.Private) c).change());
		
		c = code("1").modify().attributes(modified,added,deleted).build();
		
		//changed
		assertEquals(MODIFIED,((Code.Private) c).change());
		
		c = code("1").delete();

		assertEquals(DELETED,((Code.Private) c).change());
	}
	
	
	// codelists

	@Test
	public void deltaCodelistsCanBeFluentlyConstructed() {

		Attribute a = attr().name("name").build();
		Attribute added = attr().add().name("name").build();
		Attribute modified = attr("1").modify().name("name").build();
		Attribute deleted = attr("1").delete();
		
		Code c = code().name("name").attributes(a).build();
		Code addedCode = code("1").add().name("name").attributes(added).build();
		Code modifiedCode = code("1").modify().name("name").attributes(added, modified, deleted).build();
		Code deletedCode = code("1").delete();

		Codelist l;
		
		//new codelists
		l = codelist().name(name).build();
		l = codelist().name(name).with(c).build();
		l = codelist().name(name).attributes(a).build();
		l = codelist().name(name).with(c).attributes(a).build();

		assertEquals(null,((Codelist.Private) l).change());
		
		//added codes
		l = codelist("1").add().name(name).build(); //plus all cases above
						  
		assertEquals(NEW,((Codelist.Private) l).change());
		
		l = codelist("1").modify().attributes(modified,added,deleted).build();
		l = codelist("1").modify().with(modifiedCode,addedCode,deletedCode).build();
		l = codelist("1").modify().with(modifiedCode,addedCode,deletedCode).attributes(modified,added,deleted).build();
		
		//changed
		assertEquals(MODIFIED,((Codelist.Private) l).change());
		
		l = codelist("1").delete();

		assertEquals(DELETED,((Codelist.Private) l).change());

	}

}
