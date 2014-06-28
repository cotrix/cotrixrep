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
	private final GraphDatabaseService store;
	
	private static final InheritableThreadLocal<NeoTransaction> current = new InheritableThreadLocal<NeoTransaction>();
	
	
	public static NeoTransaction current() {
		return current.get();
	}
	
	@Inject
	public NeoTransaction(GraphDatabaseService store) {
		this.store=store;
		open();
	}
	
	void open() {
		tx = store.beginTx();
		current.set(this);
	}
	
	@Override
	public void commit() {
		tx.success();
		
	}
	
	public void split() {
		
		commit();
		close();
		open();
		
	}

	@Override
	public void close() {
		tx.close();
	}

	
}
