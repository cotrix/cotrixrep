package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetsBatch;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.ImportProgress;
import org.cotrix.web.importwizard.shared.ImportServiceException;
import org.cotrix.web.importwizard.shared.FileUploadProgress;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("importService")
public interface ImportService extends RemoteService {
	
	public AssetsBatch getAssets(Range range) throws ImportServiceException;
	
	public AssetDetails getAssetDetails(String assetId) throws ImportServiceException;
	
	public void setAsset(String assetId) throws ImportServiceException;
	
	public FileUploadProgress getUploadProgress() throws ImportServiceException;
	
	public PreviewData getCsvPreviewData(CsvParserConfiguration configuration) throws ImportServiceException;
	
	public CodeListType getCodeListType() throws ImportServiceException;
	
	public ImportMetadata getMetadata() throws ImportServiceException;
	
	public CsvParserConfiguration getCsvParserConfiguration() throws ImportServiceException;
	
	public AttributesMappings getMappings() throws ImportServiceException;
	
	public void startImport(ImportMetadata metadata, AttributesMappings mappings) throws ImportServiceException;
	
	public ImportProgress getImportProgress() throws ImportServiceException;
	
}