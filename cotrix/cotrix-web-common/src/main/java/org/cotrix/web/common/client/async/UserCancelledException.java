/**
 * 
 */
package org.cotrix.web.common.client.async;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserCancelledException extends Exception {
	private static final long serialVersionUID = 5621572950726107662L;
	
	public UserCancelledException() {
		super("User cancelled the operation");
	}

}
