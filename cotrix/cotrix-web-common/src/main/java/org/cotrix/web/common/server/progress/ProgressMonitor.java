/**
 * 
 */
package org.cotrix.web.common.server.progress;

import java.util.concurrent.ExecutionException;

import org.cotrix.common.async.ReportingFuture;
import org.cotrix.common.async.TaskUpdate;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ProgressMonitor {
	
	private Logger logger = LoggerFactory.getLogger(ProgressMonitor.class);

	private ReportingFuture<?> future;
	
	public ProgressMonitor(ReportingFuture<?> future) {
		this.future = future;
	}

	public LongTaskProgress getProgress() {
		logger.trace("getProgress");
		try {
			LongTaskProgress progress = new LongTaskProgress();

			if (future.isDone()) {
				logger.trace("future is done");
				progress.setDone();
				try {
					future.get();
				} catch(ExecutionException e) {
					logger.trace("execution failed", e);
					Throwable cause = e.getCause();
					progress.setFailed(Exceptions.toError(cause));
				}
			}

			TaskUpdate taskUpdate = future.get(TaskUpdate.class);
			progress.setPercentage(taskUpdate!=null?(int)taskUpdate.progress()*100:0);
			progress.setMessage(taskUpdate!=null?taskUpdate.activity():null);

			logger.trace("returning progress "+progress);
			
			return progress;
		} catch(Exception e) {
			logger.error("Retrieving progress failed", e);
			throw new RuntimeException("Failed getting task progress", e);
		}
	}

}
