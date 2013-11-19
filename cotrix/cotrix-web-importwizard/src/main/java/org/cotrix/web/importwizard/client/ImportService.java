package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.ReportLog;
import org.cotrix.web.share.shared.codelist.RepositoryDetails;
import org.cotrix.web.share.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("importService")
public interface ImportService extends RemoteService {
	
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
	
	public void startImport(ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ServiceException;
	
	public Progress getImportProgress() throws ServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException;
	
}