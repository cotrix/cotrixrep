package org.acme.codelists;

import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.memory.MAttributed;
import org.cotrix.domain.trait.Attributed;
import org.junit.Test;

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
	
	static class MyEntity extends Attributed.Private<MyEntity,MAttributed> {
		
		public MyEntity(MAttributed state) {
			super(state);
		}
	};
}
