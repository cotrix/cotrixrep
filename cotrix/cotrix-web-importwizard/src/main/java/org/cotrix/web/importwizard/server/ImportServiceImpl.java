/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.server.climport.Importer;
import org.cotrix.web.importwizard.server.climport.ImporterFactory;
import org.cotrix.web.importwizard.server.upload.MappingGuesser;
import org.cotrix.web.importwizard.server.util.AssetInfosCache;
import org.cotrix.web.importwizard.server.util.Assets;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.importwizard.server.util.Ranges;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AssetsBatch;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.ColumnSortInfo;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.ImportProgress;
import org.cotrix.web.importwizard.shared.ImportServiceException;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
import org.cotrix.web.importwizard.shared.ReportLog;
import org.cotrix.web.importwizard.shared.ReportLogsBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;
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
	VirtualRepository remoteRepository;

	@Inject
	protected ParsingHelper parsingHelper;

	@Inject
	protected MappingGuesser mappingsGuesser;

	@Inject
	protected ImporterFactory importerFactory;

	protected WizardImportSession getImportSession()
	{
		HttpSession httpSession = this.getThreadLocalRequest().getSession();
		WizardImportSession importSession = WizardImportSession.getImportSession(httpSession);
		return importSession;
	}
	
	protected AssetInfosCache getAssetInfos()
	{
		HttpSession httpSession = this.getThreadLocalRequest().getSession();
		return AssetInfosCache.getFromSession(httpSession, remoteRepository);
	}	

	/** 
	 * {@inheritDoc}
	 * @throws ImportServiceException 
	 */
	@Override
	public AssetsBatch getAssets(Range range, ColumnSortInfo columnSortInfo) throws ImportServiceException {
		logger.trace("getAssets range: "+range+" columnSortInfo: "+columnSortInfo);
		try {

			AssetInfosCache cache = getAssetInfos();
			List<AssetInfo> assets = cache.getAssets(columnSortInfo.getName());
			List<AssetInfo> sublist = columnSortInfo.isAscending()?Ranges.subList(assets, range):Ranges.subListReverseOrder(assets, range);
				
			/*assets.add(new AssetInfo("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=FAO:CL_DIVISION(0.1)", "CL_DIVISION", "sdmx/codelist", "D4Science Development Registry"));
			assets.add(new AssetInfo("321", "Gears", "SDMX", "D4Science Development Registry"));
			assets.add(new AssetInfo("333", "Species Year 2013", "CSV", "D4Science Development Registry"));
			assets.add(new AssetInfo("324", "Country", "SDMX", "D4Science Development Registry"));*/

			logger.trace("returning "+sublist.size()+" elements");

			return new AssetsBatch(sublist, assets.size());
		} catch(Exception e)
		{
			logger.error("Error retrieving assets", e);
			throw new ImportServiceException(e.getMessage());
		}
	}




	/** 
	 * {@inheritDoc}
	 * @throws ImportServiceException 
	 */
	@Override
	public AssetDetails getAssetDetails(String assetId) throws ImportServiceException {

		Asset asset = getAsset(assetId);
		if (asset == null) throw new ImportServiceException("Asset with id "+assetId+" not found");
		AssetDetails details = Assets.convertToDetails(asset);
		System.out.println(details);
		return details;

		/*List<Property> properties = new ArrayList<Property>();
		properties.add(new Property("scope", "/gcube/devsec", "null"));
		RepositoryDetails repository = new RepositoryDetails("D4Science Development Registry", "sdmx/codelist",  "sdmx/codelist", properties);

		properties = new ArrayList<Property>();
		properties.add(new Property("uri", 
				"http://node8.d.d4science.research-infrastructures.eu:8080/FusionRegistry/ws/rest/codelist/FAO/CL_DIVISION/0.1", 
				"asset's URI"));
		properties.add(new Property("status", "partial", "asset's status"));
		AssetDetails assetDetails = new AssetDetails("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=FAO:CL_DIVISION(0.1)", 
				"CL_DIVISION", "sdmx/codelist", properties, repository);

		return assetDetails;*/
	}



	protected Asset getAsset(String id)
	{
		for (Asset asset:remoteRepository) if (asset.id().equals(id)) return asset;
		throw new IllegalArgumentException("Asset with id "+id+" not found");
	}

	@Override
	public FileUploadProgress getUploadProgress() throws ImportServiceException {

		WizardImportSession session = getImportSession();

		FileUploadProgress uploadProgress = session.getUploadProgress();
		if (uploadProgress == null) {
			logger.error("Unexpected upload progress null.");
			throw new ImportServiceException("Upload progress not available");
		}
		return uploadProgress;
	}

	@Override
	public PreviewData getCsvPreviewData(CsvParserConfiguration configuration) throws ImportServiceException {

		WizardImportSession session = getImportSession();

		if (session.getCodeListType()!=CodeListType.CSV) {
			logger.error("Requested CSV preview data when CodeList type is {}", session.getCodeListType());
			throw new ImportServiceException("No preview data available");
		}

		if (session.getCsvParserConfiguration()!=null && session.getCsvParserConfiguration().equals(configuration)) return session.getPreviewCache();

		try {

			session.setCsvParserConfiguration(configuration);

			//FIXME duplicate code
			Table table = parsingHelper.parse(session.getCsvParserConfiguration(), session.getFileField().getInputStream());
			PreviewData previewData = parsingHelper.convert(table, !configuration.isHasHeader(), ParsingHelper.ROW_LIMIT);
			session.setPreviewCache(previewData);
			session.setCacheDirty(false);

			AttributesMappings mappings = mappingsGuesser.guessMappings(table);
			session.setMappings(mappings);

			return previewData;
		} catch(Exception e)
		{
			logger.error("Error converting the preview data", e);
			throw new ImportServiceException(e.getMessage());
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CodeListType getCodeListType() throws ImportServiceException {
		WizardImportSession session = getImportSession();

		return session.getCodeListType();
	}

	@Override
	public ImportMetadata getMetadata() throws ImportServiceException {
		WizardImportSession session = getImportSession();
		return session.getGuessedMetadata();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CsvParserConfiguration getCsvParserConfiguration() throws ImportServiceException {
		WizardImportSession session = getImportSession();
		CsvParserConfiguration configuration = session.getCsvParserConfiguration();
		configuration.setAvailablesCharset(getEncodings());
		return configuration;
	}

	protected List<String> getEncodings()
	{
		List<String> charsets = new ArrayList<String>();
		for (Charset charset:Charset.availableCharsets().values()) {
			charsets.add(charset.displayName());
		}
		return charsets;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public AttributesMappings getMappings() throws ImportServiceException {
		WizardImportSession session = getImportSession();
		return session.getMappings();
	}

	@Override
	public void startImport(ImportMetadata metadata, AttributesMappings mappings) throws ImportServiceException {
		WizardImportSession session = getImportSession();


		try {
			Importer<?> importer = importerFactory.createImporter(session, metadata, mappings);
			Thread th = new Thread(importer);
			th.start();
			session.setImporter(importer);
		} catch (IOException e) {
			logger.error("Error during import starting", e);
			throw new ImportServiceException("An error occurred starting import: "+e.getMessage());
		}
	}


	@Override
	public ImportProgress getImportProgress() throws ImportServiceException {
		WizardImportSession session = getImportSession();
		return session.getImporter().getProgress();
	}

	@Override
	public void setAsset(String assetId) throws ImportServiceException {
		
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
			AttributesMappings mappings = mappingsGuesser.getSdmxDefaultMappings();
			session.setMappings(mappings);
		}

		
		if (asset.type() == CsvCodelist.type) {
			session.setCodeListType(CodeListType.CSV);
			Table table = remoteRepository.retrieve(asset, Table.class);
			AttributesMappings mappings = mappingsGuesser.guessMappings(table);
			session.setMappings(mappings);
		}
		
		} catch(Exception e)
		{
			logger.error("Error setting the Asset",e);
			throw new ImportServiceException("Error setting the Asset: "+e.getMessage());
		}

	}

	@Override
	public ReportLogsBatch getReportLogs(Range range) throws ImportServiceException {
		logger.trace("getReportLogs range: {}", range);
		try {
			WizardImportSession session = getImportSession();
			List<ReportLog> logs = (session==null || session.getLogs()==null)?Collections.<ReportLog>emptyList():session.getLogs();
			List<ReportLog> subLogs = Ranges.subList(logs, range);
			return new ReportLogsBatch(subLogs, logs.size());
		} catch(Exception e)
		{
			logger.error("An error occurred getting the reports logs", e);
			throw new ImportServiceException("An error occurred getting the reports logs: "+e.getMessage());
		}
	}
}
