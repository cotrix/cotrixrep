/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.web.importwizard.client.step.csvpreview.PreviewGrid.DataProvider.PreviewData;
import org.cotrix.web.importwizard.server.util.ParsingHelper;
import org.cotrix.web.share.shared.CsvConfiguration;
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
	
	@Inject
	transient protected CsvParserConfigurationGuesser configurationGuesser;

	@Inject
	transient protected ParsingHelper parsingHelper;
	@Inject
	transient protected MappingGuesser mappingsGuesser;
	@Inject
	transient protected MappingsManager mappingsManager;

	protected CsvConfiguration parserConfiguration;
	protected InputStream inputStream;

	protected PreviewData previewData;

	public void setup(String fileName, InputStream inputStream) throws IOException {
		this.parserConfiguration = configurationGuesser.guessConfiguration(fileName, inputStream);
		this.inputStream = inputStream;
		buildPreviewData();
	}

	public void refresh(CsvConfiguration parserConfiguration) {
		if (this.parserConfiguration.equals(parserConfiguration)) return;
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
		Table table = parsingHelper.parse(parserConfiguration, inputStream);
		previewData = parsingHelper.convert(table, !parserConfiguration.isHasHeader(), ParsingHelper.ROW_LIMIT);
		mappingsManager.updateMappings(table);
	}
}
