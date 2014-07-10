/**
 * 
 */
package org.cotrix.web.common.server.async;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Singleton;

import org.cotrix.common.async.ReportingFuture;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.feature.FeatureCarrier;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ProgressService {
	
	private Map<String, ProgressMonitor<?, ?>> monitors;
	
	public ProgressService() {
		monitors = new HashMap<>();
	}	
	
	public <F extends AsyncOutcome<T>, T extends FeatureCarrier> String monitorize(ReportingFuture<F> future) {
		ProgressMonitor<?,?> monitor = new ProgressMonitor<F, T>(future);
		String token = UUID.randomUUID().toString();
		monitors.put(token, monitor);
		return token;
	}
	
	public LongTaskProgress getProgress(String progressToken) {
		ProgressMonitor<?,?> monitor = monitors.get(progressToken);
		if (monitor == null) throw new IllegalArgumentException("No monitor found with token "+progressToken);
		LongTaskProgress progress = monitor.getProgress();
		if (progress.isComplete()) monitors.remove(progressToken);
		return progress;
	}

}
