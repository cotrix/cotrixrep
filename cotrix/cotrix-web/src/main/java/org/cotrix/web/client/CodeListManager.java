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
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListManager implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		Log.setUncaughtExceptionHandler();
		
	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        @Override
	        public void execute() {
	        	initialize();
	        }
	      });
	}
	
	protected void initialize()
	{
    	CotrixResources.INSTANCE.css().ensureInjected();
		Window.enableScrolling(true); 
		Window.setMargin("0px");
		 
		AppGinInjector injector = GWT.create(AppGinInjector.class);
		AppController appViewer = injector.getAppController();
		appViewer.go(RootLayoutPanel.get());
	}
}
