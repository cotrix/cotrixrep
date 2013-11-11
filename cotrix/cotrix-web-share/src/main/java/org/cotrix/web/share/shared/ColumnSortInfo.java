/**
 * 
 */
package org.cotrix.web.share.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ColumnSortInfo implements IsSerializable {
	
	protected String name;
	protected boolean ascending;
	
	public ColumnSortInfo(){}
	
	/**
	 * @param name
	 * @param ascending
	 */
	public ColumnSortInfo(String name, boolean ascending) {
		this.name = name;
		this.ascending = ascending;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnSortingInfo [name=");
		builder.append(name);
		builder.append(", ascending=");
		builder.append(ascending);
		builder.append("]");
		return builder.toString();
	}
}
