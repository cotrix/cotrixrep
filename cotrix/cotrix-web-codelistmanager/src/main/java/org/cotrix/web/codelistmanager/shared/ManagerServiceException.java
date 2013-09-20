/**
 * 
 */
package org.cotrix.web.codelistmanager.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ManagerServiceException extends Exception {

	private static final long serialVersionUID = 152166625542669144L;

	protected ManagerServiceException(){}
	
	/**
	 * @param message
	 */
	public ManagerServiceException(String message) {
		super(message);
	}

}
