/**
 * 
 */
package org.cotrix.web.importwizard.server;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.cotrix.io.Channels;
import org.cotrix.web.importwizard.client.ImportService;
import org.cotrix.web.importwizard.server.climport.Importer;
import org.cotrix.web.importwizard.server.climport.ImporterFactory;
import org.cotrix.web.importwizard.server.upload.MappingGuesser;
import org.cotrix.web.importwizard.server.util.Assets;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AssetsBatch;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CsvPreviewData;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.ImportProgress;
import org.cotrix.web.importwizard.shared.ImportServiceException;
import org.cotrix.web.importwizard.shared.FileUploadProgress;
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

	/** 
	 * {@inheritDoc}
	 * @throws ImportServiceException 
	 */
	@Override
	public AssetsBatch getAssets(Range range) throws ImportServiceException {

		try {

			ArrayList<AssetInfo> assets = new ArrayList<AssetInfo>();
	
			int discovered = remoteRepository.discover(Channels.importTypes);
			logger.trace("discovered "+discovered+" remote codelist");
			
			int i = -1;

			for (Asset asset:remoteRepository) {
				i++;
				if (i<range.getStart()) continue;
				if (i>=(range.getStart() + range.getLength())) continue;
				
				AssetInfo assetInfo = Assets.convert(asset);
				logger.trace("converted {} to {}", asset.name(), assetInfo);
				assets.add(assetInfo);
			}

			/*assets.add(new AssetInfo("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=FAO:CL_DIVISION(0.1)", "CL_DIVISION", "sdmx/codelist", "D4Science Development Registry"));
			assets.add(new AssetInfo("321", "Gears", "SDMX", "D4Science Development Registry"));
			assets.add(new AssetInfo("333", "Species Year 2013", "CSV", "D4Science Development Registry"));
			assets.add(new AssetInfo("324", "Country", "SDMX", "D4Science Development Registry"));*/

			logger.trace("returning "+assets.size()+" elements");

			return new AssetsBatch(assets, i+1);
		} catch(Exception e)
		{
			e.printStackTrace();

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
		return null;
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
	public CsvPreviewData getCsvPreviewData() throws ImportServiceException {

		WizardImportSession session = getImportSession();

		if (session.getCodeListType()!=CodeListType.CSV) {
			logger.error("Requested CSV preview data when CodeList type is {}", session.getCodeListType());
			throw new ImportServiceException("No preview data available");
		}

		if (!session.isCacheDirty()) return session.getPreviewCache();

		try {
		
			//FIXME duplicate code
			Table table = parsingHelper.parse(session.getCsvParserConfiguration(), session.getFileField().getInputStream());
			CsvPreviewData previewData = parsingHelper.convert(table, ParsingHelper.ROW_LIMIT);
			session.setPreviewCache(previewData);
			session.setCacheDirty(false);
			
			List<AttributeMapping> mappings = mappingsGuesser.guessMappings(table);
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
	public void updateCsvParserConfiguration(CsvParserConfiguration configuration) throws ImportServiceException {
		WizardImportSession session = getImportSession();
		CsvParserConfiguration currentConfiguration = session.getCsvParserConfiguration();
		if (!currentConfiguration.equals(configuration)) {
			session.setCsvParserConfiguration(configuration);
			session.setCacheDirty(true);
		}

	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<AttributeMapping> getMappings() throws ImportServiceException {
		WizardImportSession session = getImportSession();
		return session.getMappings();
	}

	@Override
	public void startImport(ImportMetadata metadata, List<AttributeMapping> mappings) throws ImportServiceException {
		WizardImportSession session = getImportSession();
		
		
		try {
			Importer<?> importer = importerFactory.createImporter(session, metadata, mappings);
			Thread th = new Thread(importer);
			th.start();
			session.setImporter(importer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public ImportProgress getImportProgress() throws ImportServiceException {
		WizardImportSession session = getImportSession();
		return session.getImporter().getProgress();
	}

	@Override
	public void setAsset(String assetId) throws ImportServiceException {
		HttpSession httpSession = this.getThreadLocalRequest().getSession();
		WizardImportSession session = WizardImportSession.getCleanImportSession(httpSession);
		Asset asset = getAsset(assetId);
		session.setSelectedAsset(asset);
		List<AttributeMapping> mappings = mappingsGuesser.getSdmxDefaultMappings();
		session.setMappings(mappings);
		
		if (asset.type() == SdmxCodelist.type) session.setCodeListType(CodeListType.SDMX);
		if (asset.type()==CsvCodelist.type) session.setCodeListType(CodeListType.CSV);
		
	}
}
