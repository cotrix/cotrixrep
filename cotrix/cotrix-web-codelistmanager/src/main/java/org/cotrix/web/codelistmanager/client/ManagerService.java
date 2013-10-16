package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.codelistmanager.shared.CodelistMetadata;
import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.UICodelistRow;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.feature.Request;
import org.cotrix.web.share.shared.feature.Response;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RemoteServiceRelativePath("manager")
public interface ManagerService extends RemoteService {
	
	DataWindow<UICodelistRow> getCodelistRows(String codelistId, Range range) throws ManagerServiceException;
	DataWindow<CodelistGroup> getCodelistsGrouped() throws ManagerServiceException;
	
	CodelistMetadata getMetadata(String codelistId) throws ManagerServiceException;
	void saveMetadata(String codelistId, CodelistMetadata metadata) throws ManagerServiceException;
	
	void saveCodelistRow(String codelistId, UICodelistRow row) throws ManagerServiceException;
	

	Response<Void> saveMessage(String message); 
	
	Response<Void> lock(Request<Void> request);
	Response<Void> unlock(Request<Void> request);
	Response<Void> seal(Request<Void> request);

}
