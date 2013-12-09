package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.memory.NamedMS;
import org.cotrix.domain.trait.Named;
import org.junit.Test;

public class NamedTest {

	@Test
	public void namesMustBeValid() {
		
		Named.State state = new NamedMS();
		
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
	
	@Test
	public void namesCannotBeErased() {
		
		MyEntity e = new MyEntity(new NamedMS());
		
		NamedMS mychange = new NamedMS(e.id(),MODIFIED);
		mychange.name(NULL_QNAME);
		
		MyEntity changeset = new MyEntity(mychange);
		
		try {
			e.update(changeset);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
		
	}
	
	@Test
	public void namesCanBeChanged() {
		
		MyEntity e = new MyEntity(new NamedMS());
		
		NamedMS mychange = new NamedMS(e.id(),MODIFIED);
		mychange.name(new QName("newname"));
		
		MyEntity changeset = new MyEntity(mychange);
		
		e.update(changeset);
		
		assertEquals(new QName("newname"),e.name());
	}
	
	
	static class MyEntity extends Named.Abstract<MyEntity,NamedMS> {
		
		public MyEntity(NamedMS state) {
			super(state);
		}
	};
}
