package org.acme;

import static org.cotrix.action.Actions.*;
import static org.cotrix.action.CodelistAction.*;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.common.cdi.Current;
import org.cotrix.engine.Engine;
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
		
		return Users.user("joe").can(EDIT,LOCK,UNLOCK,PUBLISH.on("2")).build(); //but can't seal
	}
	
	@Before
	public void setup() {
		
		service.start("1");
		service.start("2");
	}
	
	@Test
	public void executesWildcardInstanceAction() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		engine.perform(EDIT.on("1")).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		
	}
	
	@Test
	public void executesInstanceAction() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		engine.perform(PUBLISH.on("2")).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		
	}
	

	@Test
	public void executesAction() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				latch.countDown();
			}
		};
		
		engine.perform(EDIT).with(task);
		
		if (latch.getCount()!=0)
			fail();
		
		
	}
	
	@Test
	public void failsDisallowedAction() throws Exception {

		Runnable task = new Runnable() {
			
			@Override
			public void run() {}
		};
		
		try {
			
			engine.perform(action("badaction")).with(task);
			
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
			engine.perform(PUBLISH.on("1")).with(task);
			
			fail();
		}
		catch(IllegalAccessError e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
