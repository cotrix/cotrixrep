package org.acme;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.async.DefaultExecutionService;
import org.cotrix.common.async.TaskEvents.StartTask;
import org.cotrix.common.async.TaskEvents.StartTask.InfoProvider;
import org.cotrix.common.events.Current;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class ScopePropagationTest {

	@Inject
	DefaultExecutionService service;

	@Inject
	Boo boo;
	
	@Inject @Current
	InfoProvider info;

	@Inject
	private Event<Object> event;
	
	@SessionScoped
	@SuppressWarnings("serial")
	static class Foo implements Serializable {
	}

	static class Boo {

		@Inject
		Foo foo;

	}

	@Test
	public void propagate() throws Exception {
		
		event.fire(new StartTask(info.get()));
		
		// access context
		System.out.println(boo.foo);

		Callable<?> c = new Callable<Void>() {

			@Override
			public Void call() throws Exception {

				System.out.println(boo.foo);

				return null;
			}

		};

		service.execute(c).get();

		System.out.println(boo.foo);

	}

}
