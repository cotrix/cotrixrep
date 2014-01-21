/**
 * 
 */
package org.cotrix.web.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UrlToken implements LoginToken {
	
	protected String token;
	
	public UrlToken(){}

	/**
	 * @param token
	 */
	public UrlToken(String token) {
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UrlToken [token=");
		builder.append(token);
		builder.append("]");
		return builder.toString();
	}
}
