package org.cotrix.domain.user;

import static org.cotrix.action.Action.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.cotrix.action.Action;
import org.cotrix.action.ResourceType;

/**
 * A view of the roles and permissions of a given user indexed by resource type and identifier. 
 * 
 * @author Fabio Simeoni
 *
 */
public class FingerPrint {

	private Map<ResourceType,Map<String,Rights>> fp = new HashMap<ResourceType,Map<String,Rights>>();
	
	/**
	 * Creates an instance for a given user.
	 * @param user the user
	 */
	public FingerPrint(User user) {
	
		for (Action p : user.permissions())
			rightsOver(p.resource(),p.type()).permissions.add(p);
		
		for (Role r : user.roles())
			rightsOver(r.resource(),r.type()).roles.add(r.name());
		
	}
	
	/**
	 * Returns the types of resources over which the user has permissions or roles.
	 * @return the types
	 */
	public Collection<ResourceType> types() {
		return fp.keySet();
	}
	
	/**
	 * Returns the identifiers of all the resources of a given type over which the user has permissions or roles.
	 * @param type the type
	 * @return the resource identifier
	 */
	public Collection<String> resources(ResourceType type) {
		
		Collection<String> resources = fp.containsKey(type)?fp.get(type).keySet():new HashSet<String>();
		
		return resources;
	}
	
	/**
	 * Returns the roles of the user over a given resource, excluding those implied by role templates.
	 * @param resource the resource identifier
	 * @param type the resource type
	 * @return the roles of the user over the resource
	 */
	public Collection<String> specificRolesOver(String resource, ResourceType type) {
		
		Rights rights = target(resource,type);
		
		Collection<String> roles =  (rights== null)? new HashSet<String>() : rights.roles;
		
		return roles;
	}
	
	
	/**
	 * Returns the roles of the user over a given resource, including those implied by role templates.
	 * @param resource the resource identifier
	 * @param type the resource type
	 * @return the roles of the user over the resource
	 */
	public Collection<String> allRolesOver(String resource, ResourceType type) {
		
		Collection<String> roles =  specificRolesOver(resource, type);
		
		//add roles over all resources
		if (!resource.equals(any))
			roles.addAll(specificRolesOver(any,type));
		
		return roles;
	}
	
	
	/**
	 * Returns the permissions of the user over a given resource, including those implied by permission templates.
	 * @param resource the resource identifier
	 * @param type the resource type
	 * @return the permissions of the user over the resource
	 */
	public Collection<Action> permissionsOver(String resource, ResourceType type) {
		
		Rights rights = target(resource,type);
		
		Collection<Action> permissions = rights == null? Collections.<Action>emptySet() : rights.permissions;
		
		//add permission over all resources
		if (!resource.equals(any))
			permissions.addAll(permissionsOver(any, type));
		
		return permissions;
	}
	
	/**
	 * Returns the permissions of the user over a specific resource, excluding those implied by permission templates.
	 * @param resource the resource identifier
	 * @param type the resource type
	 * @return the permissions of the user over the resource
	 */
	public Collection<Action> specificPermissionsOver(String resource, ResourceType type) {
		
		Rights rights = target(resource,type);
		
		Collection<Action> permissions = rights == null? Collections.<Action>emptySet() : rights.permissions;
		
		//add permission over all resources
		if (!resource.equals(any))
			permissions.addAll(permissionsOver(any, type));
		
		return permissions;
	}


	
	//helpers
	
	
	private Rights target(String resource, ResourceType type) {
		
		Map<String,Rights> roleMap = fp.get(type);
		
		return roleMap!=null? roleMap.get(resource):null;
	}
	
	private Rights rightsOver(String resource,ResourceType type) {
		
		Map<String,Rights> resourceMap = fp.get(type);
		
		if (resourceMap==null) {
			resourceMap = new HashMap<String,Rights>();
			fp.put(type,resourceMap);
		}
		
		Rights randp = resourceMap.get(resource);
		
		if (randp == null) {
			randp = new Rights();
			resourceMap.put(resource,randp);
		}
		
		return randp;
	}
	
	@Override
	public String toString() {
		return fp.toString();
	}
	
	
	class Rights {

		private Collection<String> roles = new HashSet<String>();
		private Collection<Action> permissions = new HashSet<Action>();
		
		
		@Override
		public String toString() {
			return "[roles=" + (roles != null ? roles.toString() : null) + ", permissions="
					+ (permissions != null ? permissions.toString() : null) + "]";
		}
	}
}
