/**
 * 
 */
package org.cotrix.web.share.client.util;

import com.google.gwt.regexp.shared.RegExp;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AccountValidator {
	
	protected static final RegExp EMAIL_EXP = RegExp.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$");
	
	
	public static boolean validateUsername(String username) {
		return notNullNotEmpty(username);
	}
	
	public static boolean validateFullName(String fullName) {
		return notNullNotEmpty(fullName);
	}
	
	public static boolean validatePassword(String password) {
		return notNullNotEmpty(password);
	}
	
	public static boolean validateEMail(String email) {
		if (!notNullNotEmpty(email)) return false;
		return EMAIL_EXP.test(email);
	}
	
	protected static boolean notNullNotEmpty(String value) {
		return value!=null && !value.isEmpty();
	}
}
