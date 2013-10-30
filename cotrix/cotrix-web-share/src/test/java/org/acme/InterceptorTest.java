package org.acme;

import static org.cotrix.action.CodelistAction.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import junit.framework.Assert;

import org.cotrix.common.cdi.Current;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.cotrix.web.share.server.task.CodelistTask;
import org.cotrix.web.share.server.task.ContainsTask;
import org.cotrix.web.share.server.task.Id;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class InterceptorTest {

	static CountDownLatch latch = new CountDownLatch(1);
	
	@Inject
	Subject subject;
	
	@Inject 
	LifecycleService service;
	
	@Before
	public void setup() {
		
		service.start("1");
	}

	@Test
	public void test() throws Exception {
		
		subject.method("1");
		
		if (!latch.await(100,TimeUnit.MILLISECONDS))
			Assert.fail();
	}
	
	@ContainsTask
	static class Subject {
		
		@CodelistTask(EDIT)
		public void method(@Id String id) {
			System.out.println(this+" is doing it on "+id);
			latch.countDown();
		}
		
	}
	
	@Produces @Current
	static public User user() {
		return PredefinedUsers.cotrix;
	}
}
