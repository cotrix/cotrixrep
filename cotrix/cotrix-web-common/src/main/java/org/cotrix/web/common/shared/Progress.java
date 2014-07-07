/**
 * 
 */
package org.cotrix.web.common.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Progress implements IsSerializable {
	
	public enum Status {ONGOING, DONE, FAILED};
	
	private Status status;
	private Error failureCause; 
	
	public Progress() {
		status = Status.ONGOING;
	}
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * @return the failureCause
	 */
	public Error getFailureCause() {
		return failureCause;
	}

	public void setDone() {
		this.status = Status.DONE;
	}
	
	public void setFailed(Error cause) {
		this.status = Status.FAILED;
		this.failureCause = cause;
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
		builder.append("Progress [status=");
		builder.append(status);
		builder.append(", failureCause=");
		builder.append(failureCause);
		builder.append("]");
		return builder.toString();
	}
}
