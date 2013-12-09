package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;

import org.cotrix.domain.memory.AttributedMS;
import org.cotrix.domain.trait.Attributed;
import org.junit.Test;

public class AttributedTest {


	@Test
	public void attributesMustBeValid() {
		
		AttributedMS state = new AttributedMS();
		
		try {
			state.attributes(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
	}
	
	@Test
	public void creationTimeIsTracked() {
		
		MyEntity e = new MyEntity(new AttributedMS());
		
		assertTrue(e.attributes().contains(CREATION_TIME));
	}
	
	@Test
	public void creationTimeIsNotTrackedOnChangesets() {
		
		MyEntity e = new MyEntity(new AttributedMS("someid",MODIFIED));
		
		assertFalse(e.attributes().contains(CREATION_TIME));
	}
	
	@Test
	public void updateTimeIsTracked() {
		
		MyEntity e = new MyEntity(new AttributedMS());
	
		MyEntity changeset = new MyEntity(new AttributedMS(e.id(),MODIFIED));
		
		e.update(changeset);
		
		assertTrue(e.attributes().contains(UPDATE_TIME));
		
		e.update(changeset);
		
		assertEquals(1,e.attributes().getAll(UPDATE_TIME).size());
	}
	
	
	
	static class MyEntity extends Attributed.Abstract<MyEntity,AttributedMS> {
		
		public MyEntity(AttributedMS state) {
			super(state);
		}
	};
}
