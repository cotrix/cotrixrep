/**
 * 
 */
package org.cotrix.web.shared;

import org.cotrix.web.share.shared.exception.ServiceException;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UnknownUserException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6063313755054098701L;

	/**
	 * 
	 */
	protected UnknownUserException() {
	}

	/**
	 * @param message
	 */
	public UnknownUserException(String message) {
		super(message);
	}

}
