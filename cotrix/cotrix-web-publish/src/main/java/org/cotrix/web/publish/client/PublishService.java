package org.cotrix.web.publish.client;

import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.publish.shared.AttributesMappings;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.Format;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.UIRepository;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service/publishService")
public interface PublishService extends RemoteService {
	
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) throws ServiceException;
	
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException;

	public UICodelistMetadata getMetadata(String codelistId) throws ServiceException;

	public CsvConfiguration getCsvWriterConfiguration(String codelistid) throws ServiceException;

	public AttributesMappings getMappings(String codelistId, Destination destination, Format type) throws ServiceException;

	public void startPublish(PublishDirectives publishDirectives) throws ServiceException;

	public Progress getPublishProgress() throws ServiceException;
	
	public DataWindow<UIRepository> getRepositories(Range range, ColumnSortInfo sortInfo, boolean force) throws ServiceException;
	
	public RepositoryDetails getRepositoryDetails(UIQName repositoryId) throws ServiceException;

}