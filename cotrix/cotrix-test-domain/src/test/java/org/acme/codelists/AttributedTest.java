package org.acme.codelists;

import static org.junit.Assert.*;

import org.acme.DomainTest;
import org.cotrix.domain.memory.MDescribed;
import org.cotrix.domain.trait.Described;
import org.junit.Test;

public class AttributedTest extends DomainTest {


	@Test
	public void attributesMustBeValid() {
		
		MDescribed state = new MDescribed();
		
		try {
			state.attributes(null);
			fail();
		}
		catch(IllegalArgumentException e) {}
	}
	
	static class MyEntity extends Described.Private<MyEntity,MDescribed> {
		
		public MyEntity(MDescribed state) {
			super(state);
		}
	};
}
