/**
 * 
 */
package org.cotrix.web.importwizard.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5478982387335334232L;
	
	public ImportServiceException(){
		super();
	}

	/**
	 * @param message
	 */
	public ImportServiceException(String message) {
		super(message);
	}
}
