package org.cotrix.web.manage.server;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.common.async.ExecutionService;
import org.cotrix.common.async.ReportingFuture;
import org.cotrix.web.common.server.CotrixRemoteServlet;
import org.cotrix.web.common.server.async.ProgressService;
import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.async.AsyncTask;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.common.shared.feature.UIFeature;
import org.cotrix.web.manage.client.AsyncManageService;
import org.cotrix.web.manage.shared.UICodelistInfo;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier.Void;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class AsyncManageServiceImpl implements AsyncManageService {

	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected AsyncManageServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}

	protected Logger logger = LoggerFactory.getLogger(AsyncManageServiceImpl.class);

	@Inject
	private ManageServiceImpl syncService;

	@Inject
	private ExecutionService executionService;

	@Inject
	private ProgressService progressService;

	@Override
	public AsyncOutput<Void> testAsync(final String input) throws ServiceException {
		logger.trace("testAsync input: {}", input);
		return call(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				syncService.testAsync(input);
				return AbstractFeatureCarrier.getVoid();
			}
		});
	}

	@Override
	public AsyncOutput<Void> removeCodelist(final String codelistId) throws ServiceException {
		logger.trace("asyncRemoveCodelist codelistId: {}", codelistId);
		return call(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				syncService.removeCodelist(codelistId);
				return AbstractFeatureCarrier.getVoid();
			}
		});
	}


	@Override
	public AsyncOutput<UICodelistInfo> createNewCodelistVersion(final String codelistId, final String newVersion) throws ServiceException {
		logger.trace("asynccreateNewCodelistVersion codelistId: {} newVersion: {}", codelistId, newVersion);
		return call(new Callable<UICodelistInfo>() {

			@Override
			public UICodelistInfo call() throws Exception {
				return syncService.createNewCodelistVersion(codelistId, newVersion);
			}
		});
	}

	private <T extends IsSerializable> AsyncOutput<T> call(final Callable<T> call) {
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
