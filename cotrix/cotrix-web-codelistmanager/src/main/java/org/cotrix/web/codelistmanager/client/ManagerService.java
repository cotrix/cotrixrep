package org.cotrix.web.codelistmanager.client;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.codelistmanager.shared.CodeCell;
import org.cotrix.web.share.shared.UICodelist;
import org.cotrix.web.share.shared.CotrixImportModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("manager")
public interface ManagerService extends RemoteService {
  ArrayList<UICodelist> getAllCodelists() throws IllegalArgumentException;
  CotrixImportModel getCodeListModel(String codelistId);
  ArrayList<CodeCell[]> getDataRange(String id,int start,int end);
  void editCode(CodeCell codeCell);
}
