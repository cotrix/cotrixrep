package org.cotrix.web.ingest.client;

import java.util.List;

import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.ingest.shared.AssetDetails;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.UIAssetType;
import org.cotrix.web.ingest.shared.PreviewHeaders;
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
	
	public DataWindow<AssetInfo> getAssets(Range range, ColumnSortInfo columnSortInfo, String query, boolean refreshCache, boolean requestDiscovery) throws ServiceException;
	
	public AssetDetails getAssetDetails(String assetId) throws ServiceException;
	
	public RepositoryDetails getRepositoryDetails(UIQName repositoryId) throws ServiceException;
	
	public void setAsset(String assetId) throws ServiceException;
	
	public void startUpload() throws ServiceException;
	
	public FileUploadProgress getUploadProgress() throws ServiceException;
	
	public PreviewData getCsvPreviewData(CsvConfiguration configuration) throws ServiceException;
	
	public UIAssetType getCodeListType() throws ServiceException;
	
	public ImportMetadata getMetadata() throws ServiceException;
	
	public CsvConfiguration getCsvParserConfiguration() throws ServiceException;
	
	public List<AttributeMapping> getMappings() throws ServiceException;
	
	public void startImport(CsvConfiguration csvConfiguration, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ServiceException;
	
	public Progress getImportProgress() throws ServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException;
	
	public PreviewHeaders getPreviewHeaders(CsvConfiguration configuration) throws ServiceException;
	public DataWindow<List<String>> getPreviewData(Range range) throws ServiceException;
	
}