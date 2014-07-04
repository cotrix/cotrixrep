package org.cotrix.test;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.ApplicationLifecycle;
import org.cotrix.common.tx.Transaction;
import org.cotrix.common.tx.Transactions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

/**
 * Base class for integration tests that rely on startup and shutdown events.
 * 
 * 
 */
@RunWith(JeeunitRunner.class)
public abstract class ApplicationTest {
	
	//supports test initialisation ahead of app initialisation
	public static class Start {
		public static Start INSTANCE = new Start();
	}

	@Inject
	ApplicationLifecycle app;
	
	@Inject
	Event<Start> test;
	
	@Inject
	Transactions txs;
	
	Transaction tx;

	@BeforeClass
	public static void init() {
		System.setProperty("org.slf4j.simpleLogger.log.org.cotrix","trace");
	}
	
	@Before
	public void startup() {
		
		//notifies test has started
		test.fire(Start.INSTANCE);
		
		//simulates app start
		app.start();
		
		tx = txs.open();
	}
	
	@After
	public void shutdown() {
		
		tx.close();
		
		app.stop();
	}
}
