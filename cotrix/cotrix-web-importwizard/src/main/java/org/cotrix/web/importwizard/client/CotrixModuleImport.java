package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.resources.CotrixImportResources;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CotrixModuleImport implements EntryPoint {
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
	private final ImportServiceAsync rpcService = GWT
			.create(ImportService.class);

	private final Messages messages = GWT.create(Messages.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		CotrixImportResources.INSTANCE.css().ensureInjected();
		
		
		/*rpcService.testBackendConnection(new AsyncCallback<Void>() {
			
			public void onSuccess(Void result) {
				Window.alert("Success!!!");
			}
			
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});*/
		CotrixImportAppGinInjector injector = GWT.create(CotrixImportAppGinInjector.class);
		CotrixImportAppController appViewer = injector.getAppController();
		appViewer.go(RootPanel.get());
	}
}
