package org.cotrix.web.importwizard.shared;

import java.util.HashMap;

public class ImportWizardModel {
	
	private String filename;
	private Metadata metadata;
	private CSVFile csvFile = new CSVFile();
	private HashMap<String, String> headerDescription;
	private HashMap<String, String> summary;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public CSVFile getCsvFile() {
		return csvFile;
	}
	public void setCsvFile(CSVFile csvFile) {
		this.csvFile = csvFile;
	}
	public HashMap<String, String> getHeaderDescription() {
		return headerDescription;
	}
	public void setHeaderDescription(HashMap<String, String> headerDescription) {
		this.headerDescription = headerDescription;
	}
	public HashMap<String, String> getSummary() {
		return summary;
	}
	public void setSummary(HashMap<String, String> summary) {
		this.summary = summary;
	}
}
