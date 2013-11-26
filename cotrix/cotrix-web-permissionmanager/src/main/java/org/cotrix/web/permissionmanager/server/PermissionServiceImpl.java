/**
 * 
 */
package org.cotrix.web.permissionmanager.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.FingerPrint;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.repository.user.UserRepository;
import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.UIUser;
import org.cotrix.web.permissionmanager.shared.CodelistGroup.Version;
import org.cotrix.web.share.server.util.CodelistLoader;
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
	
	protected static List<UIUser> USERS = asList(
			new UIUser("1", "Federico De Faveri"),
			new UIUser("2", "Fabio Simeoni"),
			new UIUser("3", "Anton Ellenbroek"),
			new UIUser("4", "Aureliano Gentile"),
			new UIUser("5", "Erik Van Ingen")	
			);
	
	protected static List<String> CODELIST_ROLES = asList("VIEWER", "EDITOR", "REVIEWER", "PUBLISHER");
	protected static List<String> APPLICATION_ROLES = asList("PUBLISH", "IMPORT", "ROOT");
	protected static Map<String, List<RolesRow>> CODELIST_ROWS = new HashMap<String, List<RolesRow>>();
	
	static {
	
		CODELIST_ROWS.put("1", asList(
			new RolesRow(new UIUser("1", "Federico De Faveri"), subList(CODELIST_ROLES, 0, 2)),
			new RolesRow(new UIUser("2", "Fabio Simeoni"), subList(CODELIST_ROLES, 0, 2)),
			new RolesRow(new UIUser("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 2, 4)),
			new RolesRow(new UIUser("4", "Aureliano Gentile"), subList(CODELIST_ROLES, 1, 2)),
			new RolesRow(new UIUser("5", "Erik Van Ingen"), subList(CODELIST_ROLES, 2, 3))			
			));
	
		CODELIST_ROWS.put("2", asList(
			new RolesRow(new UIUser("1", "Federico De Faveri"), subList(CODELIST_ROLES, 0, 3)),
			new RolesRow(new UIUser("2", "Fabio Simeoni"), subList(CODELIST_ROLES, 0, 3)),
			new RolesRow(new UIUser("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 2, 4))	
			));
	
		CODELIST_ROWS.put("3", asList(
			new RolesRow(new UIUser("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 0, 4)),
			new RolesRow(new UIUser("4", "Aureliano Gentile"), subList(CODELIST_ROLES, 1, 3))		
			));
	
		CODELIST_ROWS.put("4", asList(
			new RolesRow(new UIUser("1", "Federico De Faveri"), subList(CODELIST_ROLES, 0, 4)),
			new RolesRow(new UIUser("2", "Fabio Simeoni"), subList(CODELIST_ROLES, 0, 2))		
			));
	
		CODELIST_ROWS.put("5", asList(
			new RolesRow(new UIUser("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 0, 4))
			));
	}
	
	protected static List<RolesRow> APPLICATION_ROWS = asList(
			new RolesRow(new UIUser("1", "Federico De Faveri"), subList(APPLICATION_ROLES, 0, 2)),
			new RolesRow(new UIUser("2", "Fabio Simeoni"), subList(APPLICATION_ROLES, 0, 2)),
			new RolesRow(new UIUser("3", "Anton Ellenbroek"), subList(APPLICATION_ROLES, 2, 2)),
			new RolesRow(new UIUser("4", "Aureliano Gentile"), subList(APPLICATION_ROLES, 1, 2)),
			new RolesRow(new UIUser("5", "Erik Van Ingen"), subList(APPLICATION_ROLES, 2, 2))			
			);
	
	protected static List<CodelistGroup> GROUPS = new ArrayList<CodelistGroup>();
	
	static {
		CodelistGroup asfisGroup = new CodelistGroup("ASFIS");
		asfisGroup.addVersions(asList(
				new Version(asfisGroup, "1", "2011"),
				new Version(asfisGroup, "2", "2012"),
				new Version(asfisGroup, "3", "2013")
				));
		GROUPS.add(asfisGroup);
		
		CodelistGroup countriesGroup =new CodelistGroup("COUNTRIES");
		countriesGroup.addVersions(asList(
				new Version(countriesGroup, "11", "2009"),
				new Version(countriesGroup, "21", "2011"),
				new Version(countriesGroup, "31", "2013")
				));
		GROUPS.add(countriesGroup);
		
	}
	
	protected static <T> List<T> asList(T ... items) {
		List<T> list = new ArrayList<T>();
		for (T item:items) list.add(item);
		return list;
	}
	
	protected static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
		return new ArrayList<T>(list.subList(fromIndex, toIndex));
	}
	
	
	protected Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Inject
	CodelistRepository repository;
	
	@Inject
	protected CodelistLoader codelistLoader;
	
	@Inject
	protected UserRepository userRepository;
	
	public void init() {
		codelistLoader.importAllCodelist();
		logger.trace("codelist in repository:");
		for (Codelist codelist:repository.get(allLists())) logger.trace(codelist.name().toString());
		logger.trace("done");

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
		for (User user:userRepository.get(allUsers())) {
			if (user.isRoot()) continue;
			RolesRow row = getCodelistRolesRow(user, codelistId);
			if (row!=null) rows.add(row);
		}
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

		for (Codelist codelist :repository.get(allLists())) {

			CodelistGroup group = groups.get(codelist.name());
			if (group == null) {
				group = new CodelistGroup(codelist.name().toString());
				groups.put(codelist.name(), group);
			}
			group.addVersion(codelist.id(), ValueUtils.safeValue(codelist.version()));
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
		return new ArrayList<String>(roles);
	}

}
