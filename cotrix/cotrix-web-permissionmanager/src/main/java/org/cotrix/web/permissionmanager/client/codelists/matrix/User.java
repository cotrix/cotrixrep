/**
 * 
 */
package org.cotrix.web.permissionmanager.client.codelists.matrix;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class User {
	
	protected String id;
	protected String username;
	
	/**
	 * @param id
	 * @param username
	 */
	public User(String id, String username) {
		this.id = id;
		this.username = username;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	

}
