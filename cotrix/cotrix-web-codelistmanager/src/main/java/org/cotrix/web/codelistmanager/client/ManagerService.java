package org.cotrix.web.codelistmanager.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("manager")
public interface ManagerService extends RemoteService {
  String greetServer(String name) throws IllegalArgumentException;
}
