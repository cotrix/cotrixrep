/**
 * 
 */
package org.cotrix.web.permissionmanager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class User implements IsSerializable {
	
	protected String id;
	protected String username;
	
	public User(){}
	
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
