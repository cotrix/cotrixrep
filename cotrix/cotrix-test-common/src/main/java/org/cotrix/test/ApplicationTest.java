package org.cotrix.test;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

/**
 * Base class of integration tests that rely on startup and shutdown events.
 * 
 * 
 */
@RunWith(JeeunitRunner.class)
public abstract class ApplicationTest {

	@Inject
	Event<ApplicationEvents.ApplicationEvent> events;
	
	@Before
	public void startup() {
		events.fire(Startup.INSTANCE);
	}
	
	@After
	public void shutdown() {
		events.fire(Shutdown.INSTANCE);
	}
}