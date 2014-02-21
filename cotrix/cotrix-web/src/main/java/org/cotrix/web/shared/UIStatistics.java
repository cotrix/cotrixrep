/**
 * 
 */
package org.cotrix.web.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIStatistics implements IsSerializable {
	
	private int codelists;
	private int codes;
	private int users;
	private int repositories;
	/**
	 * @return the codelists
	 */
	public int getCodelists() {
		return codelists;
	}
	/**
	 * @param codelists the codelists to set
	 */
	public void setCodelists(int codelists) {
		this.codelists = codelists;
	}
	/**
	 * @return the codes
	 */
	public int getCodes() {
		return codes;
	}
	/**
	 * @param codes the codes to set
	 */
	public void setCodes(int codes) {
		this.codes = codes;
	}
	/**
	 * @return the users
	 */
	public int getUsers() {
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(int users) {
		this.users = users;
	}
	/**
	 * @return the repositories
	 */
	public int getRepositories() {
		return repositories;
	}
	/**
	 * @param repositories the repositories to set
	 */
	public void setRepositories(int repositories) {
		this.repositories = repositories;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Statistics [codelists=");
		builder.append(codelists);
		builder.append(", codes=");
		builder.append(codes);
		builder.append(", users=");
		builder.append(users);
		builder.append(", repositories=");
		builder.append(repositories);
		builder.append("]");
		return builder.toString();
	}
}
