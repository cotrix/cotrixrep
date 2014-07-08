/**
 * 
 */
package org.cotrix.web.ingest.shared;
import org.cotrix.web.common.shared.Error;
import org.cotrix.web.common.shared.Progress;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FileUploadProgress extends Progress {
	
	protected int progress;
	protected UIAssetType codeListType;
	
	public FileUploadProgress(){}
	
	/**
	 * @param progress
	 * @param status
	 */
	public FileUploadProgress(int progress, Status status, UIAssetType codeListType) {
		this.progress = progress;
		this.status = status;
		this.codeListType = codeListType;
	}

	public int getProgress() {
		return progress;
	}

	public UIAssetType getCodeListType() {
		return codeListType;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}


	public void setDone() {
		this.status = Status.DONE;
	}
	
	public void setFailed(String errorMessage) {
		setFailed(new Error(errorMessage, ""));
	}

	public void setCodeListType(UIAssetType codeListType) {
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
		builder.append(failureCause);
		builder.append("]");
		return builder.toString();
	}
}
