package org.cotrix.web.share.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CotrixImportModelController {
	public interface OnFileChangeHandler extends Serializable {
		public void onFileChange(CSVFile csvFile);
	}

	public interface OnDescriptionChangeHandler extends IsSerializable {
		public void onDescriptionChange(HashMap<String, String> description);
	}

	public interface OnMetaDataChangeHandler extends IsSerializable {
		public void onMetadataChange(Metadata metadata);
	}

	public interface OnTypeChangeHandler extends IsSerializable {
		public void onTypeChange(HashMap<String, HeaderType> headerType);
	}
	
	private ArrayList<OnFileChangeHandler> onFileChangeHandlers = new ArrayList<OnFileChangeHandler>();
	private ArrayList<OnDescriptionChangeHandler> onDescriptionChangeHandlers = new ArrayList<OnDescriptionChangeHandler>();
	private ArrayList<OnMetaDataChangeHandler> onMetaDataChangeHandlers = new ArrayList<OnMetaDataChangeHandler>();
	private ArrayList<OnTypeChangeHandler> onTypeChangeHandlers = new ArrayList<OnTypeChangeHandler>();
	
	public void addOnFileChangeHandler(OnFileChangeHandler onFileChangeHandler){
		this.onFileChangeHandlers.add(onFileChangeHandler);
	}
	public void addOnDescriptionChangeHandler(OnDescriptionChangeHandler onDescriptionChangeHandler){
		this.onDescriptionChangeHandlers.add(onDescriptionChangeHandler);
	}
	public void addOnMetaDataChangeHandler(OnMetaDataChangeHandler onMetaDataChangeHandler){
		this.onMetaDataChangeHandlers.add(onMetaDataChangeHandler);
	}
	public void addOnTypeChangeHandler(OnTypeChangeHandler onTypeChangeHandler){
		this.onTypeChangeHandlers.add(onTypeChangeHandler);
	}
	
	private void notifyOnFileChangeHandler(CSVFile csvFile){
		for (OnFileChangeHandler onFileChangeHandler : onFileChangeHandlers) {
			onFileChangeHandler.onFileChange(csvFile);
		}
	}
	private void notifyOnDescriptionChangeHandler(HashMap<String, String> description){
		for (OnDescriptionChangeHandler onDescriptionChangeHandler : onDescriptionChangeHandlers) {
			onDescriptionChangeHandler.onDescriptionChange(description);
		}
	}
	private void notifyOnMetaDataChangeHandler(Metadata metadata){
		for (OnMetaDataChangeHandler onMetaDataChangeHandler : onMetaDataChangeHandlers) {
			onMetaDataChangeHandler.onMetadataChange(metadata);
		}
	}
	private void notifyOnTypeChangeHandler(HashMap<String, HeaderType> type){
		for (OnTypeChangeHandler onTypeChangeHandler : onTypeChangeHandlers) {
			onTypeChangeHandler.onTypeChange(type);
		}
	}
	
	private CotrixImportModel model = new CotrixImportModel();
	
	public Metadata getMetadata() {
		return this.model.getMetadata();
	}
	public void setMetadata(Metadata metadata) {
		this.model.setMetadata(metadata);
		notifyOnMetaDataChangeHandler(metadata);
	}
	public CSVFile getCsvFile() {
		return this.model.getCsvFile();
	}
	public void setCsvFile(CSVFile csvFile) {
		this.model.setCsvFile(csvFile);
		notifyOnFileChangeHandler(csvFile);
	}
	public HashMap<String, String> getDescription() {
		return this.model.getDescription();
	}
	public void setDescription(HashMap<String, String> description) {
		this.model.setDescription(description);
		notifyOnDescriptionChangeHandler(description);
	}
	public HashMap<String, String> getSummary() {
		return this.model.getSummary();
	}
	public void setSummary(HashMap<String, String> summary) {
		this.model.setSummary(summary);
	}
	public HashMap<String, HeaderType> getType() {
		return this.model.getType();
	}
	public void setType(HashMap<String, HeaderType> type) {
		this.model.setType(type);
		notifyOnTypeChangeHandler(type);
	}
	
}
