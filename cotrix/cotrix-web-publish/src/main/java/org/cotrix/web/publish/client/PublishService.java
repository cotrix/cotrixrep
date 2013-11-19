package org.cotrix.web.publish.client;

import java.util.List;

import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.Format;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.ReportLog;
import org.cotrix.web.share.shared.codelist.RepositoryDetails;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIQName;
import org.cotrix.web.share.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("publish")
public interface PublishService extends RemoteService {
	
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) throws ServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException;

	UICodelistMetadata getMetadata(String codelistId) throws ServiceException;

	CsvConfiguration getCsvWriterConfiguration(String codelistid) throws ServiceException;

	List<AttributeMapping> getMappings(String codelistId, Destination destination, Format type) throws ServiceException;

	void startPublish(PublishDirectives publishDirectives) throws ServiceException;

	Progress getPublishProgress() throws ServiceException;
	
	DataWindow<UIRepository> getRepositories(Range range, ColumnSortInfo sortInfo, boolean force) throws ServiceException;
	
	public RepositoryDetails getRepositoryDetails(UIQName repositoryId) throws ServiceException;

}