package org.cotrix.web.importwizard.shared;

import java.util.ArrayList;



public class CSVFile {
	public interface OnFileChangeHandler{
		public void onFileChange(String[] headers,ArrayList<String[]> data);
	}
	private OnFileChangeHandler onFileChangeHandler;
	
	private boolean isNewDefineHeader;
	private String[] headers;
	private ArrayList<String[]> data = new ArrayList<String[]>();
	private ArrayList<OnFileChangeHandler> handlerList = new ArrayList<CSVFile.OnFileChangeHandler>();
	
	public boolean hasHeader(){
		return (headers == null || headers.length == 0)?false:true;
	}
	
	public String[] getHeaders() {
		return headers;
	}
	public boolean isEmpty(){
		return (data.isEmpty());
	}
	public void reset(){
		data.clear();
		setDataAndHeader(data, new String[]{});
	}
	
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	 
	public void setDataAndHeader(ArrayList<String[]> data,String[] header) {
		this.data = data;
		for (OnFileChangeHandler onFileChangeHandler : handlerList) {
			onFileChangeHandler.onFileChange(header, data);
		}
	}
	
	public void addOnFilechangeHandler(OnFileChangeHandler onFileChangeHandler){
		this.handlerList.add(onFileChangeHandler);
	}
	
	public boolean isNewDefineHeader() {
		return isNewDefineHeader;
	}
	
	public void setNewDefineHeader(boolean isNewDefineHeader) {
		this.isNewDefineHeader = isNewDefineHeader;
	}
}
