package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.lifecycle.impl.DefaultLifecycleStates.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class LifecycleTest {

	String resourceId = "resource";
	
	@Inject
	LifecycleService service;
	
	@Inject
	TestObserver observer;
	
	
	@Test
	public void startAndFetchDefaultLifecycle() {
		
		Lifecycle lifecycle = service.start(resourceId);
		
		assertEquals("resource", lifecycle.resourceId());
		assertEquals(draft,lifecycle.state());
		
		Lifecycle retrieved = service.lifecycleOf(resourceId);
		
		assertEquals(lifecycle,retrieved);
		
	}
		
	@Test
	public void startsInSealedState() {
		
		Lifecycle lifecycle = service.start("resource", DefaultLifecycleStates.sealed);
		
		assertEquals("resource", lifecycle.resourceId());
		assertEquals(sealed,lifecycle.state());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createInInvalidState() {
		
		service.start("resource", new State(){});
	}
	
	
		
	@Test
	public void fullUseStory() {
		
		/////////////////////////////// start lifecycle
		
		Lifecycle lc = service.start(resourceId);
		
		System.out.println("prepare GUI to allow only: "+lc.allowed());
		
		/////////////////////////////// move lifecycle
		
		
		//grab lifecycle for given resource
		lc = service.lifecycleOf(resourceId);
		
		assertFalse(observer.observed());
		
		//check action is allowed
		if (!lc.allows(LOCK.on(resourceId)))
			throw new IllegalStateException();
		
		System.out.println("performing action: "+LOCK.on(resourceId));
		
		//change lifecycle
		lc.notify(LOCK.on(resourceId));
		
		System.out.println(observer);
		
		assertTrue(observer.observed());
		
		System.out.println("prepare GUI to allow only: "+lc.allowed());
				
	}
		
	@ApplicationScoped //ensures single object is injected here and in event producer
	static class TestObserver {
		
		boolean observed=false;
		
		public boolean observed() {
			return observed;
		}
		public void observe(@Observes @Any LifecycleEvent event) {
			System.out.println(this);
			System.out.println("observed "+event);
			observed=true;
		}
	}
}
