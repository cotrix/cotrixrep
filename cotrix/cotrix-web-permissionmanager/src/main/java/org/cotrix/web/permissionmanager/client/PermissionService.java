package org.cotrix.web.permissionmanager.client;

import java.util.List;
import java.util.Map;

import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.RoleAction;
import org.cotrix.web.permissionmanager.shared.RoleState;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.UIUserDetails;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.UIUser;
import org.cotrix.web.share.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("permissionService")
public interface PermissionService extends RemoteService {
	
	public List<String> getRoles(RolesType type) throws ServiceException;
	
	public DataWindow<RolesRow> getApplicationRolesRows(Range range, ColumnSortInfo sortInfo) throws ServiceException;
	
	public Map<String, RoleState> getUserApplicationRoles() throws ServiceException;
	
	public DataWindow<RolesRow> getCodelistRolesRows(String codelistId, Range range, ColumnSortInfo sortInfo) throws ServiceException;
	
	public RolesRow codelistRoleUpdated(String userId, String codelistId, String role, RoleAction action) throws ServiceException;
	
	public RolesRow applicationRoleUpdated(String userId, String role, RoleAction action) throws ServiceException;
	
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException;
	
	public List<UIUser> getUsers() throws ServiceException;
	
	public UIUserDetails getUserDetails() throws ServiceException;
	
	public void saveUserDetails(UIUserDetails userDetails) throws ServiceException;
}