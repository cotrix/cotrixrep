package org.cotrix.web.server;

import java.util.ArrayList;

import org.cotrix.web.client.MainService;
import org.cotrix.web.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MainServiceImpl extends RemoteServiceServlet implements
   MainService {

  public ArrayList<String> getList() throws IllegalArgumentException {

    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");

    return new ArrayList<String>();
  }

}
