package org.cotrix.neo;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.common.tx.Transaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative @Priority(RUNTIME)
public class NeoTransaction implements Transaction {

	private static Logger log = LoggerFactory.getLogger(NeoTransaction.class);
	
	private org.neo4j.graphdb.Transaction tx;
	
	@Inject
	public NeoTransaction(GraphDatabaseService store) {
		log.trace("transaction start");
		tx = store.beginTx();
	}
	
	@Override
	public void commit() {
		log.trace("transaction commit");
		tx.success();
		
	}

	@Override
	public void close() {
		log.trace("transaction end");
		tx.close();
	}

	
}
