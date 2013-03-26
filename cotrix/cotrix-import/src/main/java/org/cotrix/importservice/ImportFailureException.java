package org.cotrix.importservice;

/**
 * Represents the failure of an import task.
 *  
 * @author Fabio Simeoni
 *
 */
public class ImportFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an instance with a given message.
	 * @param msg the message
	 */
	public ImportFailureException(String msg) {
		super(msg);
	}
}
