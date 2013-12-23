package org.acme;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.cotrix.common.Constants;
import org.cotrix.common.cdi.Current;
import org.cotrix.neo.NeoConfiguration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Priority(TEST)
@SuppressWarnings("all")
public class Utils {

	private static Logger log = LoggerFactory.getLogger("test");
	
	@Produces @Alternative @Singleton
	public GraphDatabaseService testDatabase() {
		
		log.info("starting Neo4j in memory for testing");
		
		return new TestGraphDatabaseFactory().
				newImpermanentDatabaseBuilder("target/neo").
				newGraphDatabase();
	}
}
