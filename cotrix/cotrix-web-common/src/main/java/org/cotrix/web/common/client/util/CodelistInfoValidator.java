/**
 * 
 */
package org.cotrix.web.common.client.util;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistInfoValidator {
	
	public static boolean validateName(String name) {
		return notNullNotEmpty(name);
	}
	
	public static boolean validateVersion(String version) {
		return notNullNotEmpty(version);
	}
	
	protected static boolean notNullNotEmpty(String value) {
		return value!=null && !value.isEmpty();
	}

}
