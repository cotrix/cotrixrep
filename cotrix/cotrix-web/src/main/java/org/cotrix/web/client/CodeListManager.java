package org.cotrix.web.client;

import org.cotrix.web.client.resources.CotrixResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CodeListManager implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final MainServiceAsync codeListService = GWT
			.create(MainService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// Get rid of scrollbars, and clear out the window's built-in margin,
		// because we want to take advantage of the entire client area.

		/*
		 * Window.enableScrolling(false); Window.setMargin("0px");
		 * 
		 * CodeListManagerView codeListManagerView = new CodeListManagerView();
		 * codeListManagerView.setSize("100%", "100%");
		 * 
		 * RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		 * rootLayoutPanel.add(codeListManagerView);
		 */
		
		Log.setUncaughtExceptionHandler();
		
	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	        	CotrixResources.INSTANCE.css().ensureInjected();
	    		Window.enableScrolling(true); Window.setMargin("0px");
	    		 
	    		AppGinInjector injector = GWT.create(AppGinInjector.class);
	    		AppController appViewer = injector.getAppController();
	    		appViewer.go(RootLayoutPanel.get());
	        }
	      });
		
		
	}
}
