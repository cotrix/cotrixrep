package org.cotrix.web.client;

import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.ResponseWrapper;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("main")
public interface MainService extends RemoteService {
	
	public ResponseWrapper<String> login(String user, String password);
	public FeatureCarrier.Void logout();
	
}
