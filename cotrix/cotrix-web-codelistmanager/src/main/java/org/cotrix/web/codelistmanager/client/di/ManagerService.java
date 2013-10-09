package org.cotrix.web.codelistmanager.client.di;

import java.util.ArrayList;

import org.cotrix.web.codelistmanager.shared.CodeListGroup;
import org.cotrix.web.codelistmanager.shared.CodeListMetadata;
import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.UICode;
import org.cotrix.web.share.shared.UICodelist;
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
	
	ArrayList<UICodelist> getAllCodelists() throws IllegalArgumentException;
	
	CotrixImportModel getCodeListModel(String codelistId);
	
	ArrayList<UICode[]> getDataRange(String id,int start,int end);
	
	void editCode(ArrayList<UICode> code);
	
	DataWindow<UICodeListRow> getCodelistRows(String codelistId, Range range) throws ManagerServiceException;
	DataWindow<CodeListGroup> getCodelistsGrouped() throws ManagerServiceException;
	

	CodeListMetadata getMetadata(String codelistId) throws ManagerServiceException;
	void saveMetadata(String codelistId, CodeListMetadata metadata) throws ManagerServiceException;
	
	void saveCodelistRow(String codelistId, UICodeListRow row) throws ManagerServiceException;
	

	Response<Void> saveMessage(String message); 
	
	Response<Void> lock(Request<Void> request);
	Response<Void> unlock(Request<Void> request);
	Response<Void> seal(Request<Void> request);

}
