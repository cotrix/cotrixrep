/**
 * 
 */
package org.cotrix.web.common.shared.exception;

import org.cotrix.web.common.shared.Error;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceErrorException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6296816748831623594L;
	
	private Error error;
	
	@SuppressWarnings("unused")
	private ServiceErrorException() {
	}
	
	public ServiceErrorException(Error error) {
		super(error.getMessage());
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

}
