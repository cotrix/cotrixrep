package org.acme;

import static org.mockito.Mockito.*;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.ApplicationLifecycle;
import org.cotrix.common.events.ApplicationLifecycleEvents.ApplicationEvent;
import org.cotrix.common.events.ApplicationLifecycleEvents.FirstTime;
import org.cotrix.common.events.ApplicationLifecycleEvents.Ready;
import org.cotrix.common.events.ApplicationLifecycleEvents.Restart;
import org.cotrix.common.events.ApplicationLifecycleEvents.Shutdown;
import org.cotrix.common.events.ApplicationLifecycleEvents.Startup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class ApplicationLifecycleTest {

	@Inject
	ApplicationLifecycle lc;
	
	@Inject
	Consumer consumer;
	
	@Inject
	Event<ApplicationEvent> events;
	
	@Test
	public void startAndStop() {

		Consumer mock = mock(Consumer.class);
		
		consumer.delegate = mock;
		
		lc.start();
		
		verify(mock).onStart(Startup.INSTANCE);
		
		verify(mock).onFirstStart(Ready.INSTANCE);
		
		lc.stop();
		
		verify(mock).onShutdown(Shutdown.INSTANCE);
	}
	
	@Test
	public void reStart() {

		Consumer mock = mock(Consumer.class);
		
		Answer<Void> answer = new Answer<Void>() {
				
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					lc.markAsRestart();
					return null;
				}
		};
		
		doAnswer(answer).when(mock).onStart(Startup.INSTANCE);
		
		consumer.delegate = mock;
		
		lc.start();
		
		verify(mock).onRestart(Ready.INSTANCE);
		
	}
	
	
	@Singleton
	static class Consumer {
	
		Consumer delegate;
		
		public void onStart(@Observes Startup e){
			delegate.onStart(e);
		}
		
		public void onShutdown(@Observes Shutdown e){
			delegate.onShutdown(e);
		}
		
		public void onFirstStart(@Observes @FirstTime Ready e){
			delegate.onFirstStart(e);
		};
		
		public void onRestart(@Observes @Restart Ready e){
			delegate.onRestart(e);
		};
	}
	

}
