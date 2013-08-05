/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportProgress implements IsSerializable {
	
	public enum Status {ONGOING, DONE, FAILED};
	
	protected Status status;
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	
	public boolean isComplete()
	{
		return (status == Status.DONE || status == Status.FAILED);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImportProgress [status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
}
