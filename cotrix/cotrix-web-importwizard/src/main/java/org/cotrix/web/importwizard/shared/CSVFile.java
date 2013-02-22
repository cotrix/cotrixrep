package org.cotrix.web.importwizard.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class CSVFile {
	public interface OnFile1ChangeHandler{
		public void onFileChange(String[] headers,ArrayList<String[]> data);
	}
	public interface OnFile2ChangeHandler{
		public void onFileChange(String[] headers,ArrayList<String[]> data);
	}
	public interface OnFile3ChangeHandler{
		public void onFileChange(String[] headers,ArrayList<String[]> data);
	}
	public interface OnFile4ChangeHandler{
		public void onFileChange(String[] headers,ArrayList<String[]> data);
	}
	
	private OnFile1ChangeHandler onFile1ChangeHandler;
	private OnFile2ChangeHandler onFile2ChangeHandler;
	private OnFile3ChangeHandler onFile3ChangeHandler;
	private OnFile4ChangeHandler onFile4ChangeHandler;
	
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
		onFile1ChangeHandler.onFileChange(header,data);
		onFile2ChangeHandler.onFileChange(header,data);
		onFile3ChangeHandler.onFileChange(header,data);
		onFile4ChangeHandler.onFileChange(header,data);
	}
	
	public void setOnFile4ChangeHandler(OnFile4ChangeHandler onFile4ChangeHandler){
		this.onFile4ChangeHandler = onFile4ChangeHandler;
	}
	
	public void setOnFile3ChangeHandler(OnFile3ChangeHandler onFile3ChangeHandler){
		this.onFile3ChangeHandler = onFile3ChangeHandler;
	}
	
	public void setOnFile2ChangeHandler(OnFile2ChangeHandler onFile2ChangeHandler){
		this.onFile2ChangeHandler = onFile2ChangeHandler;
	}

	public void setOnFile1ChangeHandler(OnFile1ChangeHandler onFile1ChangeHandler){
		this.onFile1ChangeHandler = onFile1ChangeHandler;
	}
	
	public boolean isNewDefineHeader() {
		return isNewDefineHeader;
	}
	
	public void setNewDefineHeader(boolean isNewDefineHeader) {
		this.isNewDefineHeader = isNewDefineHeader;
	}
}
