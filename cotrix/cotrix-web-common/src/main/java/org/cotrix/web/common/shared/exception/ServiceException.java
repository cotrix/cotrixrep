/**
 * 
 */
package org.cotrix.web.common.shared.exception;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8938142105568411445L;

	/**
	 * 
	 */
	protected ServiceException() {
	}

	/**
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}

}
