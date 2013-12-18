package org.cotrix.test;

import org.cotrix.domain.memory.IdentifiedMS;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DomainTest {

	
	@BeforeClass
	public static void setup() {
		IdentifiedMS.testmode=true;
	}
	
	@AfterClass
	public static void shutdown() {
		IdentifiedMS.testmode=false;
	}
}
