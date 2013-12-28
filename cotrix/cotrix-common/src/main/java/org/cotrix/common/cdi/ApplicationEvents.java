package org.cotrix.common.cdi;

public class ApplicationEvents {

	public static class LifecycleEvent {}
	
	/**
	 * The startup of the application.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Startup extends LifecycleEvent {
		public static final Startup INSTANCE = new Startup();
		private Startup() {}
	}
	
	
	/**
	 * The creation of the application's embedded store.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static interface NewStore {
	}
	
	/**
	 * The shutdown of the application.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Shutdown extends LifecycleEvent {
		public static final Shutdown INSTANCE = new Shutdown();
		private Shutdown() {}

	}
	
}
