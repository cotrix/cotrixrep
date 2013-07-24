package org.cotrix.web.importwizard.client;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.ColumnDefinition;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListPreviewData;
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
	
	ArrayList<AssetInfo> getAssets(Range range) throws ImportServiceException;
	
	AssetDetails getAssetDetails(String assetId) throws ImportServiceException;
	
	public FileUploadProgress getUploadProgress() throws ImportServiceException;
	
	public CodeListPreviewData getPreviewData() throws ImportServiceException;
	
	public CodeListType getCodeListType() throws ImportServiceException;
	
	public ImportMetadata getMetadata() throws ImportServiceException;
	
	public void updateMetadata(ImportMetadata metadata) throws ImportServiceException;
	
	public CsvParserConfiguration getCsvParserConfiguration() throws ImportServiceException;
	
	public void updateCsvParserConfiguration(CsvParserConfiguration configuration) throws ImportServiceException;
	
	public List<ColumnDefinition> getColumns() throws ImportServiceException;
	
	public void startImport(ImportMetadata metadata, List<ColumnDefinition> columns) throws ImportServiceException;
	
	public ImportProgress getImportProgress() throws ImportServiceException;
	
}