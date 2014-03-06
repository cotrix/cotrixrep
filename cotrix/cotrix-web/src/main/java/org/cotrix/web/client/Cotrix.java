package org.cotrix.web.client;

import org.cotrix.web.client.resources.CotrixResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Cotrix implements EntryPoint {

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
		Window.enableScrolling(true); 
		Window.setMargin("0px");
		
		hideLoader();
		 
		CotrixGinInjector injector = GWT.create(CotrixGinInjector.class);
		
		CotrixResources resources = injector.getCotrixResources();
		resources.css().ensureInjected();
		
		CotrixController appController = injector.getAppController();
		appController.go(RootLayoutPanel.get());
	}
	
	protected void hideLoader() {
		RootPanel loader = RootPanel.get("cotrixLoader");
		if (loader!=null) loader.setVisible(false);
	}
}
