package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.codelistmanager.shared.CodelistMetadata;
import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.feature.Request;
import org.cotrix.web.share.shared.feature.FeatureCarrier;

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
	
	DataWindow<UICode> getCodelistCodes(String codelistId, Range range) throws ManagerServiceException;
	DataWindow<CodelistGroup> getCodelistsGrouped() throws ManagerServiceException;
	
	CodelistMetadata getMetadata(String codelistId) throws ManagerServiceException;

	FeatureCarrier.Void saveMessage(String message); 
	
	FeatureCarrier.Void lock(Request<Void> request);
	FeatureCarrier.Void unlock(Request<Void> request);
	FeatureCarrier.Void seal(Request<Void> request);
	
	public ModifyCommandResult modify(String codelistId, ModifyCommand command) throws ManagerServiceException;

}
