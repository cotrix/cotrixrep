package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommandResult;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UICode;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.ResponseWrapper;

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
	
	UICodelistMetadata getMetadata(String codelistId) throws ManagerServiceException;
	
	CodelistGroup createNewCodelistVersion(String codelistId, String newVersion) throws ManagerServiceException;
	void removeCodelist(String codelistId) throws ManagerServiceException;
	
	ResponseWrapper<String> getCodelistState(String codelistId) throws ManagerServiceException;
	
	FeatureCarrier.Void lock(String codelistId) throws ManagerServiceException;
	FeatureCarrier.Void unlock(String codelistId) throws ManagerServiceException;
	FeatureCarrier.Void seal(String codelistId) throws ManagerServiceException;
	
	public ModifyCommandResult modify(String codelistId, ModifyCommand command) throws ManagerServiceException;

}
