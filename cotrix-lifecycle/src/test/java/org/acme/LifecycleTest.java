package org.acme;

import static org.cotrix.lifecycle.Action.*;
import static org.junit.Assert.*;

import java.util.concurrent.Callable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.lifecycle.LifecycleFactory;
import org.cotrix.lifecycle.LifecycleRegistry;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.impl.DefaultLifecycleFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class LifecycleTest {

	@Inject
	LifecycleRegistry registry;
	
	@Inject
	LifecycleService service;
	
	@Inject
	TestObserver observer;
	
	String resourceId = "id";
	
	//client knows resource identifier
	@Test
	public void startAndFetchDefaultLifecycle() {
		
		Lifecycle lc = service.start(resourceId);
		
		Lifecycle retrieved = service.lifecycleOf(resourceId);
		
		assertEquals(lc,retrieved);
		
	}
	
	@Test
	public void fullUseStory() {
		
		/////////////////////////////// start lifecycle
		
		Lifecycle lc = service.start(resourceId);
		
		System.out.println("prepare GUI to allow only: "+lc.allowed());
		
		/////////////////////////////// move lifecycle
		
		//grab lifecycle for given resource
		lc = service.lifecycleOf(resourceId);
		
		assertFalse(observer.observed);
		
		//check action is allowed
		if (!lc.allows(edit))
			throw new IllegalStateException();
		
		System.out.println("performing action: "+edit+" on "+lc.resourceId());
		
		//change lifecycle
		lc.notify(edit);
		
		assertTrue(observer.observed);
		
		System.out.println("prepare GUI to allow only: "+lc.allowed());
				
	}
	
	 
	@Test
	public void taskBasedUseStory() {
		
		Lifecycle lc = service.start(resourceId);
		
		Callable<Void> task = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				System.out.println("performing action: "+edit+" on "+resourceId+" using injected resource e.g. "+service);
				return null;
			}
		};
		
		lc.perform(edit).with(task);
		
		System.out.println("prepare GUI to allow only: "+lc.allowed());
				
	}
	
	@Singleton //singleton here ensures that we have in the test the same instance injected in lifecycle 
	static class TestObserver {
		
		boolean observed=false;
		
		public void observe(@Observes @Any LifecycleEvent event) {
			System.out.println("observed "+event);
			observed=true;
		}
	}
	
	@SuppressWarnings("serial")
	LifecycleFactory testFactory = new LifecycleFactory() {
		
		public String name() {
			return new DefaultLifecycleFactory().name();
		}
		
		public Lifecycle create(String id) {
			return new DefaultLifecycleFactory().create(id);
		}
	};
	
}
