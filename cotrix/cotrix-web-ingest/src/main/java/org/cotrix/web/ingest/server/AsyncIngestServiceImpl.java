/**
 * 
 */
package org.cotrix.web.ingest.server;

import javax.inject.Inject;

import org.cotrix.web.common.server.CotrixRemoteServlet;
import org.cotrix.web.ingest.client.AsyncIngestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class AsyncIngestServiceImpl implements AsyncIngestService {
	
	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected AsyncIngestServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}

	private Logger logger = LoggerFactory.getLogger(AsyncIngestServiceImpl.class);

	}
