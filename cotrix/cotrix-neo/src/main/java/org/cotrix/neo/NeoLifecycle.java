package org.cotrix.neo;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import java.io.File;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.common.cdi.ApplicationLifecycle;
import org.cotrix.common.cdi.Current;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")

// this is the priority of store producer below (test producers have it higher)

@Singleton @Priority(RUNTIME)
public class NeoLifecycle {

	// need to:
	// a) produce a store to use in production
	// b) signal app when it's a new store
	// c) shutdown the store gracefully along with application

	private static Logger log = LoggerFactory.getLogger(NeoLifecycle.class);

	private boolean newstore = true;

	@Produces
	@Alternative
	@Singleton
	GraphDatabaseService store(@Current NeoConfiguration config) {

		File dir = new File(config.location());

		if (dir.exists())
			validDirectory(dir);
		else
			if (!dir.mkdirs()) 
				throw new RuntimeException("cannot create directory @ location " + config.location());
	

		// remembers if directory was empty (new store)
		newstore = dir.list().length == 0;

		final GraphDatabaseService store = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(config.location()).setConfig(config.properties())
				.newGraphDatabase();
		
		if (newstore) {
		
			log.info("creating Neo store @ {}", config.location());
			
			init(store);
		}
		else
			log.info("restarting Neo store @ {}", config.location());
		
		
		return store;
	}
	
	//at startup, notifies app lifecycle it's a fresh start
	//(we know by now, as store has been produced)
	void onStart(@Observes Startup event, ApplicationLifecycle app) {

		if (!newstore)
			app.isRestart();
		
	}

	// why not more elegantly consume new store event?
	// currently complicates testing for a combination of factors
	// (lack of ordering of consumers, workaround for current CDI-based test facilities, etc)
	public void init(GraphDatabaseService store) {

		log.info("creating Neo indices");

		try (Transaction tx = store.beginTx()) {

			store.schema().indexFor(IDENTITY).on(name_prop).create();
			store.schema().indexFor(CODELIST).on(id_prop).create();
			store.schema().indexFor(DEFINITION).on(id_prop).create();
			store.schema().indexFor(CODELISTLINK).on(id_prop).create();
			store.schema().indexFor(USER).on(name_prop).create();
			store.schema().indexFor(LIFECYCLE).on(id_prop).create();

			tx.success();
		}

	}

	@Produces
	static ExecutionEngine engine(GraphDatabaseService store) {

		return new ExecutionEngine(store);
	}

	// shutdown
	static void shutdown(@Observes Shutdown event, GraphDatabaseService store, @Current NeoConfiguration config) {

		log.info("stopping Neo store @ {}", config.location());

		store.shutdown();
	}

}
