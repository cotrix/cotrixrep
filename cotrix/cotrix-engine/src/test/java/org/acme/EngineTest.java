package org.acme;

import static java.util.Arrays.*;
import static org.cotrix.action.Actions.*;
import static org.cotrix.action.CodelistActions.*;
import static org.cotrix.user.dsl.Users.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.action.Action;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class EngineTest {

	
	static Action editAction = edit;
	
	static Action bad = action("badaction");
	static Action editAny = edit.cloneFor(any);
	static Action lockAny = lock.cloneFor(any);

	static User joe = user("joe").can(editAny,lockAny).build();;
	
	@Inject
	Engine engine;
	
	@Inject 
	LifecycleService service;
	
	
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
		
		assertTrue(outcome.nextActions().contains(edit.cloneFor("instance")));
		assertTrue(outcome.nextActions().contains(lock.cloneFor("instance")));
		assertFalse(outcome.nextActions().contains(seal.cloneFor("instance")));
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
		
		engine.perform(seal).with(task);
		
	}
	
	@Produces
	public User testuser() {
		
		return joe;
	}
	
}
