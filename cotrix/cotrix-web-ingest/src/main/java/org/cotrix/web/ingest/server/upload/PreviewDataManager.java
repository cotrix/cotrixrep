/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.apache.commons.fileupload.FileItem;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.server.util.ParsingHelper;
import org.cotrix.web.ingest.shared.PreviewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	@Inject
	transient protected CsvParserConfigurationGuesser configurationGuesser;

	@Inject
	transient protected ParsingHelper parsingHelper;
	
	@Inject
	transient protected MappingGuesser mappingsGuesser;
	
	@Inject
	transient protected MappingsManager mappingsManager;

	protected CsvConfiguration parserConfiguration;
	protected FileItem fileItem;

	protected PreviewData previewData;

	public void setup(String fileName, FileItem fileItem) throws IOException {
		this.fileItem = fileItem;
		this.parserConfiguration = configurationGuesser.guessConfiguration(fileName, fileItem.getInputStream());
		buildPreviewData();
	}

	public void refresh(CsvConfiguration parserConfiguration) {
		logger.trace("refresh parserConfiguration {}", parserConfiguration);
		if (this.parserConfiguration.equals(parserConfiguration)) {
			logger.trace("generation not necessary, the CSV parser configuration is the same as before");
			return;
		}
		this.parserConfiguration = parserConfiguration;
		buildPreviewData();
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

	protected void buildPreviewData() {
		logger.trace("buildPreviewData");
		try {
			Table table = parsingHelper.parse(parserConfiguration, fileItem.getInputStream());
			
			previewData = parsingHelper.convert(table, !parserConfiguration.isHasHeader(), ParsingHelper.ROW_LIMIT);
			mappingsManager.updateMappings(table);
		} catch(Exception e) {
			logger.error("Failed building CSV preview", e);
			throw new RuntimeException("Failed CSV preview generation", e);
		}
	}
}
