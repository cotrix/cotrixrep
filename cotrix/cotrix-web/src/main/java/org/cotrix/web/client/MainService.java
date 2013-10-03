package org.cotrix.web.client;

import java.util.ArrayList;

import org.cotrix.web.share.shared.feature.Response;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("main")
public interface MainService extends RemoteService {
	ArrayList<String> getList() throws IllegalArgumentException;
	
	public Response<String> login(String user, String password);
	public Response<Void> logout();
	
}
