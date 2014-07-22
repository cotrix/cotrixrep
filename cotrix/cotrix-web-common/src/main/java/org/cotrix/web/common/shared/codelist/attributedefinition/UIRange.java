/**
 * 
 */
package org.cotrix.web.common.shared.codelist.attributedefinition;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIRange implements IsSerializable {
	
	private int min;
	private int max;
	
	public UIRange(){}

	public UIRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIRange [min=");
		builder.append(min);
		builder.append(", max=");
		builder.append(max);
		builder.append("]");
		return builder.toString();
	}
}
