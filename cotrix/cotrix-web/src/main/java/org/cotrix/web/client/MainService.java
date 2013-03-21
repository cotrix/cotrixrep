package org.cotrix.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("main")
public interface MainService extends RemoteService {
  ArrayList<String> getList() throws IllegalArgumentException;
}
