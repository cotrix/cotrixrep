/**
 * 
 */
package org.cotrix.web.users.shared;

import org.cotrix.web.common.shared.exception.ServiceException;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class InvalidPasswordException extends ServiceException {

	private static final long serialVersionUID = -8393979745606612764L;

	protected InvalidPasswordException() {
	}

	public InvalidPasswordException(String message) {
		super(message);
	}
}
