package org.cotrix.web.client;

import java.util.List;

import org.cotrix.web.share.shared.exception.IllegalActionException;
import org.cotrix.web.share.shared.exception.ServiceException;
import org.cotrix.web.share.shared.feature.ResponseWrapper;
import org.cotrix.web.shared.UINews;
import org.cotrix.web.shared.UIStatistics;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("main")
public interface MainService extends RemoteService {
	
	//IllegalActionException necessary to make it serializable
	public ResponseWrapper<String> login(String user, String password, List<String> openCodelists) throws ServiceException, IllegalActionException;
	public ResponseWrapper<String> logout(List<String> openCodelists) throws ServiceException;
	public ResponseWrapper<String> registerUser(String username, String password, String email, List<String> openCodelists) throws ServiceException;
	
	public UIStatistics getStatistics() throws ServiceException;
	
	public List<UINews> getNews() throws ServiceException;
	
}
