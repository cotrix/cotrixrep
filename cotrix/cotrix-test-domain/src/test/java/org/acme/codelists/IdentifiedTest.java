package org.acme.codelists;

import static org.junit.Assert.*;
import static org.cotrix.domain.common.Status.*;

import org.acme.DomainTest;
import org.cotrix.domain.memory.MIdentified;
import org.cotrix.domain.trait.Identified;
import org.junit.Test;

public class IdentifiedTest extends DomainTest {

	@Test
	public void newEntitiesHaveIdentifiers() {
		
		Identified.Private<?,?> e = like(new MyEntity(new MIdentified()));
		
		assertNotNull(e.id());
		assertNull(e.status());
		
	}
	
	@Test
	public void identifierAndStatusMustBeValid() {
		
		try {
			like(new MyEntity(new MIdentified(null,MODIFIED)));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		
		try {
			like(new MyEntity(new MIdentified("id",null)));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void changesetsCannotBeNull() {
		
		MyEntity e = new MyEntity(new MIdentified());
		
		try {
			e.update(null);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void changesetsCannotBeNew() {
		
		MyEntity e = new MyEntity(new MIdentified());
		MyEntity newEntity = new MyEntity(new MIdentified());
		
		try {
			e.update(newEntity);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void changesetsMustMatchIdentifiers() {
		
		MyEntity e = new MyEntity(new MIdentified());
		
		try {
			e.update(null);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
		MyEntity changeset = new MyEntity(new MIdentified("another",MODIFIED));
		
		try {
			e.update(changeset);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
	}
	
	@Test
	public void changesetsCannotBeUpdated() {
		
		MyEntity changeset = new MyEntity(new MIdentified("someid",MODIFIED){});
		
		try {
			changeset.update(changeset);
			fail();
		}
		catch(IllegalStateException ex) {}
		
	}
	
	@Test
	public void changesetsCannotBeDirectlyDeleted() {
		
		MyEntity e = new MyEntity(new MIdentified());
		MyEntity changeset = new MyEntity(new MIdentified("someid",DELETED));
		
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
