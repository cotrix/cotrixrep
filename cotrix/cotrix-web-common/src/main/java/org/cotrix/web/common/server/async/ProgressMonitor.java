/**
 * 
 */
package org.cotrix.web.common.server.async;

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
		LongTaskProgress progress = new LongTaskProgress();

		try {

			if (future.isDone()) {
				logger.trace("future is done");
				progress.setDone();
			}

			TaskUpdate taskUpdate = future.get(TaskUpdate.class);
			progress.setPercentage(taskUpdate!=null?(int)taskUpdate.progress()*100:0);
			progress.setMessage(taskUpdate!=null?taskUpdate.activity():null);

			logger.trace("returning progress "+progress);
		} catch(ExecutionException e) {
			logger.trace("execution failed", e);
			Throwable cause = e.getCause();
			progress.setFailed(Exceptions.toError(cause));
		}
		return progress;
	}

}
