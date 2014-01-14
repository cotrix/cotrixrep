package org.acme;

import static org.mockito.Mockito.*;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.Constants;
import org.cotrix.common.tx.Transaction;
import org.cotrix.common.tx.Transactional;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
@Priority(Constants.TEST)
public class TransactionTest {

	@Produces @Alternative
	static Transaction tx = mock(Transaction.class);
	
	@Inject
	MethodAnnotatedBean mab;
	
	@Inject
	ClassAnnotatedBean cab;
	
	@Inject
	Event<MyEvent> events;
	
	@After
	public void resetMockAfterEachTest() {
		
		reset(tx);
	}
	
	static class MethodAnnotatedBean {
		
		@Transactional
		void foo() {};
		
		void goo() {};
	}
	
	
	@Transactional
	static class ClassAnnotatedBean {
		
		void foo() {}
		
	}
	
	
	@Test
	public void methodInterception() {
		
		mab.foo();
		
		mab.goo();
		
		verify(tx).commit();
		
	}

	
	@Test
	public void classInterception() {
		
		cab.foo();
		
		verify(tx).commit();
		
	}
	
	
	@Test
	public void consumerInterception() {
		
		events.fire(new MyEvent());
		
		verify(tx).commit();
		
	}
	
	static class MyEvent {}
	
	
	@Singleton
	static class Consumer {
	
		@Transactional
		public void consumer(@Observes MyEvent e) {
			//System.out.println(this.getClass());
		}
	}
}
