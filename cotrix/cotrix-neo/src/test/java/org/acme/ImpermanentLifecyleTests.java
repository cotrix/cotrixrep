package org.acme;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.test.ApplicationTest;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class ImpermanentLifecyleTests extends ApplicationTest {

	@Inject
	GraphDatabaseService db;

	@Test
	public void oneTest() {

		try (Transaction tx = db.beginTx()) {
			db.createNode();
			tx.success();
		}

	}

	@Test
	@SuppressWarnings("all")
	public void anotherTest() {

		try (Transaction tx = db.beginTx()) {
			assertFalse(db.getAllNodes().iterator().hasNext());
		}

	}

}
