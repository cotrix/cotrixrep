package org.cotrix.web.client;

import org.cotrix.web.client.resources.CotrixResources;
import org.cotrix.web.common.client.ext.CotrixExtension;
import org.cotrix.web.common.client.ext.CotrixExtensionProvider;

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
	
	protected CotrixExtensionProvider extensionProvider = GWT.create(CotrixExtensionProvider.class);

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
		
		activateExtensions();
		

		 
		CotrixGinInjector injector = GWT.create(CotrixGinInjector.class);
		
		CotrixResources resources = injector.getCotrixResources();
		resources.css().ensureInjected();

		CotrixController appController = injector.getAppController();
		
		hideLoader();
		appController.go(RootLayoutPanel.get());
	}
	
	
	protected void activateExtensions() {
		Log.trace("Activating extensions");
		for (CotrixExtension extension:extensionProvider.getExtensions()) {
			Log.trace("Activating extension "+extension.getName());
			extension.activate();
		}
		Log.trace("done.");
	}
	
	protected void hideLoader() {
		RootPanel loader = RootPanel.get("cotrixLoader");
		if (loader!=null) loader.setVisible(false);
	}
}
