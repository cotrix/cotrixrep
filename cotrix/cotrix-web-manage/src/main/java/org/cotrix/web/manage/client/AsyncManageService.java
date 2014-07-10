package org.cotrix.web.manage.client;

import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier.Void;

/**
 * The client side stub for the RPC service.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("service/asyncManageService")
public interface AsyncManageService extends RemoteService {
	
	AsyncOutput<Void> testAsync(String input) throws ServiceException;
	
	AsyncOutput<Void> removeCodelist(String codelistId) throws ServiceException;

}
