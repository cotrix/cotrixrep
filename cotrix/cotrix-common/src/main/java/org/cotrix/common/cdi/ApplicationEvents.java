package org.cotrix.common.cdi;

public class ApplicationEvents {

	public static class ApplicationEvent {}
	/**
	 * The startup of the application.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Startup extends ApplicationEvent {
		public static final Startup INSTANCE = new Startup();
		private Startup() {}
	}
	
	/**
	 * The shutdown of the application.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Shutdown extends ApplicationEvent {
		public static final Shutdown INSTANCE = new Shutdown();
		private Shutdown() {}

	}
	
}
