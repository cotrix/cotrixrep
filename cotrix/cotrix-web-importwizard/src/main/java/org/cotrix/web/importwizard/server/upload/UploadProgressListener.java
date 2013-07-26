/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

import org.apache.commons.fileupload.ProgressListener;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UploadProgressListener implements ProgressListener {
	
	protected Logger logger = LoggerFactory.getLogger(UploadProgressListener.class);
	protected FileUploadProgress uploadProgress;
	protected long megaBytes = -1;

	/**
	 * @param uploadProgress
	 */
	public UploadProgressListener(FileUploadProgress uploadProgress) {
		this.uploadProgress = uploadProgress;
	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		long mBytes = pBytesRead / 1000000;
		
		int progress = (int) (pContentLength>0?(pBytesRead*90)/pContentLength : 0);
		uploadProgress.setProgress(progress);
		
		if (megaBytes == mBytes) {
			return;
		}
		megaBytes = mBytes;
		logger.trace("Reading item {}, {} of {} bytes have been read. progress {}", pItems, pBytesRead, pContentLength, progress);
	}

}
