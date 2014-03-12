package org.cotrix.web.users.client;

import java.util.List;
import java.util.Map;

import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.users.shared.CodelistGroup;
import org.cotrix.web.users.shared.RoleAction;
import org.cotrix.web.users.shared.RoleState;
import org.cotrix.web.users.shared.RolesRow;
import org.cotrix.web.users.shared.RolesType;
import org.cotrix.web.users.shared.UIUserDetails;
import org.cotrix.web.users.shared.InvalidPasswordException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service/permissionService")
public interface PermissionService extends RemoteService {
	
	public List<String> getRoles(RolesType type) throws ServiceException;
	
	public DataWindow<RolesRow> getApplicationRolesRows(Range range, ColumnSortInfo sortInfo) throws ServiceException;
	
	public Map<String, RoleState> getUserApplicationRoles() throws ServiceException;
	
	public DataWindow<RolesRow> getCodelistRolesRows(String codelistId, Range range, ColumnSortInfo sortInfo) throws ServiceException;
	
	public RolesRow codelistRoleUpdated(String userId, String codelistId, String role, RoleAction action) throws ServiceException;
	
	public void codelistRolesRowRemoved(String codelistId, RolesRow row) throws ServiceException;
	
	public RolesRow applicationRoleUpdated(String userId, String role, RoleAction action) throws ServiceException;
	
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException;
	
	public DataWindow<UIUserDetails> getUsersDetails() throws ServiceException;
	
	public UIUserDetails getUserDetails(String userId) throws ServiceException;
	
	public void saveUserDetails(UIUserDetails userDetails) throws ServiceException;
	
	public void updateUserPassword(String userId, String oldPassword, String newPassword) throws ServiceException, InvalidPasswordException;
	
	public void removeUser(String userId) throws ServiceException;
}