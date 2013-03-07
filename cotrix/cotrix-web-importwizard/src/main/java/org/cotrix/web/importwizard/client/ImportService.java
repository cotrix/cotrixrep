package org.cotrix.web.importwizard.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("import")
public interface ImportService extends RemoteService {
	boolean sendToServer(String model) throws IllegalArgumentException;
}