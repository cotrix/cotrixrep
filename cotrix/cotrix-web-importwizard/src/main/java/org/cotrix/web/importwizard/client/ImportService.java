package org.cotrix.web.importwizard.client;

import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("import")
public interface ImportService extends RemoteService {
	boolean sendToServer(CotrixImportModel model) throws IllegalArgumentException;
	void testBackendConnection() throws IllegalArgumentException;
}