package org.cotrix.test;

import org.cotrix.domain.memory.IdentifiedMS;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DomainTest {

	//sets a secret constant that ignores ids during equality tests
	@BeforeClass
	public static void setup() {
		IdentifiedMS.testmode=true;
	}
	
	@AfterClass
	public static void shutdown() {
		IdentifiedMS.testmode=false;
	}
}
