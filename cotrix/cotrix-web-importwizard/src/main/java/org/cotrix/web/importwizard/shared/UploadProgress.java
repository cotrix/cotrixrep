/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UploadProgress implements Serializable {
	
	private static final long serialVersionUID = -5904685977710138006L;
	
	public enum Status{ONGOING, FAILED, DONE};
	
	protected int progress;
	protected Status status;
	
	public UploadProgress(){}
	
	/**
	 * @param progress
	 * @param status
	 */
	public UploadProgress(int progress, Status status) {
		this.progress = progress;
		this.status = status;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UploadProgress [progress=");
		builder.append(progress);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
