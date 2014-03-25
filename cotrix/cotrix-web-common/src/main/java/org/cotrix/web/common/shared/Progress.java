/**
 * 
 */
package org.cotrix.web.common.shared;

import org.cotrix.web.common.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Progress implements IsSerializable {
	
	public enum Status {ONGOING, DONE, FAILED};
	
	protected Status status;
	protected ServiceException failureCause; 
	protected boolean mappingFailed;
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * @return the failureCause
	 */
	public ServiceException getFailureCause() {
		return failureCause;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setFailed(ServiceException cause) {
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
	public void setMappingFailed(boolean mappingFailed) {
		this.mappingFailed = mappingFailed;
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
