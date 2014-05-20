/**
 * 
 */
package org.cotrix.web.common.shared;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.shared.feature.FeatureCarrier;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataWindow<T> extends FeatureCarrier {
	
	protected static DataWindow<Object> EMPTY = new DataWindow<Object>(new ArrayList<Object>());
	
	@SuppressWarnings("unchecked")
	public static <T> DataWindow<T> emptyWindow() {
		return (DataWindow<T>) EMPTY;
	}
	
	protected List<T> data;
	protected int totalSize;
	
	protected DataWindow() {}
	
	/**
	 * @param data
	 * @param totalSize
	 */
	public DataWindow(List<T> data, int totalSize) {
		this.data = data;
		this.totalSize = totalSize;
	}
	
	/**
	 * @param data
	 * @param totalSize
	 */
	public DataWindow(List<T> data) {
		this.data = data;
		this.totalSize = data.size();
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataWindow [data=");
		builder.append(data);
		builder.append(", totalSize=");
		builder.append(totalSize);
		builder.append("]");
		return builder.toString();
	}
}
