package org.cotrix.web.codelistmanager.client;

import java.util.ArrayList;

import org.cotrix.web.codelistmanager.shared.ManagerServiceException;
import org.cotrix.web.codelistmanager.shared.UICodeListRow;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.UICode;
import org.cotrix.web.share.shared.UICodelist;

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
	
	
	
	DataWindow<UICodelist> getCodelists(Range range) throws ManagerServiceException;
	DataWindow<UICodeListRow> getCodelistRows(String codelistId, Range range) throws ManagerServiceException;
}
