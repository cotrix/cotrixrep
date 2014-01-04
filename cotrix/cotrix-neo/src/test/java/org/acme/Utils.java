package org.acme;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.acme.utils.TestNeoEngine;
import org.acme.utils.TestNeoStore;
import org.cotrix.common.cdi.ApplicationEvents.NewStore;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.neo.NeoLifecycle.NewNeoStore;
import org.cotrix.neo.repository.NeoQueryEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(TEST)
@SuppressWarnings("all")
public class Utils {

	private static Logger log = LoggerFactory.getLogger("test");
	
	//we want a fresh db across tests, but the same within one test.
	//we use a singleton delegate and stuff it with a fresh db at each test.
	
	static TestNeoStore store = new TestNeoStore();
	
	
	public static void init(@Observes Startup event, Event<NewStore> events) {
		
		log.info("creating Neo store in memory for testing");
		
		store.setInner(new TestGraphDatabaseFactory().newImpermanentDatabase());
		
		events.fire(new NewNeoStore(store));
	}
			
	@Produces @Alternative @Singleton
	public static GraphDatabaseService testStore() {
		
		return store;
	}
	
	
	
	
}
