package org.acme;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.memory.IdentifiedMS;
import org.cotrix.test.ApplicationTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class DomainTest extends ApplicationTest {

	@Inject
	SubjectProvider provider;
	
	@BeforeClass
	public static void start() {
		IdentifiedMS.testmode=true;
	}
	
	@AfterClass
	public static void stop() {
		IdentifiedMS.testmode=false;
	}
	
	protected <T> T like(T object) {
		return provider.like(object);
	}
	
	protected NamedStateContainer<Attribute.State> like(Attribute ... attributes) {
		
		Collection<Attribute.State> states = new ArrayList<>();
		
		for (Attribute a : attributes)
			states.add(reveal(a, Attribute.Private.class).state());
		
		return provider.like(states.toArray(new Attribute.State[0]));
	}
}