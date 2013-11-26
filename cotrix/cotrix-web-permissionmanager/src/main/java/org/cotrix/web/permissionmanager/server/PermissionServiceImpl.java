/**
 * 
 */
package org.cotrix.web.permissionmanager.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.acme.FingerprintTest;
import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.repository.user.UserRepository;
import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.server.util.RolesSorter;
import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.UIUser;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.UserLoader;
import org.cotrix.web.share.server.util.ValueUtils;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import static org.cotrix.repository.user.UserQueries.*;
import static org.cotrix.repository.codelist.CodelistQueries.*;
import static org.cotrix.domain.dsl.Users.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PermissionServiceImpl extends RemoteServiceServlet implements PermissionService {

	private static final long serialVersionUID = 2621331963385532979L;
	
	
	protected Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Inject
	CodelistRepository repository;
	
	@Inject
	protected CodelistLoader codelistLoader;
	
	@Inject
	protected UserLoader userLoader;
	
	@Inject
	protected UserRepository userRepository;
	
	@Inject
	protected RolesSorter rolesSorter;
	
	@Current
	@Inject
	protected User user;
	
	public void init() {
		codelistLoader.importAllCodelist();
		logger.trace("codelist in repository:");
		for (Codelist codelist:repository.get(allLists())) logger.trace(codelist.name().toString());
		logger.trace("done");
		userLoader.loadUsers();
	}

	@Override
	public List<String> getRoles(RolesType type) throws ServiceException {
		logger.trace("getRoles type {}", type);
		switch (type) {
			case APPLICATION: return toRoles(Roles.getBy(ResourceType.application));
			case CODELISTS: return  toRoles(Roles.getBy(ResourceType.codelists));
		}
		return new ArrayList<String>();
	}
	
	protected List<String> toRoles(Collection<Role> roles) {
		List<String> uiRoles = new ArrayList<String>(roles.size());
		for (Role role:roles) uiRoles.add(role.name());
		return uiRoles;
	}

	@Override
	public DataWindow<RolesRow> getApplicationRolesRows() throws ServiceException {
		logger.trace("getApplicationRolesRows");
		List<RolesRow> rows = new ArrayList<RolesRow>();
		for (User user:userRepository.get(allUsers())) {
			RolesRow row = getApplicationRolesRow(user);
			rows.add(row);
		}
		Collections.sort(rows, rolesSorter);
		return new DataWindow<RolesRow>(rows);

	}
	
	protected RolesRow getApplicationRolesRow(User user) {
		FingerPrint fp = user.fingerprint();
		Collection<String> userRoles = fp.rolesOver(Action.any, ResourceType.application);
		RolesRow row = new RolesRow(toUiUser(user), getRoles(userRoles));
		return row;
	}

	@Override
	public DataWindow<RolesRow> getCodelistRolesRows(String codelistId)	throws ServiceException {
		logger.trace("getCodelistRolesRows codelistId {}", codelistId);
		List<RolesRow> rows = new ArrayList<RolesRow>();
		for (User user:userRepository.get(allUsers().with(roleOn(codelistId, ResourceType.codelists)))) {
			RolesRow row = getCodelistRolesRow(user, codelistId);
			if (row!=null) rows.add(row);
		}
		Collections.sort(rows, rolesSorter);
		return new DataWindow<RolesRow>(rows);

	}
	
	protected RolesRow getCodelistRolesRow(User user, String codelistId) {
		FingerPrint fp = user.fingerprint();
		Collection<String> userRoles = fp.rolesOver(codelistId, ResourceType.codelists);		
		RolesRow row = new RolesRow(toUiUser(user), getRoles(userRoles));
		return row;
	}

	@Override
	public void codelistRolesRowUpdated(String codelistId, RolesRow row) {
		logger.trace("codelistRolesRowUpdated codelistId {} row {}", codelistId, row);
		String userId = row.getUser().getId();
		User user = userRepository.lookup(userId);
		user(userId).can(user.permissions()).is(toRoles(row.getRoles()));
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
	public void applicationRolesRowUpdated(RolesRow row) {
		logger.trace("applicationRolesRowUpdated row {}", row);
	}

	@Override
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException {
		logger.trace("getCodelistGroups");

		Map<QName, CodelistGroup> groups = new HashMap<QName, CodelistGroup>();

		/*FingerPrint fp = user.fingerprint();
		
		for (String codelistId:fp.resources(ResourceType.codelists)) {

			logger.trace("checking codelist "+codelistId);
			
			if (fp.rolesOver(codelistId, ResourceType.codelists).isEmpty()) continue;
			
			Codelist codelist = repository.lookup(codelistId);*/
		
		for (Codelist codelist:repository.get(allLists())) {
			
			CodelistGroup group = groups.get(codelist.name());
			if (group == null) {
				group = new CodelistGroup(codelist.name().toString());
				groups.put(codelist.name(), group);
			}
			List<String> roles = getRoles(user.fingerprint().rolesOver(codelist.id(), ResourceType.codelists));
			group.addVersion(codelist.id(), ValueUtils.safeValue(codelist.version()), roles);
		}
		
		for (CodelistGroup group:groups.values()) Collections.sort(group.getVersions()); 
		
		return new DataWindow<CodelistGroup>(new ArrayList<CodelistGroup>(groups.values()));
	}

	@Override
	public List<UIUser> getUsers() throws ServiceException {
		logger.trace("getUsers");
		List<UIUser> users = new ArrayList<UIUser>();
		
		for (User user:userRepository.get(allUsers())) {
			users.add(toUiUser(user));
		}
		return users;
	}
	
	protected UIUser toUiUser(User user) {
		return new UIUser(user.id(), user.fullName());
	}
	
	protected List<String> getRoles(Collection<String> roles) {
		if (roles == null) return null;
		return new ArrayList<String>(roles);
	}

}
