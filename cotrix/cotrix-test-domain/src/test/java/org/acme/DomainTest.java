package org.acme;

import static org.cotrix.common.CommonUtils.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.memory.MIdentified;
import org.cotrix.test.ApplicationTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class DomainTest extends ApplicationTest {

	@Inject
	SubjectProvider provider;
	
	@BeforeClass
	public static void start() {
		MIdentified.testmode=true;
	}
	
	@AfterClass
	public static void stop() {
		MIdentified.testmode=false;
	}
	
	protected <T> T like(T object) {
		return provider.like(object);
	}
	
	
	protected Container.Bean<Attribute.Bean> likes(Attribute ... attributes) {
		
		Collection<Attribute.Bean> states = new ArrayList<>();
		
		for (Attribute a : attributes)
			states.add(reveal(a, Attribute.Private.class).bean());
		
		return provider.like(states.toArray(new Attribute.Bean[0]));
	}
	
	
}
