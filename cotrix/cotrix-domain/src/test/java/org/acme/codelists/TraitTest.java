package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.memory.AttributedMS;
import org.cotrix.domain.memory.IdentifiedMS;
import org.cotrix.domain.memory.NamedMS;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Status;
import org.junit.Test;

public class TraitTest {

	@Test
	public void newEntitiesHaveIdentifiers() {
		
		
		Identified.State state = new IdentifiedMS() {};
		
		assertNotNull(state.id());
		assertNull(state.status());
		
	}
	
	@Test
	public void identifierAndStatusMustBeValid() {
		
		try {
			new IdentifiedMS(null,Status.MODIFIED) {};
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		
		try {
			new IdentifiedMS("id",null) {};
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void namesMustBeValid() {
		
		Named.State state = new NamedMS() {};
		
		try {
			state.name(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			state.name(q(""));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	//all attributed DOs: we workd directly against AttributedMS class simulating a subclass
	
	@Test
	@SuppressWarnings("all")
	public void attributesMustBeValid() {
		
		AttributedMS po = new AttributedMS() {};
		
		try {
			po.attributes(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
	}
}
