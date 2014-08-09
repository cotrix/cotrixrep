package org.acme.codelists;

import static org.junit.Assert.*;
import static org.cotrix.domain.trait.Status.*;

import org.acme.DomainTest;
import org.cotrix.domain.memory.IdentifiedMS;
import org.cotrix.domain.trait.Identified;
import org.junit.Test;

public class IdentifiedTest extends DomainTest {

	@Test
	public void newEntitiesHaveIdentifiers() {
		
		Identified.Private<?,?> e = like(new MyEntity(new IdentifiedMS()));
		
		assertNotNull(e.id());
		assertNull(e.status());
		
	}
	
	@Test
	public void identifierAndStatusMustBeValid() {
		
		try {
			like(new MyEntity(new IdentifiedMS(null,MODIFIED)));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		
		try {
			like(new MyEntity(new IdentifiedMS("id",null)));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void changesetsCannotBeNull() {
		
		MyEntity e = new MyEntity(new IdentifiedMS());
		
		try {
			e.update(null);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void changesetsCannotBeNew() {
		
		MyEntity e = new MyEntity(new IdentifiedMS());
		MyEntity newEntity = new MyEntity(new IdentifiedMS());
		
		try {
			e.update(newEntity);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void changesetsMustMatchIdentifiers() {
		
		MyEntity e = new MyEntity(new IdentifiedMS());
		
		try {
			e.update(null);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
		MyEntity changeset = new MyEntity(new IdentifiedMS("another",MODIFIED));
		
		try {
			e.update(changeset);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void changesetsCannotBeUpdated() {
		
		MyEntity changeset = new MyEntity(new IdentifiedMS("someid",MODIFIED){});
		
		try {
			changeset.update(changeset);
			fail();
		}
		catch(IllegalStateException ex) {}
		
	}
	
	@Test
	public void changesetsCannotBeDirectlyDeleted() {
		
		MyEntity e = new MyEntity(new IdentifiedMS());
		MyEntity changeset = new MyEntity(new IdentifiedMS("someid",DELETED));
		
		try {
			e.update(changeset);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	
	static class MyEntity extends Identified.Private<MyEntity,Identified.Bean> {
		
		public MyEntity(Identified.Bean state) {
			super(state);
		}
	};
}
