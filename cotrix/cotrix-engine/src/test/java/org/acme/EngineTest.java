package org.acme;

import static org.cotrix.action.Actions.*;
import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.user.dsl.Users.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.action.Actions;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.engine.impl.DefaultEngine;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.impl.DefaultLifecycleFactory;
import org.cotrix.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class EngineTest {

	
	static Action editAction = EDIT;
	
	static Action bad = action("badaction");
	static Action editAny = EDIT.cloneFor(Actions.any);
	static Action lockAny = LOCK.cloneFor(Actions.any);

	static User joe = user("joe").can(editAny,lockAny).build();
	
	@Inject
	Engine engine;
	
	@Inject 
	LifecycleService service;
	
	@Test
	public void executeTasksForAllowedActions2() throws Exception {

		User joe = user("joe").can(editAny).build();
		
		Lifecycle lifecycle = new DefaultLifecycleFactory().create("instance");

		LifecycleService service = mock(LifecycleService.class);
		when(service.lifecycleOf(any(String.class))).thenReturn(lifecycle);
		
		Engine engine = new DefaultEngine(joe,service);
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		TaskOutcome<Void> outcome = engine.perform(EDIT).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		assertEquals(joe.permissions(),outcome.nextActions());
		
		
	}
	
	
	@Test
	public void executeTasksForAllowedActions() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		TaskOutcome<Void> outcome = engine.perform(editAction).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		assertEquals(joe.permissions(),outcome.nextActions());
		
		
	}
	
	@Test
	public void executesTasksForLegalLifecycleActions() throws Exception {

		service.start("instance");
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		Action editInstance = editAction.cloneFor("instance");
		
		TaskOutcome<Void> outcome = engine.perform(editInstance).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		assertTrue(outcome.nextActions().contains(EDIT.cloneFor("instance")));
		assertTrue(outcome.nextActions().contains(LOCK.cloneFor("instance")));
		assertFalse(outcome.nextActions().contains(SEAL.cloneFor("instance")));
	}
	
	@Test(expected=IllegalAccessError.class)
	public void failsTasksForDisallowedActions() throws Exception {

		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		engine.perform(bad).with(task);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void failsTasksForIllegalLifecycleActions() throws Exception {

		service.start("instance");
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		engine.perform(bad.cloneFor("instance")).with(task);
		
	}
	
	
	@Test(expected=IllegalAccessError.class)
	public void failsTasksForDisallowedLifecycleActions() throws Exception {

		service.start("instance");
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		engine.perform(SEAL).with(task);
		
	}
	
	@Produces
	public User testuser() {
		
		return joe;
	}
	
	
	
}
