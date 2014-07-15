package org.acme;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.common.async.DefaultExecutionService;
import org.cotrix.common.async.TaskManagerProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
@SuppressWarnings("serial")
public class ScopePropagationTest {

	@Inject
	DefaultExecutionService service;

	@Inject
	Boo boo;
	
	@Inject
	TaskManagerProvider provider;
	
	@SessionScoped

	static class Foo implements Serializable {
	}

	static class Boo {

		@Inject
		Foo foo;

		@Inject
		Goo goo;
	}
	
	@RequestScoped
	static class Goo implements Serializable  {
		
	}

	@Test
	public void propagate() throws Exception {
		
		provider.get().started();
		
		System.out.println(boo.foo.toString());
		System.out.println(boo.goo.toString());
		
		final int hash = boo.foo.hashCode();
	
		Callable<?> c = new Callable<Void>() {

			@Override
			public Void call() throws Exception {

				System.out.println(boo.foo.toString());
				System.out.println(boo.goo.toString());
				
				assertEquals(hash,boo.foo.hashCode());

				return null;
			}

		};

		service.execute(c).get();

		System.out.println(boo.foo.toString());
		System.out.println(boo.goo.toString());
		
		assertEquals(hash,boo.foo.hashCode());

	}

}
