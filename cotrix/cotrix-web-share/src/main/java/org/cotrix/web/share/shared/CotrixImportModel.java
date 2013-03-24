package org.cotrix.web.share.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CotrixImportModel implements IsSerializable{
	private Metadata metadata;
	private CSVFile csvFile;
	private HashMap<String, String> description;
	private HashMap<String, String> summary;
	private HashMap<String, HeaderType> type;
	
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
	public HashMap<String, String> getDescription() {
		return description;
	}
	public void setDescription(HashMap<String, String> description) {
		this.description = description;
	}
	public HashMap<String, String> getSummary() {
		return summary;
	}
	public void setSummary(HashMap<String, String> summary) {
		this.summary = summary;
	}
	public HashMap<String, HeaderType> getType() {
		return type;
	}
	public void setType(HashMap<String, HeaderType> type) {
		this.type = type;
	}
	
}
