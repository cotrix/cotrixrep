/**
 * 
 */
package org.cotrix.web.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SessionIdToken implements LoginToken {
	
	protected String sessionId;

	/**
	 * @param sessionId
	 */
	public SessionIdToken(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SessionIdToken [sessionId=");
		builder.append(sessionId);
		builder.append("]");
		return builder.toString();
	}
}
