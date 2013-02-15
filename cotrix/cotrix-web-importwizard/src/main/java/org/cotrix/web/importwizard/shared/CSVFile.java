package org.cotrix.web.importwizard.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class CSVFile {
	public interface OnHeaderChangeHandler{
		public void OnHeaderChange(String[] headers,ArrayList<String[]> data);
	}
	public interface OnFileChangeHandler{
		public void OnFileChange(String[] headers,ArrayList<String[]> data);
	}
	private OnHeaderChangeHandler onHeaderChangeHandler;
	private OnFileChangeHandler onFileChangeHandler;
	
	private boolean isNewDefineHeader;
	private String[] headers;
	private ArrayList<String[]> data;
	
	public boolean hasHeader(){
		return (headers == null || headers.length == 0)?false:true;
	}
	public String[] getHeaders() {
		return headers;
	}
	
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	 
	public void setDataAndHeader(ArrayList<String[]> data,String[] header) {
		this.data = data;
		onFileChangeHandler.OnFileChange(header, data);
		onHeaderChangeHandler.OnHeaderChange(header,data);
	}
	public void setOnFileChangeHandler(OnFileChangeHandler onFileChangeHandler){
		this.onFileChangeHandler = onFileChangeHandler;
	}
	public void setOnHeaderChangeHandler(OnHeaderChangeHandler onHeaderChangeHandler){
		this.onHeaderChangeHandler = onHeaderChangeHandler;
	}
	public boolean isNewDefineHeader() {
		return isNewDefineHeader;
	}
	public void setNewDefineHeader(boolean isNewDefineHeader) {
		this.isNewDefineHeader = isNewDefineHeader;
	}
}
