/**
 * 
 */
package org.cotrix.web.users.server;

import static org.cotrix.action.MainAction.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.CodelistQueries.*;
import static org.cotrix.repository.UserQueries.*;
import static org.cotrix.web.users.shared.PermissionUIFeatures.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.action.UserAction;
import org.cotrix.application.PermissionDelegationService;
import org.cotrix.common.events.Current;
import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.Criterion;
import org.cotrix.repository.UserQueries;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.InvalidCredentialsException;
import org.cotrix.security.SignupService;
import org.cotrix.web.common.server.CotrixRemoteServlet;
import org.cotrix.web.common.server.task.ActionMapper;
import org.cotrix.web.common.server.task.ContainsTask;
import org.cotrix.web.common.server.task.Id;
import org.cotrix.web.common.server.task.UserTask;
import org.cotrix.web.common.server.util.Users;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.users.client.UsersService;
import org.cotrix.web.users.server.util.RolesSorter;
import org.cotrix.web.users.shared.CodelistGroup;
import org.cotrix.web.users.shared.InvalidPasswordException;
import org.cotrix.web.users.shared.RoleAction;
import org.cotrix.web.users.shared.RoleState;
import org.cotrix.web.users.shared.RolesRow;
import org.cotrix.web.users.shared.RolesType;
import org.cotrix.web.users.shared.UIUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.view.client.Range;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
@ContainsTask
public class UsersServiceImpl implements UsersService {

	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected UsersServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}

	protected Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

	@Inject
	ActionMapper mapper;

	@Inject
	protected CodelistRepository codelistRepository;

	@Inject
	protected UserRepository userRepository;

	@Inject
	protected RolesSorter rolesSorter;

	@Inject
	protected PermissionDelegationService delegationService;
	
	@Inject
	protected SignupService signupService;

	@Current
	@Inject
	protected User currentUser;

	@PostConstruct
	protected void init() {

		mapper.map(MANAGE_USERS).to(EDIT_USERS_ROLES);
		mapper.map(UserAction.EDIT).to(EDIT_PROFILE);
		mapper.map(UserAction.CHANGE_PASSWORD).to(CHANGE_PASSWORD);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRoles(RolesType type) throws ServiceException {
		logger.trace("getRoles type {}", type);
		switch (type) {
			case APPLICATION: return toRoles(Roles.getBy(ResourceType.application), Roles.getBy(ResourceType.codelists));
			case CODELISTS: return  toRoles(Roles.getBy(ResourceType.codelists));
		}
		return new ArrayList<String>();
	}


	@Override
	public DataWindow<RolesRow> getApplicationRolesRows(Range range, ColumnSortInfo sortInfo) throws ServiceException {
		logger.trace("getApplicationRolesRows range: {}, sortInfo: {}", range, sortInfo);
		List<RolesRow> rows = new ArrayList<RolesRow>();

		logger.trace("current user: "+currentUser);
		
		Criterion<User> sortCriterion = byName();
		if (!sortInfo.isAscending()) sortCriterion = UserQueries.<User>descending(sortCriterion);

		for (User user:userRepository.get(allUsers().sort(sortCriterion).excluding(currentUser.id()).from(range.getStart()).to(range.getLength()))) {
			RolesRow row = getRow(user, Action.any, Roles.getBy(ResourceType.application, ResourceType.codelists));
			rows.add(row);
		}

		rolesSorter.syncUser();
		Collections.sort(rows, rolesSorter);
		//logger.trace("returning {} rows", rows.size());
		return new DataWindow<RolesRow>(rows);
	}

	protected RolesRow getRow(User user, String instance, Iterable<Role> roles) {

		RolesRow row = new RolesRow(Users.toUiUser(user), getRolesStates(user, instance, roles));
		return row;
	}

	protected Map<String, RoleState> getRolesStates(User user, String instance, Iterable<Role> roles) {
		Map<String, RoleState> rolesStates = new HashMap<String, RoleState>();
		for (Role role:roles) {
			RoleState state = getRoleState(user, role.on(instance));
			rolesStates.put(role.name(), state);
		}
		return rolesStates;
	}

	protected RoleState getRoleState(User user, Role role) {
		boolean delegable = currentUser.is(role);  //role flags
		boolean active =  user.is(role);
		boolean direct =  user.isDirectly(role);

		boolean tick = active;
		boolean enable = delegable && (!active || direct);
		//logger.trace("role: {} active: {} direct: {} user: {}", role, active, direct, user);
		return new RoleState(enable, tick, false);
	}

	@Override
	public Map<String, RoleState> getUserApplicationRoles() throws ServiceException {
		logger.trace("getUserApplicationRoles");
		return getRolesStates(currentUser, Action.any, Roles.getBy(ResourceType.application, ResourceType.codelists));
	}

	@Override
	public DataWindow<RolesRow> getCodelistRolesRows(String codelistId, Range range, ColumnSortInfo sortInfo)	throws ServiceException {
		logger.trace("getCodelistRolesRows codelistId {} range: {}, sortInfo: {}", codelistId, range, sortInfo);

		List<RolesRow> rows = new ArrayList<RolesRow>();
		
		Criterion<User> sortCriterion = byName();
		if (!sortInfo.isAscending()) sortCriterion = UserQueries.<User>descending(sortCriterion);

		for (User user:userRepository.get(teamFor(codelistId).sort(sortCriterion).excluding(currentUser.id()).from(range.getStart()).to(range.getLength()))) {

			RolesRow row = getRow(user, codelistId, Roles.getBy(ResourceType.codelists));
			rows.add(row);
		}
		rolesSorter.syncUser();
		Collections.sort(rows, rolesSorter);
		//logger.trace("returning {} rows", rows.size());
		return new DataWindow<RolesRow>(rows);

	}

	@Override
	public RolesRow codelistRoleUpdated(String userId, String codelistId, String roleName, RoleAction action) {
		logger.trace("codelistRoleUpdated userId: {} codelistId: {} role: {} action: {}", userId, codelistId, roleName, action);

		User target = userRepository.lookup(userId);

		if (roleName!=null) {
			Role role = toRole(roleName).on(codelistId);

			switch (action) {
				case DELEGATE: delegationService.delegate(role).to(target); break;
				case REVOKE: delegationService.revoke(role).from(target); break;
			}
		}
		RolesRow row = getRow(target, codelistId, Roles.getBy(ResourceType.codelists));
		logger.trace("row: {}", row);
		return row;
	}
	

	@Override
	public void codelistRolesRowRemoved(String codelistId, RolesRow row) throws ServiceException {
		logger.trace("codelistRolesRowRemoved codelistId: {} row: {}", codelistId, row);

		User target = userRepository.lookup(row.getUser().getId());
		for (Entry<String, RoleState> roleEntry:row.getRoles().entrySet()) {
			if (roleEntry.getValue().isEnabled() && roleEntry.getValue().isChecked()) {
				Role role = toRole(roleEntry.getKey()).on(codelistId);
				delegationService.revoke(role).from(target);
			}
		}
	}

	@Override
	public RolesRow applicationRoleUpdated(String userId, String roleName, RoleAction action) {
		logger.trace("applicationRoleUpdated userId: {} role: {} action: {}", userId, roleName, action);
		User target = userRepository.lookup(userId);
		Role role = toRole(roleName);

		switch (action) {
			case DELEGATE: delegationService.delegate(role).to(target); break;
			case REVOKE: delegationService.revoke(role).from(target); break;
		}
		
		if (action == RoleAction.REVOKE && userRepository.lookup(userId) == null) return RolesRow.DELETED;
		
		RolesRow row = getRow(target, Action.any, Roles.getBy(ResourceType.application, ResourceType.codelists));
		logger.trace("row: {}", row);
		return row;
	}

	@Override
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException {
		logger.trace("getCodelistGroups");

		Map<QName, CodelistGroup> groups = new HashMap<QName, CodelistGroup>();

		FingerPrint fp = currentUser.fingerprint();

		for (CodelistCoordinates codelist:codelistRepository.get(codelistsFor(currentUser))) {

			CodelistGroup group = groups.get(codelist.qname());
			if (group == null) {
				group = new CodelistGroup(codelist.qname().toString());
				groups.put(codelist.qname(), group);
			}
			List<String> roles = getRoles(fp.allRolesOver(codelist.id(), ResourceType.codelists));
			group.addVersion(codelist.id(), ValueUtils.safeValue(codelist.version()), roles);
		}

		for (CodelistGroup group:groups.values()) Collections.sort(group.getVersions()); 

		return new DataWindow<CodelistGroup>(new ArrayList<CodelistGroup>(groups.values()));
	}

	@Override
	public DataWindow<UIUserDetails> getUsersDetails() throws ServiceException {
		logger.trace("getUsersDetails");
		List<UIUserDetails> users = new ArrayList<UIUserDetails>();

		for (User user:userRepository.get(allUsers().sort(byName()))) {
			users.add(toUserDetails(user));
		}
		return new DataWindow<UIUserDetails>(users);
	}

	@Override
	@UserTask(UserAction.VIEW)
	public UIUserDetails getUserDetails(@Id String userId) throws ServiceException {
		logger.trace("getUserDetails");

		User user = userRepository.lookup(userId);
		UIUserDetails userDetails = toUserDetails(user);
		return userDetails;
	}
	
	protected UIUserDetails toUserDetails(User user) {
		UIUserDetails userDetails = new UIUserDetails();
		userDetails.setId(user.id());
		userDetails.setFullName(user.fullName());
		userDetails.setUsername(user.name());
		userDetails.setEmail(user.email());
		return userDetails;
	}

	@Override
	@UserTask(UserAction.EDIT)
	public void saveUserDetails(UIUserDetails userDetails) throws ServiceException {
		logger.trace("saveUserDetails userDetails: {}", userDetails);
		User changeSet = modifyUser(currentUser).fullName(userDetails.getFullName()).email(userDetails.getEmail()).build();
		userRepository.update(changeSet);
	}
	
	@Override
	@UserTask(UserAction.EDIT)
	public void updateUserPassword(@Id String userId, String oldPassword, String newPassword) throws ServiceException {
		logger.trace("updateUserPassword userId: "+userId);
		try {
			User user = userRepository.lookup(userId);
			signupService.changePassword(user, oldPassword, newPassword);
		} catch(InvalidCredentialsException e) {
			throw new InvalidPasswordException(e.getMessage());
		}
	}
	
	protected UIUserDetails toUiUserDetails(User user) {
		UIUserDetails userDetails = new UIUserDetails();
		userDetails.setId(currentUser.id());
		userDetails.setFullName(currentUser.fullName());
		userDetails.setUsername(currentUser.name());
		userDetails.setEmail(currentUser.email());
		return userDetails;
	}

	@SuppressWarnings("unchecked")
	protected List<String> getRoles(Collection<String> ... rolesSet) {
		List<String> uiroles = new ArrayList<String>();
		for (Collection<String> roles:rolesSet) uiroles.addAll(roles);
		return uiroles;
	}

	protected List<String> getRoles(Collection<String> roles) {
		if (roles == null) return null;
		return new ArrayList<String>(roles);
	}


	@SuppressWarnings("unchecked")
	protected List<String> toRoles(Collection<Role> ... rolesSets) {
		List<String> uiRoles = new ArrayList<String>();
		for (Collection<Role> roles: rolesSets) for (Role role:roles) uiRoles.add(role.name());
		return uiRoles;
	}

	protected List<String> toRoles(Collection<Role> roles) {
		List<String> uiRoles = new ArrayList<String>(roles.size());
		for (Role role:roles) uiRoles.add(role.name());
		return uiRoles;
	}

	protected Role[] toRoles(List<String> uiRoles) {
		Role[] roles = new Role[uiRoles.size()];
		for (int i = 0; i < roles.length; i++) {
			roles[i] = toRole(uiRoles.get(i));
		}
		return roles;
	}

	protected Role toRole(String name) {
		for (Role role:Roles.predefinedRoles) {
			if (role.name().equals(name)) return role;
		}
		throw new IllegalArgumentException("Unknown role name "+name);
	}

	@Override
	public void removeUser(String userId) throws ServiceException {
		logger.trace("removeUser userId: {}", userId);
	}
}
