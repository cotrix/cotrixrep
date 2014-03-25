/**
 * 
 */
package org.cotrix.web.ingest.shared;

import java.io.Serializable;
import org.cotrix.web.common.shared.Error;

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
	protected Error error;
	
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
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}


	public void setDone() {
		this.status = Status.DONE;
	}
	
	public void setFailed(String errorMessage) {
		setFailed(new Error(errorMessage, ""));
	}
		
	
	public void setFailed(Error error) {
		this.status = Status.FAILED;
		this.error = error;
	}

	/**
	 * @param codeListType the codeListType to set
	 */
	public void setCodeListType(CodeListType codeListType) {
		this.codeListType = codeListType;
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
		builder.append(", error=");
		builder.append(error);
		builder.append("]");
		return builder.toString();
	}
}
