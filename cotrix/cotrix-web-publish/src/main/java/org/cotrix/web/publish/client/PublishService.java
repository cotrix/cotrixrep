package org.cotrix.web.publish.client;

import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.publish.shared.ReportLog;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("publish")
public interface PublishService extends RemoteService {
	
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) throws PublishServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws PublishServiceException;

	UICodelistMetadata getMetadata(String codelistId)	throws PublishServiceException;

	CsvConfiguration getCsvWriterConfiguration(String codelistid)	throws PublishServiceException;

}