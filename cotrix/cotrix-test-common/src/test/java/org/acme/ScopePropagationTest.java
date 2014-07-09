package org.acme;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.common.async.DefaultExecutionService;
import org.cotrix.common.async.TaskManagerProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class ScopePropagationTest {

	@Inject
	DefaultExecutionService service;

	@Inject
	Boo boo;
	
	@Inject
	TaskManagerProvider provider;
	
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
		
		provider.get().started();
		
		final int hash = boo.foo.hashCode();
	
		Callable<?> c = new Callable<Void>() {

			@Override
			public Void call() throws Exception {

				System.out.println(boo.foo.toString());
				
				assertEquals(hash,boo.foo.hashCode());

				return null;
			}

		};

		service.execute(c).get();

		assertEquals(hash,boo.foo.hashCode());

	}

}
