/**
 * 
 */
package org.cotrix.web.shared;

import org.cotrix.web.common.shared.exception.ServiceException;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class InvalidUsernameException extends ServiceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7652311038392376105L;

	protected InvalidUsernameException(){}

	public InvalidUsernameException(String message) {
		super(message);
	}

}
