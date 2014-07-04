package org.cotrix.common.events;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

public class ApplicationLifecycleEvents {

	public static class ApplicationEvent {
	}

	public static class Startup extends ApplicationEvent {
		public static final Startup INSTANCE = new Startup();

		private Startup() {
		}
	}

	public static class Ready extends ApplicationEvent {
		public static final Ready INSTANCE = new Ready();

		private Ready() {
		}
	}

	@Target({ PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	public @interface FirstTime {
	}

	@Target({ PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	public @interface Restart {
	}

	public static class Shutdown extends ApplicationEvent {
		
		public static final Shutdown INSTANCE = new Shutdown();

		private Shutdown() {
		}

	}
	
	public static class StartRequest extends ApplicationEvent {
		
		public static final StartRequest INSTANCE = new StartRequest();

		private StartRequest() {
		}

	}
	
	public static class EndRequest extends ApplicationEvent {
		
		public static final EndRequest INSTANCE = new EndRequest();

		private EndRequest() {
		}

	}

}
