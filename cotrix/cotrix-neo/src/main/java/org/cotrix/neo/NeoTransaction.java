package org.cotrix.neo;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.common.tx.Transaction;
import org.neo4j.graphdb.GraphDatabaseService;

@Alternative @Priority(RUNTIME)
public class NeoTransaction implements Transaction {

	private org.neo4j.graphdb.Transaction tx;
	
	@Inject
	public NeoTransaction(GraphDatabaseService store) {
		tx = store.beginTx();
	}
	
	@Override
	public void commit() {
		tx.success();
		
	}

	@Override
	public void close() {
		tx.close();
	}

	
}
