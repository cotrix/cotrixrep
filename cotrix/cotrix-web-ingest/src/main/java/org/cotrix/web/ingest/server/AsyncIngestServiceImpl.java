/**
 * 
 */
package org.cotrix.web.ingest.server;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.web.common.server.CotrixRemoteServlet;
import org.cotrix.web.common.server.async.AbstractAsyncService;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.ingest.client.AsyncIngestService;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.ImportResult;
import org.cotrix.web.ingest.shared.MappingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class AsyncIngestServiceImpl extends AbstractAsyncService implements AsyncIngestService {

	public static class Servlet extends CotrixRemoteServlet {

		@Inject
		protected AsyncIngestServiceImpl bean;

		@Override
		public Object getBean() {
			return bean;
		}
	}

	private Logger logger = LoggerFactory.getLogger(AsyncIngestServiceImpl.class);
	
	@Inject
	private IngestServiceImpl syncService;

	@Override
	public AsyncOutput<ImportResult> startImport(final CsvConfiguration csvConfiguration, final ImportMetadata metadata,
			final List<AttributeMapping> mappings, final MappingMode mappingMode)
					throws ServiceException {
		logger.trace("asyncStartImport csvConfiguration: {}, metadata: {}, mappings: {}, mappingMode: {}", csvConfiguration, metadata, mappings, mappingMode);

		return call(new Callable<ImportResult>() {

			@Override
			public ImportResult call() throws Exception {
				return syncService.startImport(csvConfiguration, metadata, mappings, mappingMode);
			}
		});
	}

}
