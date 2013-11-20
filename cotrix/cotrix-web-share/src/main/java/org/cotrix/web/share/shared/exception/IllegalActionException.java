/**
 * 
 */
package org.cotrix.web.share.shared.exception;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class IllegalActionException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6269560101350615471L;
	
	protected IllegalActionException(){}
	
	public IllegalActionException(String message) {
		super(message);
	}

}
