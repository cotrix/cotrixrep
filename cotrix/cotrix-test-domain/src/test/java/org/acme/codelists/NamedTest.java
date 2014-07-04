package org.acme.codelists;

import static org.junit.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.trait.Status.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.acme.DomainTest;
import org.cotrix.domain.memory.NamedMS;
import org.cotrix.domain.trait.Named;
import org.junit.Test;

@SuppressWarnings({"unchecked","rawtypes"})
public class NamedTest extends DomainTest {

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
		
		Named.Abstract e = like(new MyEntity(new NamedMS()));
		
		NamedMS mychange = new NamedMS(e.id(),MODIFIED);
		mychange.name(NULL_QNAME);
		
		Named.Abstract changeset = new MyEntity(mychange);
		
		try {
			e.update(changeset);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
		
	}
	
	@Test
	public void namesCanBeChanged() {
		
		Named.Abstract e = like(new MyEntity(new NamedMS()));
		
		NamedMS mychange = new NamedMS(e.id(),MODIFIED);
		mychange.name(new QName("newname"));
		
		Named.Abstract changeset = new MyEntity(mychange);
		
		e.update(changeset);
		
		assertEquals(new QName("newname"),e.qname());
	}
	
	
	static class MyEntity extends Named.Abstract<MyEntity,NamedMS> {
		
		public MyEntity(NamedMS state) {
			super(state);
		}
	};
}
