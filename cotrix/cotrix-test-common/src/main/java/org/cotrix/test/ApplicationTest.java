package org.cotrix.test;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
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

	@Inject
	Event<ApplicationEvents.LifecycleEvent> events;
	
	@Inject
	Transactions txs;
	
	Transaction tx;

	@BeforeClass
	public static void init() {
		System.setProperty("org.slf4j.simpleLogger.log.org.cotrix","trace");
	}
	
	@Before
	public void startup() {
		events.fire(Startup.INSTANCE);
		tx = txs.open();
	}
	
	@After
	public void shutdown() {
		tx.close();
		events.fire(Shutdown.INSTANCE);
	}
}
