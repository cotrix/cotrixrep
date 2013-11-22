package org.cotrix.web.permissionmanager.client;

import java.util.List;

import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.User;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("permissionService")
public interface PermissionService extends RemoteService {
	
	public List<String> getRoles(RolesType type) throws ServiceException;
	
	public DataWindow<RolesRow> getApplicationRolesRows() throws ServiceException;
	
	public DataWindow<RolesRow> getCodelistRolesRows(String codelistId) throws ServiceException;
	
	public void codelistRolesRowUpdated(String codelistId, RolesRow row) throws ServiceException;
	
	public void applicationRolesRowUpdated(RolesRow row) throws ServiceException;
	
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException;
	
	public List<User> getUsers() throws ServiceException;
}