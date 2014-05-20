package org.cotrix.common.cdi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

public class ApplicationEvents {

	public static class ApplicationEvent {
	}

	/**
	 * The startup of the application.
	 * 
	 * @author Fabio Simeoni
	 * 
	 */
	public static class Startup extends ApplicationEvent {
		public static final Startup INSTANCE = new Startup();

		private Startup() {
		}
	}

	/**
	 * The completion of startup processes.
	 * 
	 * @author Fabio Simeoni
	 * 
	 */
	public static class Ready extends ApplicationEvent {
		public static final Ready INSTANCE = new Ready();

		private Ready() {
		}
	}

	@Target({ PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	/**
	 * A CDI qualifier for beans produced in session or request scope.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public @interface FirstTime {
	}

	@Target({ PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@Qualifier
	/**
	 * A CDI qualifier for beans produced in session or request scope.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public @interface Restart {
	}

	/**
	 * The shutdown of the application.
	 * 
	 * @author Fabio Simeoni
	 * 
	 */
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
