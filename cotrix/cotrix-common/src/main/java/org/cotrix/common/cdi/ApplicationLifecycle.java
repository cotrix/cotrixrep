package org.cotrix.common.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.ApplicationEvent;
import org.cotrix.common.cdi.ApplicationEvents.FirstTime;
import org.cotrix.common.cdi.ApplicationEvents.Ready;
import org.cotrix.common.cdi.ApplicationEvents.Restart;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.common.tx.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//fires application lifecycle events

@Singleton
@SuppressWarnings("serial")
public class ApplicationLifecycle {

	private static Logger log = LoggerFactory.getLogger(ApplicationLifecycle.class);
	
	private Event<ApplicationEvent> events;
	
	//assume by default a restart scenario
	private AnnotationLiteral<? extends Annotation> scenario;
	
	@Inject
	public ApplicationLifecycle(Event<ApplicationEvent> events) {
		this.events=events;
	}
	
	//for now, someone needs to explicitly tell us (servlet context listener)
	//(if future CDI versions fire startup events that we can align and avoid explicit callers)
	@Transactional
	public void start() {
		
		scenario = new AnnotationLiteral<FirstTime>() {};
		 
		log.info("Cotrix is starting...");
	
		//wait for application do whatever it needs to do initialise
		events.fire(Startup.INSTANCE);
		
		//by now app is fully initialised and may be staged
		
		log.info("Cotrix is staging...({})",scenario.annotationType().getSimpleName());
		
		//an initialiser may have called us back to let us know if this is a first start
		
		events.select(scenario).fire(Ready.INSTANCE);
	}
	
	
	public void isRestart() {
		
		scenario = new AnnotationLiteral<Restart>() {};
		
	}
	
	//for now, someone needs to explicitly tell us (servlet context listener)
	//(if future CDI versions fire startup events that we can align and avoid explicit callers)
	public void stop() {
		
		log.info("Cotrix is stopping...");
		
		events.fire(Shutdown.INSTANCE);
	}
}
