/**
 * 
 */
package org.cotrix.web.publish.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ReportLog implements IsSerializable {
	
	public enum LogType {INFO, WARNING, ERROR};
	
	protected LogType type;
	protected String message;
	
	public ReportLog(){}
	
	/**
	 * @param type
	 * @param message
	 */
	public ReportLog(LogType type, String message) {
		this.type = type;
		this.message = message;
	}
	/**
	 * @return the type
	 */
	public LogType getType() {
		return type;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportLog [type=");
		builder.append(type);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}

}
