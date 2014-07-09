/**
 * 
 */
package org.cotrix.web.common.server;

import javax.inject.Inject;

import org.cotrix.web.common.client.CommonService;
import org.cotrix.web.common.server.async.ProgressService;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CommonServiceImpl implements CommonService {
	
	@SuppressWarnings("serial")
	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected CommonServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}

	private Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);
	
	@Inject
	private ProgressService progressService;
	
	@Override
	public LongTaskProgress getProgress(String progressToken) throws ServiceException {
		logger.trace("getProgress progressToken: {}", progressToken);
		
		return progressService.getProgress(progressToken);
	}

}
