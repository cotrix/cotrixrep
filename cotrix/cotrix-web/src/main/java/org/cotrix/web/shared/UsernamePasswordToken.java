/**
 * 
 */
package org.cotrix.web.shared;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UsernamePasswordToken implements LoginToken {
	
	public static final UsernamePasswordToken GUEST = new UsernamePasswordToken(null, null);
	
	protected String username;
	protected String password;
	
	public UsernamePasswordToken(){}
	
	/**
	 * @param username
	 * @param password
	 */
	public UsernamePasswordToken(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsernamePasswordToken other = (UsernamePasswordToken) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UsernamePasswordToken [username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}
}
