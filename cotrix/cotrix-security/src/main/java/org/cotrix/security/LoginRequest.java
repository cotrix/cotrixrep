/**
 * 
 */
package org.cotrix.security;

import java.util.HashMap;
import java.util.Map;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LoginRequest {
	
	private Map<String, String> attributes = new HashMap<>();
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}
}
