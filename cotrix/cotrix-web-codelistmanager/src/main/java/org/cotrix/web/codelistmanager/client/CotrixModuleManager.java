package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CotrixModuleManager implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ManagerServiceAsync rpcService = GWT.create(ManagerService.class);
	private final Messages messages = GWT.create(Messages.class);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		/*CotrixManagerResources.INSTANCE.css().ensureInjected();

		CotrixManagerAppGinInjector injector = GWT.create(CotrixManagerAppGinInjector.class);
		CotrixManagerAppController appViewer = injector.getAppController();
		appViewer.go(RootLayoutPanel.get());*/
	}
}
