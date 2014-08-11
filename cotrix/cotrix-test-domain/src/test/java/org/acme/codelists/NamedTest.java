package org.acme.codelists;

import static org.junit.Assert.*;
import static org.cotrix.domain.common.Status.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.acme.DomainTest;
import org.cotrix.domain.memory.MNamed;
import org.cotrix.domain.trait.Named;
import org.junit.Test;

@SuppressWarnings({"unchecked","rawtypes"})
public class NamedTest extends DomainTest {

	@Test
	public void namesMustBeValid() {
		
		Named.Bean state = new MNamed();
		
		try {
			state.qname(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
		
		try {
			state.qname(q(""));
			fail();
		}
		catch(IllegalArgumentException e) {}
		
	}
	
	@Test
	public void namesCannotBeErased() {
		
		Named.Private e = like(new MyEntity(new MNamed()));
		
		MNamed mychange = new MNamed(e.id(),MODIFIED);
		mychange.qname(NULL_QNAME);
		
		Named.Private changeset = new MyEntity(mychange);
		
		try {
			e.update(changeset);
			fail();
		}
		catch(IllegalArgumentException ex) {}
		
		
	}
	
	@Test
	public void namesCanBeChanged() {
		
		Named.Private e = like(new MyEntity(new MNamed()));
		
		MNamed mychange = new MNamed(e.id(),MODIFIED);
		mychange.qname(new QName("newname"));
		
		Named.Private changeset = new MyEntity(mychange);
		
		e.update(changeset);
		
		assertEquals(new QName("newname"),e.qname());
	}
	
	
	static class MyEntity extends Named.Private<MyEntity,MNamed> {
		
		public MyEntity(MNamed state) {
			super(state);
		}
	};
}
