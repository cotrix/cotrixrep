/**
 * 
 */
package org.cotrix.web.common.server.async;

import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.common.async.ExecutionService;
import org.cotrix.common.async.ReportingFuture;
import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.async.AsyncTask;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.common.shared.feature.UIFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractAsyncService {
	
	private Logger logger = LoggerFactory.getLogger(AbstractAsyncService.class);

	@Inject
	private ExecutionService executionService;

	@Inject
	private ProgressService progressService;
	
	

	protected <T extends IsSerializable> AsyncOutput<T> call(final Callable<T> call) {
		ReportingFuture<AsyncOutcome<T>> future = executionService.execute(new Callable<AsyncOutcome<T>>() {
			@Override
			public AsyncOutcome<T> call() throws Exception {
				logger.trace("executing call");
				T result = call.call();
				return toOutcome(result);
			}
		});

		String progressToken = progressService.<AsyncOutcome<T>,T>monitorize(future);
		logger.trace("returning progress token: {}",progressToken);

		return new AsyncTask<>(progressToken);
	}

	private <T extends IsSerializable> AsyncOutcome<T> toOutcome(T result) {
		AsyncOutcome<T> outcome = new AsyncOutcome<T>(result);
		if (result instanceof FeatureCarrier) copy((FeatureCarrier) result, outcome);
		return outcome;
	}

	private void copy(FeatureCarrier source, FeatureCarrier destination) {
		destination.setApplicationFeatures(source.getApplicationFeatures());
		if (source.getInstancesFeatures()!=null) {
			for (Entry<String, Set<UIFeature>> entry:source.getInstancesFeatures().entrySet()) {
				destination.addInstancesFeatures(entry.getKey(), entry.getValue());
			}
		}
	}

}
