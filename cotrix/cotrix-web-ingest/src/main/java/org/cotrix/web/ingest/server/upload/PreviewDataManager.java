/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.server.util.ParsingHelper;
import org.cotrix.web.ingest.shared.PreviewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.csv.CsvAsset;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class PreviewDataManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7002044840644562951L;
	
	protected transient Logger logger = LoggerFactory.getLogger(PreviewDataManager.class);
	
	private interface StreamProvider {
		public InputStream getStream() throws IOException;
		public CsvConfiguration getConfiguration() throws IOException;
		public void clean() throws IOException;
	}

	
	@Inject
	transient protected CsvParserConfigurationGuesser configurationGuesser;

	@Inject
	transient protected ParsingHelper parsingHelper;
	
	@Inject
	transient protected MappingGuesser mappingsGuesser;
	
	@Inject
	transient protected MappingsManager mappingsManager;

	private CsvConfiguration parserConfiguration;
	private StreamProvider streamProvider;

	private PreviewData previewData;
	
	public void setup(final InputStream inputStream, final CsvAsset asset) throws IOException {
		
		final File tmpFile = File.createTempFile(asset.name(), ".tmp");
		FileOutputStream output = new FileOutputStream(tmpFile);
		IOUtils.copy(inputStream, output);
		output.close();
		inputStream.close();
				
		setup(new StreamProvider() {
			
			@Override
			public InputStream getStream() throws IOException {
				return new FileInputStream(tmpFile);
			}
			
			public CsvConfiguration getConfiguration() {
				return toCsvConfiguration(asset);
			}
			
			public void clean() throws IOException {
				tmpFile.delete();
			}
		});
	}

	public void setup(final String fileName, final FileItem fileItem) throws IOException {
		setup(new StreamProvider() {
			
			@Override
			public InputStream getStream() throws IOException {
				return fileItem.getInputStream();
			}
			
			public CsvConfiguration getConfiguration() throws IOException {
				return configurationGuesser.guessConfiguration(fileName, streamProvider.getStream());
			}
			
			public void clean() throws IOException {
				fileItem.delete();
			}
		});
	}
	
	private CsvConfiguration toCsvConfiguration(CsvAsset asset) {
		CsvConfiguration configuration = new CsvConfiguration();
		configuration.setCharset(asset.encoding().name());
		configuration.setComment('#');
		configuration.setFieldSeparator(asset.delimiter());
		configuration.setHasHeader(true);
		configuration.setQuote(asset.quote());
		
		return configuration;
	}
	
	private void setup(StreamProvider streamProvider) throws IOException {
		this.streamProvider = streamProvider;
		this.parserConfiguration = streamProvider.getConfiguration();
		buildCsvPreviewData();
	}

	public void refresh(CsvConfiguration parserConfiguration) {
		logger.trace("refresh parserConfiguration {}", parserConfiguration);
		if (this.parserConfiguration.equals(parserConfiguration)) {
			logger.trace("generation not necessary, the CSV parser configuration is the same as before");
			return;
		}
		this.parserConfiguration = parserConfiguration;
		buildCsvPreviewData();
	}
	
	public void refresh(Table table) {
		logger.trace("refresh with table");
		this.parserConfiguration = null;
		setupPreview(table, false);
	}

	/**
	 * @return the previewData
	 */
	public PreviewData getPreviewData() {
		return previewData;
	}

	/**
	 * @return the parserConfiguration
	 */
	public CsvConfiguration getParserConfiguration() {
		return parserConfiguration;
	}

	private void buildCsvPreviewData() {
		logger.trace("buildCsvPreviewData");
		try {
			Table table = parsingHelper.parse(parserConfiguration, streamProvider.getStream());
			setupPreview(table, !parserConfiguration.isHasHeader());
		} catch(Exception e) {
			logger.error("Failed building CSV preview", e);
			throw new RuntimeException("Failed CSV preview generation", e);
		}
	}
	
	private void setupPreview(Table table, boolean hasHeader) {
		try {
			previewData = parsingHelper.convert(table, hasHeader, ParsingHelper.ROW_LIMIT);
			mappingsManager.updateMappings(table);
		} catch(Exception e) {
			logger.error("Failed building CSV preview", e);
			throw new RuntimeException("Failed CSV preview generation", e);
		}
	}
	
	public void clean() {
		
	}
}
