package org.cotrix.web.codelistmanager.server;

import org.cotrix.web.codelistmanager.client.CotrixModuleManager;
import org.cotrix.web.codelistmanager.client.ManagerService;
import org.cotrix.web.codelistmanager.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
  public String greetServer(String input)   {
	  return "xxx";
  }
}
