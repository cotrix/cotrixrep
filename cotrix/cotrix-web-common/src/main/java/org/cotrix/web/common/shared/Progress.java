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
	
	protected Status status;
	protected Error failureCause; 
	protected boolean mappingFailed;
	
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
	
	/**
	 * @return the mappingFailed
	 */
	public boolean isMappingFailed() {
		return mappingFailed;
	}

	/**
	 * @param mappingFailed the mappingFailed to set
	 */
	public void setMappingFailed() {
		this.status = Status.DONE;
		this.mappingFailed = true;
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
		builder.append(", mappingFailed=");
		builder.append(mappingFailed);
		builder.append("]");
		return builder.toString();
	}
}
