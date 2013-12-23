package org.cotrix.neo;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;

import java.io.File;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.Current;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(RUNTIME)
public class NeoLifecycle {

	private static Logger log = LoggerFactory.getLogger(NeoLifecycle.class);
	
	@Inject @Current
	NeoConfiguration config;
	
	@SuppressWarnings("deprecation")

	@Produces @Alternative @Singleton
	GraphDatabaseService db() {
		
		File dir = new File(config.location());
		
		if (!dir.exists())
			if (!dir.mkdirs())
				throw new RuntimeException("cannot create directory @ location "+config.location());
		
		validDirectory(dir);
		
		log.info("starting Neo @ {}",config.location());
	
		return new GraphDatabaseFactory().
				newEmbeddedDatabaseBuilder(config.location()).
				setConfig(config.properties()).
				newGraphDatabase();
	}
	
	
	void shutdown(@Observes Shutdown event, GraphDatabaseService service) {
	
		log.info("stopping Neo @ {}",config.location());
		
		service.shutdown();
	}
}
