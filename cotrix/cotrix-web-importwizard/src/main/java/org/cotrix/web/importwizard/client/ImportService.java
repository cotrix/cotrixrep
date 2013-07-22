package org.cotrix.web.importwizard.client;

import java.util.ArrayList;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListPreviewData;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportServiceException;
import org.cotrix.web.importwizard.shared.UploadProgress;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("importService")
public interface ImportService extends RemoteService {
	
	boolean sendToServer(CotrixImportModel model) throws IllegalArgumentException;
	
	void testBackendConnection() throws IllegalArgumentException;
	
	ArrayList<AssetInfo> getAssets(Range range) throws ImportServiceException;
	
	AssetDetails getAssetDetails(String assetId) throws ImportServiceException;
	
	public UploadProgress getUploadProgress() throws ImportServiceException;
	
	public CodeListPreviewData getPreviewData() throws ImportServiceException;
	
	public CodeListType getCodeListType() throws ImportServiceException;
	
	public Metadata getMetadata() throws ImportServiceException;
	
	public CsvParserConfiguration getCsvParserConfiguration() throws ImportServiceException;
	
	public void updateCsvParserConfiguration(CsvParserConfiguration configuration) throws ImportServiceException;
	
}