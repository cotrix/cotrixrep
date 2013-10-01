package org.cotrix.web.server;

import java.util.ArrayList;

import org.cotrix.web.client.MainService;
import org.cotrix.web.share.shared.feature.Response;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class MainServiceImpl extends RemoteServiceServlet implements MainService {

	public ArrayList<String> getList() throws IllegalArgumentException {

		return new ArrayList<String>();
	}

	@Override
	public Response<String> login(String user, String password) {
		
		return Response.wrap(user);
	}

	@Override
	public Response<Void> logout() {
		return Response.wrap(null);
	}

}
