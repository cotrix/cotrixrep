/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportProgress implements IsSerializable {
	
	public enum Status {ONGOING, DONE, FAILED};
	
	protected Status status;
	protected String report;
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * @return the report
	 */
	public String getReport() {
		return report;
	}
	
	/**
	 * @param report the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImportProgress [status=");
		builder.append(status);
		builder.append(", report=");
		builder.append(report);
		builder.append("]");
		return builder.toString();
	}
}
