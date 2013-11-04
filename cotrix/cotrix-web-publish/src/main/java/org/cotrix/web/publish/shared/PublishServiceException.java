/**
 * 
 */
package org.cotrix.web.publish.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5478982387335334232L;
	
	public PublishServiceException(){
		super();
	}

	/**
	 * @param message
	 */
	public PublishServiceException(String message) {
		super(message);
	}
}
