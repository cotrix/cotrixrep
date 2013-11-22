/**
 * 
 */
package org.cotrix.web.permissionmanager.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.web.permissionmanager.client.PermissionService;
import org.cotrix.web.permissionmanager.shared.CodelistGroup;
import org.cotrix.web.permissionmanager.shared.RolesRow;
import org.cotrix.web.permissionmanager.shared.RolesType;
import org.cotrix.web.permissionmanager.shared.User;
import org.cotrix.web.permissionmanager.shared.CodelistGroup.Version;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PermissionServiceImpl extends RemoteServiceServlet implements PermissionService {

	private static final long serialVersionUID = 2621331963385532979L;
	
	protected static List<User> USERS = asList(
			new User("1", "Federico De Faveri"),
			new User("2", "Fabio Simeoni"),
			new User("3", "Anton Ellenbroek"),
			new User("4", "Aureliano Gentile"),
			new User("5", "Erik Van Ingen")	
			);
	
	protected static List<String> CODELIST_ROLES = asList("VIEWER", "EDITOR", "REVIEWER", "PUBLISHER");
	protected static List<String> APPLICATION_ROLES = asList("PUBLISH", "IMPORT", "ROOT");
	protected static Map<String, List<RolesRow>> CODELIST_ROWS = new HashMap<String, List<RolesRow>>();
	
	static {
	
		CODELIST_ROWS.put("1", asList(
			new RolesRow(new User("1", "Federico De Faveri"), subList(CODELIST_ROLES, 0, 2)),
			new RolesRow(new User("2", "Fabio Simeoni"), subList(CODELIST_ROLES, 0, 2)),
			new RolesRow(new User("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 2, 4)),
			new RolesRow(new User("4", "Aureliano Gentile"), subList(CODELIST_ROLES, 1, 2)),
			new RolesRow(new User("5", "Erik Van Ingen"), subList(CODELIST_ROLES, 2, 3))			
			));
	
		CODELIST_ROWS.put("2", asList(
			new RolesRow(new User("1", "Federico De Faveri"), subList(CODELIST_ROLES, 0, 3)),
			new RolesRow(new User("2", "Fabio Simeoni"), subList(CODELIST_ROLES, 0, 3)),
			new RolesRow(new User("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 2, 4))	
			));
	
		CODELIST_ROWS.put("3", asList(
			new RolesRow(new User("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 0, 4)),
			new RolesRow(new User("4", "Aureliano Gentile"), subList(CODELIST_ROLES, 1, 3))		
			));
	
		CODELIST_ROWS.put("4", asList(
			new RolesRow(new User("1", "Federico De Faveri"), subList(CODELIST_ROLES, 0, 4)),
			new RolesRow(new User("2", "Fabio Simeoni"), subList(CODELIST_ROLES, 0, 2))		
			));
	
		CODELIST_ROWS.put("5", asList(
			new RolesRow(new User("3", "Anton Ellenbroek"), subList(CODELIST_ROLES, 0, 4))
			));
	}
	
	protected static List<RolesRow> APPLICATION_ROWS = asList(
			new RolesRow(new User("1", "Federico De Faveri"), subList(APPLICATION_ROLES, 0, 2)),
			new RolesRow(new User("2", "Fabio Simeoni"), subList(APPLICATION_ROLES, 0, 2)),
			new RolesRow(new User("3", "Anton Ellenbroek"), subList(APPLICATION_ROLES, 2, 2)),
			new RolesRow(new User("4", "Aureliano Gentile"), subList(APPLICATION_ROLES, 1, 2)),
			new RolesRow(new User("5", "Erik Van Ingen"), subList(APPLICATION_ROLES, 2, 2))			
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

	@Override
	public List<String> getRoles(RolesType type) throws ServiceException {
		logger.trace("getRoles type {}", type);
		switch (type) {
			case APPLICATION: return APPLICATION_ROLES;
			case CODELISTS: return CODELIST_ROLES;
		}
		return new ArrayList<String>();
	}

	@Override
	public DataWindow<RolesRow> getApplicationRolesRows() throws ServiceException {
		logger.trace("getApplicationRolesRows");
		return new DataWindow<RolesRow>(APPLICATION_ROWS);

	}

	@Override
	public DataWindow<RolesRow> getCodelistRolesRows(String codelistId)	throws ServiceException {
		logger.trace("getCodelistRolesRows codelistId {}", codelistId);
		return new DataWindow<RolesRow>(CODELIST_ROWS.get(codelistId));

	}

	@Override
	public void codelistRolesRowUpdated(String codelistId, RolesRow row) {
		logger.trace("codelistRolesRowUpdated codelistId {} row {}", codelistId, row);
	}

	@Override
	public void applicationRolesRowUpdated(RolesRow row) {
		logger.trace("applicationRolesRowUpdated row {}", row);
	}

	@Override
	public DataWindow<CodelistGroup> getCodelistGroups() throws ServiceException {
		logger.trace("getCodelistGroups");
		return new DataWindow<CodelistGroup>(GROUPS);
	}

	@Override
	public List<User> getUsers() throws ServiceException {
		logger.trace("getUsers");
		return USERS;
	}

}
