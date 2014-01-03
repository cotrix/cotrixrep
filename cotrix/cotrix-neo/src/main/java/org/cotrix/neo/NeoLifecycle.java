package org.cotrix.neo;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import java.io.File;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.NewStore;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.common.cdi.Current;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(RUNTIME)
public class NeoLifecycle {

	private static Logger log = LoggerFactory.getLogger(NeoLifecycle.class);

	public static class NewNeoStore implements NewStore {
		
		private final GraphDatabaseService store;
		
		public NewNeoStore(GraphDatabaseService store) {
			this.store=store;
		}
		
		public GraphDatabaseService store() {
			return store;
		}
		
	}

	
	@SuppressWarnings("deprecation")

	//produces the database at runtime for the rest of the application
	@Produces @Alternative @Singleton
	static GraphDatabaseService db(Event<NewStore> events, @Current NeoConfiguration config) {
		
		File dir = new File(config.location());
		
		if (!dir.exists())
			if (!dir.mkdirs())
				throw new RuntimeException("cannot create directory @ location "+config.location());
		
		validDirectory(dir);
		
		boolean firstTime = dir.list().length==0;
		
		GraphDatabaseService store =  new GraphDatabaseFactory().
				newEmbeddedDatabaseBuilder(config.location()).
				setConfig(config.properties()).
				newGraphDatabase();
		
		if (firstTime) {
			
			log.info("creating Neo store @ {}",config.location());
			
			events.fire(new NewNeoStore(store));
		}
		else 
			log.info("starting Neo store @ {}",config.location());
		
		return store;
	}
	
	
	@Produces
	static ExecutionEngine engine(GraphDatabaseService store) {
		
		return new ExecutionEngine(store);
	}
	
	static void configureIndices(@Observes NewNeoStore event) {
		
		log.info("creating indices"); 
		
		GraphDatabaseService store = event.store();
		
		//setup index
		try (Transaction tx = store.beginTx()) {
		   
			store.schema().indexFor(IDENTITY).on(name_prop).create();
			store.schema().indexFor(CODELIST).on(id_prop).create();
			
			tx.success();
		}
	}

	
	//factory support
	
	static void startup(@Observes Startup event, GraphDatabaseService store) {
		
		NeoLifecycle.store =store;
	}
	
	private static GraphDatabaseService store;
	
	
	public static Node newnode(NodeType type) {
		return store.createNode(type);
	}
	
	
	//shutdown
	
	static void shutdown(@Observes Shutdown event, GraphDatabaseService store, @Current NeoConfiguration config) {
	
		log.info("stopping Neo @ {}",config.location());
		
		store.shutdown();
	}
	
	
}
