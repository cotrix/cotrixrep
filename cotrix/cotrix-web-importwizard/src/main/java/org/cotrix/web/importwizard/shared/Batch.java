/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class Batch<T> implements IsSerializable {
	
	protected List<T> data;
	protected int totalSize;
	
	public Batch(){}
	
	/**
	 * @param data
	 * @param totalSize
	 */
	public Batch(List<T> data, int totalSize) {
		this.data = data;
		this.totalSize = totalSize;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * @return the totaleSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
}
