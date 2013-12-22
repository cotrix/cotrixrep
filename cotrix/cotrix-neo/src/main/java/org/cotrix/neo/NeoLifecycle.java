package org.cotrix.neo;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.configuration.Provider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeoLifecycle {

	private static Logger log = LoggerFactory.getLogger(NeoLifecycle.class);
	
	@Inject
	Provider<NeoConfiguration> provider;
	
	@SuppressWarnings("deprecation")
	@Produces @Singleton
	GraphDatabaseService db() {
		
		NeoConfiguration config = provider.get();
		
		log.info("starting Neo @ {}",provider.get().location());
	
		
		return new GraphDatabaseFactory().
				newEmbeddedDatabaseBuilder(config.location()).
				setConfig(config.properties()).
				newGraphDatabase();
	}
	
	
	void shutdown(@Observes Shutdown event, GraphDatabaseService service) {
	
		log.info("stopping Neo @ {}",provider.get().location());
		
		service.shutdown();
	}
}
