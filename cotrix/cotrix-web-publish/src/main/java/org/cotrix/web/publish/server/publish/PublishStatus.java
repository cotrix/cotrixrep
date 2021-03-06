/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.util.List;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.publish.shared.PublishProgress;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishStatus {
	
	protected Codelist publishedCodelist;
	protected PublishProgress progress = new PublishProgress();
	protected List<ReportLog> reportLogs;
	protected String report;
	protected Object publishResult;
	
	/**
	 * @return the publishResult
	 */
	public Object getPublishResult() {
		return publishResult;
	}

	/**
	 * @param publishResult the publishResult to set
	 */
	public void setPublishResult(Object publishResult) {
		this.publishResult = publishResult;
	}

	/**
	 * @return the reportLogs
	 */
	public List<ReportLog> getReportLogs() {
		return reportLogs;
	}

	/**
	 * @param reportLogs the reportLogs to set
	 */
	public void setReportLogs(List<ReportLog> reportLogs) {
		this.reportLogs = reportLogs;
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
	 * @return the progress
	 */
	public PublishProgress getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(PublishProgress progress) {
		this.progress = progress;
	}

	/**
	 * @return the publishedCodelist
	 */
	public Codelist getPublishedCodelist() {
		return publishedCodelist;
	}

	/**
	 * @param publishedCodelist the publishedCodelist to set
	 */
	public void setPublishedCodelist(Codelist publishedCodelist) {
		this.publishedCodelist = publishedCodelist;
	}

}
