/**
 * 
 */
package org.cotrix.web.ingest.server.climport;

import java.io.InputStream;
import java.util.List;

import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.MappingMode;
import org.virtualrepository.Asset;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportTaskSession {
	
	protected CsvConfiguration csvParserConfiguration;
	
	protected InputStream inputStream;
	protected Asset asset;
	
	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;
	
	protected String ownerId;
	
	protected List<ReportLog> logs;
	protected String report;
	
	public ImportTaskSession(InputStream inputStream, Asset asset, String ownerId) {
		this.inputStream = inputStream;
		this.asset = asset;
		this.ownerId = ownerId;
	}
	
	public void setUserOptions(CsvConfiguration csvParserConfiguration, ImportMetadata metadata,
			List<AttributeMapping> mappings, MappingMode mappingMode) {
		this.csvParserConfiguration = csvParserConfiguration;
		this.metadata = metadata;
		this.mappings = mappings;
		this.mappingMode = mappingMode;
	}

	/**
	 * @return the csvParserConfiguration
	 */
	public CsvConfiguration getCsvParserConfiguration() {
		return csvParserConfiguration;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @return the asset
	 */
	public Asset getAsset() {
		return asset;
	}

	/**
	 * @return the metadata
	 */
	public ImportMetadata getMetadata() {
		return metadata;
	}
	
	/**
	 * @return the mappings
	 */
	public List<AttributeMapping> getMappings() {
		return mappings;
	}
	
	/**
	 * @return the mappingMode
	 */
	public MappingMode getMappingMode() {
		return mappingMode;
	}

	/**
	 * @param logs the logs to set
	 */
	public void setLogs(List<ReportLog> logs) {
		this.logs = logs;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * @return the owner
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @return the logs
	 */
	public List<ReportLog> getLogs() {
		return logs;
	}

	/**
	 * @return the report
	 */
	public String getReport() {
		return report;
	}

	

}
