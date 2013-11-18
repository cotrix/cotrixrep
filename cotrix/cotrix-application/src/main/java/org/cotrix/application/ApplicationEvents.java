package org.cotrix.application;

public class ApplicationEvents {

	/**
	 * The startup of the application.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Startup {
		public static final Startup INSTANCE = new Startup();
		private Startup() {}
	}
	
	/**
	 * The shutdown of the application.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public static class Shutdown {
		public static final Shutdown INSTANCE = new Shutdown();
		private Shutdown() {}

	}
	
}
