package org.cotrix.web.importwizard.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CSVFile implements IsSerializable {
	private String filename;
	private int rowCount = 0;
	private String[] header;
	private ArrayList<String[]> data;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String[] getHeader() {
		return header;
	}
	public void setHeader(String[] header) {
		this.header = header;
	}
	public ArrayList<String[]> getData() {
		return data;
	}
	public void setData(ArrayList<String[]> data) {
		this.data = data;
	}
	public boolean isEmpty(){
		return (this.data == null)?true:false;
	}
	public void reset(){
		this.filename = null;
		this.rowCount = 0;
		this.header = null;
		this.data = null;
	}
}
