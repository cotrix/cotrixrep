package org.acme;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.action.MainAction;
import org.cotrix.action.Action;
import org.cotrix.common.cdi.Current;
import org.cotrix.engine.Engine;
import org.cotrix.engine.TaskOutcome;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.user.User;
import org.cotrix.user.dsl.Users;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class EngineTest {

	
	@Inject
	Engine engine;
	
	@Inject 
	LifecycleService service;
	
	@Produces @Current
	public User testuser() {
		
		return Users.user("joe").can(IMPORT,EDIT,LOCK.on("2"),UNLOCK).build(); //but can't seal
	}
	
	@Before
	public void setup() {
		
		service.start("1");
		service.start("2");
	}
	
	@Test
	public void executesResourceActionAgainstTemplate() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		TaskOutcome<Void> outcome = engine.perform(EDIT.on("1")).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		Collection<Action> expected = new ArrayList<Action>();
		expected.add(EDIT.on("1"));
		assertEquals(expected,outcome.nextActions());
		
	}
	
	@Test
	public void executesResourceAction() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		engine.perform(LOCK.on("2")).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		
	}
	

	
	@Test
	public void executesGenericAction() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		TaskOutcome<Void> outcome = engine.perform(IMPORT.on(app)).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		Collection<Action> expected = new ArrayList<Action>();
		expected.add(IMPORT.on(app));
		assertEquals(expected,outcome.nextActions());
	}
	
	@Test
	public void failsGenericAction() throws Exception {

		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		try {
			
			engine.perform(MainAction.PUBLISH.on(app)).with(task);
			
			fail();
		}
		catch(IllegalAccessError e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Test
	public void failsIllegalInstanceAction() throws Exception {

		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		try {
			
			engine.perform(UNLOCK.on("1")).with(task);
			
			fail();
		}
		catch(IllegalStateException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	@Test
	public void failsDisallowedInstanceAction() throws Exception {

		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		try {
			engine.perform(LOCK.on("1")).with(task);
			
			fail();
		}
		catch(IllegalAccessError e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
