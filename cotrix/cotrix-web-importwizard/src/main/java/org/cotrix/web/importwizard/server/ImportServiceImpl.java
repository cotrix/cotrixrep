/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.cotrix.io.CloudService;
import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.server.climport.Importer;
import org.cotrix.web.importwizard.server.climport.ImporterFactory;
import org.cotrix.web.importwizard.server.upload.MappingGuesser;
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
	CloudService cloud;

	@Inject
	protected ParsingHelper parsingHelper;

	@Inject
	protected MappingGuesser mappingsGuesser;

	@Inject
	protected ImporterFactory importerFactory;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws ServletException {
		cloud.discover();
	}

	protected WizardImportSession getImportSession()
	{
		HttpSession httpSession = this.getThreadLocalRequest().getSession();
		WizardImportSession importSession = WizardImportSession.getImportSession(httpSession);
		return importSession;
	}

	protected AssetInfosCache getAssetInfos()
	{
		HttpSession httpSession = this.getThreadLocalRequest().getSession();
		return AssetInfosCache.getFromSession(httpSession, cloud);
	}	

	/** 
	 * {@inheritDoc}
	 * @throws ServiceException 
	 */
	@Override
	public DataWindow<AssetInfo> getAssets(Range range, ColumnSortInfo columnSortInfo, boolean forceRefresh) throws ServiceException {
		logger.trace("getAssets range: {} columnSortInfo: {} forceRefresh: {}", range, columnSortInfo, forceRefresh);
		try {

			AssetInfosCache cache = getAssetInfos();
			if (forceRefresh) cache.refreshCache();
			List<AssetInfo> assets = cache.getAssets(columnSortInfo.getName());
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
			Asset asset = getAsset(assetId);
			if (asset == null) throw new ServiceException("Asset with id "+assetId+" not found");
			AssetDetails assetDetails = Assets.convertToDetails(asset);
			System.out.println(assetDetails);
			return assetDetails;
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	protected Asset getAsset(String id)
	{
		AssetInfosCache cache = getAssetInfos();
		Asset asset = cache.getAsset(id);
		if (asset == null) throw new IllegalArgumentException("Asset with id "+id+" not found");
		return asset;
	}

	public RepositoryDetails getRepositoryDetails(String repositoryId) throws ServiceException
	{
		try {

			AssetInfosCache cache = getAssetInfos();
			RepositoryDetails repository = cache.getRepository(repositoryId);
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
			WizardImportSession.getCleanImportSession(this.getThreadLocalRequest().getSession());
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public FileUploadProgress getUploadProgress() throws ServiceException {

		try {
			WizardImportSession session = getImportSession();

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

		try {
			WizardImportSession session = getImportSession();

			if (session.getCodeListType()!=CodeListType.CSV) {
				logger.error("Requested CSV preview data when CodeList type is {}", session.getCodeListType());
				throw new ServiceException("No preview data available");
			}

			if (session.getCsvParserConfiguration()!=null && session.getCsvParserConfiguration().equals(configuration)) return session.getPreviewCache();


			session.setCsvParserConfiguration(configuration);

			//FIXME duplicate code
			Table table = parsingHelper.parse(session.getCsvParserConfiguration(), session.getFileField().getInputStream());
			PreviewData previewData = parsingHelper.convert(table, !configuration.isHasHeader(), ParsingHelper.ROW_LIMIT);
			session.setPreviewCache(previewData);
			session.setCacheDirty(false);

			List<AttributeMapping> mappings = mappingsGuesser.guessMappings(table);
			session.setGuessedMappings(mappings);

			return previewData;
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

			WizardImportSession session = getImportSession();

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
			WizardImportSession session = getImportSession();
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
			WizardImportSession session = getImportSession();
			CsvConfiguration configuration = session.getCsvParserConfiguration();
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
			WizardImportSession session = getImportSession();
			return session.getGuessedMappings();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public void startImport(ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode) throws ServiceException {
		logger.trace("startImport metadata: {}, mappings: {}, mappingMode: {}", metadata, mappings, mappingMode);
		WizardImportSession session = getImportSession();

		try {
			session.setImportedCodelistName(metadata.getName());
			Importer<?> importer = importerFactory.createImporter(session, metadata, mappings, mappingMode);
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
			WizardImportSession session = getImportSession();
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
			HttpSession httpSession = this.getThreadLocalRequest().getSession();
			WizardImportSession session = WizardImportSession.getCleanImportSession(httpSession);
			Asset asset = getAsset(assetId);	
			session.setSelectedAsset(asset);

			ImportMetadata metadata = new ImportMetadata();
			metadata.setOriginalName(asset.name());
			metadata.setName(asset.name());
			session.setGuessedMetadata(metadata);

			if (asset.type() == SdmxCodelist.type) {
				session.setCodeListType(CodeListType.SDMX);
				CodelistBean codelist = cloud.retrieveAsSdmx(asset.id());
				metadata.setVersion(codelist.getVersion());
				List<AttributeMapping> mappings = mappingsGuesser.getSdmxDefaultMappings();
				session.setGuessedMappings(mappings);
			}


			if (asset.type() == CsvCodelist.type) {
				session.setCodeListType(CodeListType.CSV);
				Table table = cloud.retrieveAsTable(asset.id());
				metadata.setVersion("1.0");
				List<AttributeMapping> mappings = mappingsGuesser.guessMappings(table);
				session.setGuessedMappings(mappings);
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
			WizardImportSession session = getImportSession();
			List<ReportLog> logs = (session==null || session.getLogs()==null)?Collections.<ReportLog>emptyList():session.getLogs();
			List<ReportLog> subLogs = Ranges.subList(logs, range);
			return new DataWindow<ReportLog>(subLogs, logs.size());
		} catch(Exception e)
		{
			logger.error("An error occurred getting the reports logs", e);
			throw new ServiceException("An error occurred getting the reports logs: "+e.getMessage());
		}
	}
}
