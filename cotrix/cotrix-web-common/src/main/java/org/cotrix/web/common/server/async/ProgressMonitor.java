/**
 * 
 */
package org.cotrix.web.common.server.async;

import org.cotrix.common.async.ReportingFuture;
import org.cotrix.common.async.TaskUpdate;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ProgressMonitor<F extends AsyncOutcome<T>, T extends IsSerializable> {

	private Logger logger = LoggerFactory.getLogger(ProgressMonitor.class);

	private ReportingFuture<F> future;

	public ProgressMonitor(ReportingFuture<F> future) {
		this.future = future;
	}

	public LongTaskProgress getProgress() {
		logger.trace("getProgress");
		LongTaskProgress progress = new LongTaskProgress();

		try {

			if (future.isDone()) {
				logger.trace("future is done");
				F outcome = future.get();
				progress.setOutcome(outcome);
				
				progress.setDone();
			}

			TaskUpdate taskUpdate = future.get(TaskUpdate.class);
			logger.trace("taskUpdate.progress(): {}", taskUpdate.progress());
			progress.setPercentage(getProgressPercentage(taskUpdate));
			progress.setMessage(taskUpdate!=null?taskUpdate.activity():null);

			logger.trace("returning progress "+progress);
		} catch(Exception e) {
			logger.trace("execution failed", e);
			Throwable cause = e.getCause();
			progress.setFailed(Exceptions.toError(cause));
		}
		return progress;
	}
	
	private int getProgressPercentage(TaskUpdate update) {
		if (update == null) return 0;
		float progress = update.progress();
		return Math.min(100, (int)(progress*100));
	}

}
