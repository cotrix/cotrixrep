/**
 * 
 */
package org.cotrix.web.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UINews implements IsSerializable {
	
	protected Date timestamp;
	protected String text;
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UINews [timestamp=");
		builder.append(timestamp);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}
	
	

}
