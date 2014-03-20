package org.cotrix.web.ingest.client;

import java.util.List;

import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.ingest.shared.AssetDetails;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.CodeListType;
import org.cotrix.web.ingest.shared.CsvPreviewHeaders;
import org.cotrix.web.ingest.shared.FileUploadProgress;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.MappingMode;
import org.cotrix.web.ingest.shared.PreviewData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service/ingestService")
public interface IngestService extends RemoteService {
	
	public DataWindow<AssetInfo> getAssets(Range range, ColumnSortInfo columnSortInfo, boolean forceRefresh) throws ServiceException;
	
	public AssetDetails getAssetDetails(String assetId) throws ServiceException;
	
	public RepositoryDetails getRepositoryDetails(String repositoryId) throws ServiceException;
	
	public void setAsset(String assetId) throws ServiceException;
	
	public void startUpload() throws ServiceException;
	
	public FileUploadProgress getUploadProgress() throws ServiceException;
	
	public PreviewData getCsvPreviewData(CsvConfiguration configuration) throws ServiceException;
	
	public CodeListType getCodeListType() throws ServiceException;
	
	public ImportMetadata getMetadata() throws ServiceException;
	
	public CsvConfiguration getCsvParserConfiguration() throws ServiceException;
	
	public List<AttributeMapping> getMappings() throws ServiceException;
	
	public void startImport(CsvConfiguration csvConfiguration, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ServiceException;
	
	public Progress getImportProgress() throws ServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException;
	
	public CsvPreviewHeaders getCsvPreviewHeaders(CsvConfiguration configuration) throws ServiceException;
	public DataWindow<List<String>> getCsvPreviewData(Range range) throws ServiceException;
	
}