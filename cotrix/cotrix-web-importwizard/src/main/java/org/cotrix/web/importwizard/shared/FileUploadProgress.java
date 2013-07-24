/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUploadProgress implements Serializable {
	
	private static final long serialVersionUID = -5904685977710138006L;
	
	public enum Status {ONGOING, FAILED, DONE};
	
	protected int progress;
	protected Status status;
	protected CodeListType codeListType;
	
	public FileUploadProgress(){}
	
	/**
	 * @param progress
	 * @param status
	 */
	public FileUploadProgress(int progress, Status status, CodeListType codeListType) {
		this.progress = progress;
		this.status = status;
		this.codeListType = codeListType;
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
	 * @return the codeListType
	 */
	public CodeListType getCodeListType() {
		return codeListType;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUploadProgress [progress=");
		builder.append(progress);
		builder.append(", status=");
		builder.append(status);
		builder.append(", codeListType=");
		builder.append(codeListType);
		builder.append("]");
		return builder.toString();
	}
}
