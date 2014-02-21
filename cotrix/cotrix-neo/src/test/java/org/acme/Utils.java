package org.acme;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.acme.utils.TestNeoStore;
import org.cotrix.neo.NeoLifecycle;
import org.cotrix.test.ApplicationTest;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(TEST)
public class Utils {

	
	private static Logger log = LoggerFactory.getLogger("test");
	
	//we need to:
	// a) produce a fresh db @ each test, but the same within one test.
	//	  we use a singleton delegate and stuff it with a fresh db at each test.
	// b) inform the application is always a new store scenario
	
	static TestNeoStore tstore = new TestNeoStore();
	
	
	public static void start(@Observes ApplicationTest.Start event, NeoLifecycle neo) {
		
		log.info("creating in memory store for testing");
		
		//purely in-memory
		GraphDatabaseService store = new TestGraphDatabaseFactory().newImpermanentDatabase();
		
		neo.init(store);
		
		//plug fresh store for current test
		tstore.setInner(store);
		
	}
			
	@Produces @Alternative @Singleton
	public static GraphDatabaseService testStore() {
		return tstore;
	}
	
	
	
	
}
