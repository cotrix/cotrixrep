package org.acme.codelists;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.memory.MAttributed;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Status;
import org.junit.Test;

@SuppressWarnings({"rawtypes","unchecked"})
public class AttributedTest extends DomainTest {


	@Test
	public void attributesMustBeValid() {
		
		MAttributed state = new MAttributed();
		
		try {
			state.attributes(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
	}
	
	@Test
	public void creationTimeIsTracked() {
		
		Attributed e = like(new MyEntity(new MAttributed()));
		
		assertTrue(e.attributes().contains(CREATION_TIME));
		
		assertEquals(1,e.attributes().get(CREATION_TIME).size());
	}
	
	@Test
	public void creationTimeIsNotTrackedOnChangesets() {
		
		Attributed e = like(new MyEntity(new MAttributed("someid",Status.MODIFIED)));
		
		assertFalse(e.attributes().contains(CREATION_TIME));
		
	}
	
	@Test
	public void updateTimeIsTracked() {
		
		Attributed.Private e = like(new MyEntity(new MAttributed()));
	
		Attributed.Private changeset = new MyEntity(new MAttributed(e.id(),Status.MODIFIED));
		
		e.update(changeset);
		
		assertTrue(e.attributes().contains(UPDATE_TIME));
		
		assertEquals(1,e.attributes().get(UPDATE_TIME).size());
		
		e.update(changeset);
		
		assertEquals(1,e.attributes().get(UPDATE_TIME).size());
	}
	
	
	
	static class MyEntity extends Attributed.Private<MyEntity,MAttributed> {
		
		public MyEntity(MAttributed state) {
			super(state);
		}
	};
}
