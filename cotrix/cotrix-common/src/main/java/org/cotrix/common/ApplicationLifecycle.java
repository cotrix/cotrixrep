package org.cotrix.common;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.ApplicationLifecycleEvents.ApplicationEvent;
import org.cotrix.common.events.ApplicationLifecycleEvents.FirstTime;
import org.cotrix.common.events.ApplicationLifecycleEvents.Ready;
import org.cotrix.common.events.ApplicationLifecycleEvents.Restart;
import org.cotrix.common.events.ApplicationLifecycleEvents.Shutdown;
import org.cotrix.common.events.ApplicationLifecycleEvents.Startup;
import org.cotrix.common.tx.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton @SuppressWarnings("serial")
public class ApplicationLifecycle {

	private static Logger log = LoggerFactory.getLogger(ApplicationLifecycle.class);
	
	
	@Inject
	private Event<ApplicationEvent> events;
	
	private AnnotationLiteral<? extends Annotation> scenario;
	
	
	
	//it's a 2-phase affair: 'init' and 'stage', in two possible scenarios: 'fresh' and 'restart'.
	//we notify 'initialisers' some of which may tell us back which scenario we're in.
	//we then notify 'stagers', qualifying with the scenario
	
	@Transactional
	public void start() {
		
		scenario = new AnnotationLiteral<FirstTime>() {}; 
		 
		log.info("application is starting...");
	
		events.fire(Startup.INSTANCE);
		
		log.info("application is staging...({})",scenario.annotationType().getSimpleName().toLowerCase());
		
		events.select(scenario).fire(Ready.INSTANCE);

	}
	
	
	
	public void markAsRestart() {
		
		scenario = new AnnotationLiteral<Restart>() {};
		
	}
	
	
	public void stop() {
		
		log.info("application is stopping...");
		
		events.fire(Shutdown.INSTANCE);
	}
}
