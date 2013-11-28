package org.cotrix.web.permissionmanager.client;

import java.util.List;

import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.RoleAction;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.UIUser;
import org.cotrix.web.permissionmanager.shared.UIUserDetails;
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
	
	public void codelistRoleUpdated(String userId, String codelistId, String role, RoleAction action) throws ServiceException;
	
	public void applicationRoleUpdated(String userId, String role, RoleAction action) throws ServiceException;
	
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException;
	
	public List<UIUser> getUsers() throws ServiceException;
	
	public UIUserDetails getUserDetails() throws ServiceException;
	
	public void saveUserDetails(UIUserDetails userDetails) throws ServiceException;
}