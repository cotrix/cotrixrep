/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.io.CloudService;
import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.server.climport.ImportTaskSession;
import org.cotrix.web.importwizard.server.climport.Importer;
import org.cotrix.web.importwizard.server.climport.ImporterFactory;
import org.cotrix.web.importwizard.server.upload.MappingGuesser;
import org.cotrix.web.importwizard.server.upload.MappingsManager;
import org.cotrix.web.importwizard.server.upload.PreviewDataManager;
import org.cotrix.web.importwizard.server.util.AssetInfosCache;
import org.cotrix.web.importwizard.server.util.Assets;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.share.server.util.Encodings;
import org.cotrix.web.share.server.util.Ranges;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.ReportLog;
import org.cotrix.web.share.shared.codelist.RepositoryDetails;
import org.cotrix.web.share.shared.exception.ServiceException;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.tabular.Table;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.Range;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class ImportServiceImpl extends RemoteServiceServlet implements ImportService {

	protected Logger logger = LoggerFactory.getLogger(ImportServiceImpl.class);

	@Inject
	protected CloudService cloud;

	@Inject
	protected ParsingHelper parsingHelper;

	@Inject
	protected MappingGuesser mappingsGuesser;

	@Inject
	protected ImporterFactory importerFactory;
	
	@Inject
	protected ImportSession session;
	
	@Inject
	protected AssetInfosCache assetInfosCache;
	
	@Inject
	protected PreviewDataManager previewDataManager;
	
	@Inject
	protected MappingsManager mappingsManager;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws ServletException {
		cloud.discover();
	}

	/** 
	 * {@inheritDoc}
	 * @throws ServiceException 
	 */
	@Override
	public DataWindow<AssetInfo> getAssets(Range range, ColumnSortInfo columnSortInfo, boolean forceRefresh) throws ServiceException {
		logger.trace("getAssets range: {} columnSortInfo: {} forceRefresh: {}", range, columnSortInfo, forceRefresh);
		try {

			if (forceRefresh) assetInfosCache.refreshCache();
			List<AssetInfo> assets = assetInfosCache.getAssets(columnSortInfo.getName());
			List<AssetInfo> sublist = columnSortInfo.isAscending()?Ranges.subList(assets, range):Ranges.subListReverseOrder(assets, range);

			logger.trace("returning "+sublist.size()+" elements");

			return new DataWindow<AssetInfo>(sublist, assets.size());
		} catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Error retrieving assets", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/** 
	 * {@inheritDoc}
	 * @throws ServiceException 
	 */
	@Override
	public AssetDetails getAssetDetails(String assetId) throws ServiceException {

		try {
			Asset asset = assetInfosCache.getAsset(assetId);
			if (asset == null) throw new IllegalArgumentException("Asset with id "+assetId+" not found");
			
			AssetDetails assetDetails = Assets.convertToDetails(asset);
			return assetDetails;
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	public RepositoryDetails getRepositoryDetails(String repositoryId) throws ServiceException
	{
		try {
			RepositoryDetails repository = assetInfosCache.getRepository(repositoryId);
			if (repository == null) throw new IllegalArgumentException("Repository with id "+repositoryId+" not found");
			return repository;
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	public void startUpload() throws ServiceException {
		logger.trace("startUpload");
		try {
			session.clean();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public FileUploadProgress getUploadProgress() throws ServiceException {

		try {
			FileUploadProgress uploadProgress = session.getUploadProgress();
			if (uploadProgress == null) {
				logger.error("Unexpected upload progress null.");
				throw new ServiceException("Upload progress not available");
			}
			return uploadProgress;
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public PreviewData getCsvPreviewData(CsvConfiguration configuration) throws ServiceException {
		logger.trace("getCsvPreviewData configuration: {}", configuration);
		
		try {
			if (session.getCodeListType()!=CodeListType.CSV) {
				logger.error("Requested CSV preview data when CodeList type is {}", session.getCodeListType());
				throw new ServiceException("No preview data available");
			}
			
			previewDataManager.refresh(configuration);

			return previewDataManager.getPreviewData();
		} catch(Exception e)
		{
			logger.error("Error converting the preview data", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CodeListType getCodeListType() throws ServiceException {
		try {
			return session.getCodeListType();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public ImportMetadata getMetadata() throws ServiceException {

		try {
			return session.getGuessedMetadata();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CsvConfiguration getCsvParserConfiguration() throws ServiceException {
		try {
			CsvConfiguration configuration = previewDataManager.getParserConfiguration();
			configuration.setAvailablesCharset(Encodings.getEncodings());
			return configuration;
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<AttributeMapping> getMappings() throws ServiceException {
		try {
			return mappingsManager.getMappings();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public void startImport(CsvConfiguration csvConfiguration, ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ServiceException {
		logger.trace("startImport csvConfiguration: {}, metadata: {}, mappings: {}, mappingMode: {}", csvConfiguration, metadata, mappings, mappingMode);

		try {
			session.setImportedCodelistName(metadata.getName());
			
			ImportTaskSession importTaskSession = session.createImportTaskSession();
			importTaskSession.setUserOptions(csvConfiguration, metadata, mappings, mappingMode);
			Importer<?> importer = importerFactory.createImporter(importTaskSession, session.getCodeListType());
			session.setImporter(importer);
			
			//FIXME use a serialiser provider
			Thread th = new Thread(importer);
			th.start();
		} catch (IOException e) {
			logger.error("Error during import starting", e);
			throw new ServiceException("An error occurred starting import: "+e.getMessage());
		}
	}


	@Override
	public Progress getImportProgress() throws ServiceException {
		try {
			return session.getImporter().getProgress();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public void setAsset(String assetId) throws ServiceException {
		logger.trace("setAsset {}", assetId);

		try {
			Asset asset = assetInfosCache.getAsset(assetId);
			if (asset == null) throw new IllegalArgumentException("Asset with id "+assetId+" not found");
			session.setSelectedAsset(asset);

			ImportMetadata metadata = new ImportMetadata();
			metadata.setOriginalName(asset.name());
			metadata.setName(asset.name());
			session.setGuessedMetadata(metadata);

			if (asset.type() == SdmxCodelist.type) {
				session.setCodeListType(CodeListType.SDMX);
				CodelistBean codelist = cloud.retrieveAsSdmx(asset.id());
				metadata.setVersion(codelist.getVersion());
				mappingsManager.setDefaultSdmxMappings();
			}


			if (asset.type() == CsvCodelist.type) {
				session.setCodeListType(CodeListType.CSV);
				Table table = cloud.retrieveAsTable(asset.id());
				metadata.setVersion("1");
				mappingsManager.updateMappings(table);
			}

		} catch(Exception e)
		{
			logger.error("Error setting the Asset",e);
			throw new ServiceException("Error setting the Asset: "+e.getMessage());
		}

	}

	@Override
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException {
		logger.trace("getReportLogs range: {}", range);
		try {
			ImportTaskSession importTaskSession = session.getImportTaskSession();
			List<ReportLog> logs = (importTaskSession==null || importTaskSession.getLogs()==null)?Collections.<ReportLog>emptyList():importTaskSession.getLogs();
			List<ReportLog> subLogs = Ranges.subList(logs, range);
			return new DataWindow<ReportLog>(subLogs, logs.size());
		} catch(Exception e)
		{
			logger.error("An error occurred getting the reports logs", e);
			throw new ServiceException("An error occurred getting the reports logs: "+e.getMessage());
		}
	}
}
