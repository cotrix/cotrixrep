package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.ImportServiceException;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.ReportLog;
import org.cotrix.web.share.shared.codelist.RepositoryDetails;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("importService")
public interface ImportService extends RemoteService {
	
	public DataWindow<AssetInfo> getAssets(Range range, ColumnSortInfo columnSortInfo, boolean forceRefresh) throws ImportServiceException;
	
	public AssetDetails getAssetDetails(String assetId) throws ImportServiceException;
	
	public RepositoryDetails getRepositoryDetails(String repositoryId) throws ImportServiceException;
	
	public void setAsset(String assetId) throws ImportServiceException;
	
	public void startUpload() throws ImportServiceException;
	
	public FileUploadProgress getUploadProgress() throws ImportServiceException;
	
	public PreviewData getCsvPreviewData(CsvConfiguration configuration) throws ImportServiceException;
	
	public CodeListType getCodeListType() throws ImportServiceException;
	
	public ImportMetadata getMetadata() throws ImportServiceException;
	
	public CsvConfiguration getCsvParserConfiguration() throws ImportServiceException;
	
	public List<AttributeMapping> getMappings() throws ImportServiceException;
	
	public void startImport(ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ImportServiceException;
	
	public Progress getImportProgress() throws ImportServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws ImportServiceException;
	
}