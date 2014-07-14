package org.cotrix.web.ingest.client;

import java.util.List;

import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.ImportResult;
import org.cotrix.web.ingest.shared.MappingMode;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service/asyncIngestService")
public interface AsyncIngestService extends RemoteService {
	
	public AsyncOutput<ImportResult> startImport(CsvConfiguration csvConfiguration, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ServiceException;

	
}