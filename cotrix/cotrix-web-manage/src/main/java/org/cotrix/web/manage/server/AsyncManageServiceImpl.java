package org.cotrix.web.manage.server;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.web.common.server.CotrixRemoteServlet;
import org.cotrix.web.common.server.async.AbstractAsyncService;
import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier.Void;
import org.cotrix.web.manage.client.AsyncManageService;
import org.cotrix.web.manage.shared.UICodelistInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class AsyncManageServiceImpl extends AbstractAsyncService implements AsyncManageService {

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

}
