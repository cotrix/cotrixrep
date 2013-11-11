package org.cotrix.web.publish.client;

import org.cotrix.web.publish.shared.Codelist;
import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.publish.shared.ReportLog;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("publish")
public interface PublishService extends RemoteService {
	
	public DataWindow<Codelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) throws PublishServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws PublishServiceException;

	CodelistMetadata getMetadata(String codelistId)	throws PublishServiceException;

}