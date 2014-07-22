package org.acme;

import static java.util.Arrays.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.lifecycle.impl.DefaultLifecycleStates.*;
import static org.junit.Assert.*;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class LifecycleTest extends ApplicationTest {

	String resourceId = "resource";
	
	@Inject
	LifecycleService service;
	
	@Inject
	TestObserver observer;
	
	@Test
	public void startFetchAndUpdateDefaultLifecycle() {
		
		Lifecycle lifecycle = service.start(resourceId);
		
		assertEquals("resource", lifecycle.resourceId());
		assertEquals(draft,lifecycle.state());
		
		Lifecycle retrieved = service.lifecycleOf(resourceId);
		
		assertEquals(lifecycle,retrieved);
		
		retrieved.notify(retrieved.allowed().iterator().next());
		
		service.update(retrieved);
		
		Lifecycle retrievedAfterUpdate = service.lifecycleOf(resourceId);
		
		assertEquals(retrieved,retrievedAfterUpdate);
		
	}
	
	@Test
	public void fetchMultiple() {
	
		Lifecycle lc1 = service.start("1");
		Lifecycle lc2 = service.start("2");
		
		Map<String,Lifecycle> fetched = service.lifecyclesOf(asList("1","2"));

		
		assertEquals(lc1,fetched.get("1"));
		assertEquals(lc2,fetched.get("2"));
	}
		
	@Test
	public void startsInSealedState() {
		
		Lifecycle lifecycle = service.start("resource", DefaultLifecycleStates.sealed);
		
		assertEquals("resource", lifecycle.resourceId());
		assertEquals(sealed,lifecycle.state());
	}
	
	@Test(expected=IllegalArgumentException.class)
	@SuppressWarnings("serial")
	public void createInInvalidState() {
		
		service.start("resource", new State(){});
	}
	
	
	@Test(expected=IllegalStateException.class)
	public void cannotDeleteWhenSealed() {
		
		Lifecycle lifecycle = service.start("resource", DefaultLifecycleStates.sealed);
		
		lifecycle.notify(REMOVE.on(lifecycle.resourceId()));
	}
	

	@Test(expected=IllegalStateException.class)
	public void delete() {
		
		Lifecycle lifecycle = service.start("resource");
		
		service.delete(lifecycle.resourceId());
		
		assertNull(service.lifecycleOf(lifecycle.resourceId()));
	}
		
	@Test
	public void fullUseStory() {
		 
		observer.reset();
		
		Lifecycle lc = service.start(resourceId);
		
		lc = service.lifecycleOf(resourceId);
		
		assertFalse(observer.observed());
		
		lc.notify(lc.allowed().iterator().next());
		
		assertTrue(observer.observed());
		
	}
		
	@Singleton //ensures single object is injected here and in event producer
	static class TestObserver {
		
		boolean observed=false;
		
		public void reset() {
			observed=false;
		}
		
		public boolean observed() {
			return observed;
		}
		public void observe(@Observes @Any LifecycleEvent event) {
			observed=true;
		}
	}
}
