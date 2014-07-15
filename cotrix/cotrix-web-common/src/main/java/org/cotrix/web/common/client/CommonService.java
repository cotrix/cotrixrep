/**
 * 
 */
package org.cotrix.web.common.client;

import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("service/common")
public interface CommonService extends RemoteService  {
	
	public LongTaskProgress getProgress(String progressToken) throws ServiceException;
	
	public boolean cancel(String progressToken) throws ServiceException;

}
