package org.cotrix.web.importwizard.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CotrixImportModel implements IsSerializable{
	public interface OnMetaDataChangeHandler{
		public void onMetadataChange(Metadata metadata);
	}
	
	public interface OnTypeChangeHandler{
		public void onTypeChange(HashMap<String, HeaderType> headerType);
	}
	
	public interface OnDescriptionChangeHandler{
		public void onDescriptionChange(HashMap<String, String> headerDescription);
	}
	private OnMetaDataChangeHandler onMetadataChangeHandler;
	private OnTypeChangeHandler onTypeChangeHandler;
	private OnDescriptionChangeHandler onDescriptionChangeHandler;

	private String filename;
	private Metadata metadata;
	private CSVFile csvFile = new CSVFile();
	private HashMap<String, String> headerDescription;
	private HashMap<String, String> summary;
	private HashMap<String, HeaderType> headerType;

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
		onMetadataChangeHandler.onMetadataChange(metadata);
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
		onDescriptionChangeHandler.onDescriptionChange(headerDescription);
	}
	public HashMap<String, String> getSummary() {
		return summary;
	}
	public void setSummary(HashMap<String, String> summary) {
		this.summary = summary;
	}
	public HashMap<String, HeaderType> getHeaderType() {
		return headerType;
	}
	public void setHeaderType(HashMap<String, HeaderType> headerType) {
		this.headerType = headerType;
		onTypeChangeHandler.onTypeChange(headerType);
	}
	
	public void setOnMetadataChangeHandler(OnMetaDataChangeHandler onMetadataChangeHandler) {
		this.onMetadataChangeHandler = onMetadataChangeHandler;
	}
	public void setOnTypeChangeHandler(OnTypeChangeHandler onTypeChangeHandler) {
		this.onTypeChangeHandler = onTypeChangeHandler;
	}
	public void setOnDescriptionChangeHandler(OnDescriptionChangeHandler onDescriptionChangeHandler) {
		this.onDescriptionChangeHandler = onDescriptionChangeHandler;
	}
}
