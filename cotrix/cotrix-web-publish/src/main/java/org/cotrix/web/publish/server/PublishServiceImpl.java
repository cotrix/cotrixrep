package org.cotrix.web.publish.server;

import static org.cotrix.repository.CodelistQueries.summary;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.server.util.Encodings;
import org.cotrix.web.common.server.util.Ranges;
import org.cotrix.web.common.server.util.Repositories;
import org.cotrix.web.common.server.util.Ranges.Predicate;
import org.cotrix.web.common.shared.ColumnSortInfo;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.Format;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.ReportLog;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.publish.server.publish.PublishStatus;
import org.cotrix.web.publish.server.publish.Publishers;
import org.cotrix.web.publish.server.util.CodelistFilter;
import org.cotrix.web.publish.server.util.Mappings;
import org.cotrix.web.publish.server.util.Mappings.MappingProvider;
import org.cotrix.web.publish.server.util.PublishSession;
import org.cotrix.web.publish.server.util.RepositoryFilter;
import org.cotrix.web.publish.shared.AttributesMappings;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.UIRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.RepositoryService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.Range;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class PublishServiceImpl extends RemoteServiceServlet implements PublishService {

	protected Logger logger = LoggerFactory.getLogger(PublishServiceImpl.class);

	@Inject
	CodelistRepository repository;

	@Inject
	protected PublishSession session;

	@Inject
	protected CloudService cloud;
	

	@Inject
	public Publishers publishers;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws ServletException {
		super.init();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, String query, boolean force) {
		logger.trace("getCodelists range: {} sortInfo: {} query: {} force: {}", range, sortInfo, query, force);

		if (force) session.loadCodelists();
		
		Predicate<UICodelist> predicate = Ranges.noFilter();
		if (!query.isEmpty()) predicate = new CodelistFilter(query);

		List<UICodelist> codelists = session.getOrderedCodelists(sortInfo.getName());
		return getDataWindow(codelists, sortInfo.isAscending(), range, predicate);
	}

	@Override
	public UICodelistMetadata getMetadata(String codelistId) throws ServiceException {
		logger.trace("getMetadata codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		UICodelist uiCodelist = session.getUiCodelist(codelistId);

		return Codelists.toCodelistMetadata(codelist, uiCodelist.getState());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CsvConfiguration getCsvWriterConfiguration(String codelistid) throws ServiceException {
		try {
			CsvConfiguration configuration = new CsvConfiguration();
			configuration.setAvailablesCharset(Encodings.getEncodings());
			configuration.setCharset("UTF-8");
			configuration.setComment('#');
			configuration.setFieldSeparator(',');
			configuration.setHasHeader(true);
			configuration.setQuote('"');
			return configuration;
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public AttributesMappings getMappings(String codelistId, Destination destination, Format type) throws ServiceException {
		logger.trace("getMappings codelistId{} destination {} type {}", codelistId, destination, type);

		CodelistSummary summary = repository.get(summary(codelistId));
		
		MappingProvider<?> provider = null;
		switch (type) {
			case CSV: provider = Mappings.COLUMN_PROVIDER; break;
			case SDMX: provider = Mappings.SDMX_PROVIDER; break;
			case COMET: provider = Mappings.COMET_PROVIDER; break;
			default: return new AttributesMappings();
		}
		
		boolean includeCodelistsMappings = type == Format.SDMX;
		
		return Mappings.getMappings(summary, provider, includeCodelistsMappings);
	}

	@Override
	public void startPublish(PublishDirectives publishDirectives) throws ServiceException {
		logger.trace("startPublish publishDirectives: {}", publishDirectives);

		PublishStatus publishStatus = new PublishStatus();
		session.setPublishStatus(publishStatus);

		Codelist codelist = repository.lookup(publishDirectives.getCodelistId());
		publishStatus.setPublishedCodelist(codelist);

		publishers.createPublisher(publishDirectives, publishStatus);
	}


	@Override
	public Progress getPublishProgress() throws ServiceException {
		try {
			return session.getPublishStatus().getProgress();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new ServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public DataWindow<ReportLog> getReportLogs(Range range) throws ServiceException {
		logger.trace("getReportLogs range: {}",range);
		if (session.getPublishStatus() == null || session.getPublishStatus().getReportLogs() == null) return DataWindow.emptyWindow();
		return new DataWindow<ReportLog>(Ranges.subList(session.getPublishStatus().getReportLogs(), range), session.getPublishStatus().getReportLogs().size());
	}

	@Override
	public DataWindow<UIRepository> getRepositories(Range range, ColumnSortInfo sortInfo, String query, boolean force) throws ServiceException {
		logger.trace("getRepositories range: {} sortInfo: {} query: {} force: {}", range, sortInfo, query, force);

		if (force) session.loadRepositories();
		
		Predicate<UIRepository> predicate = Ranges.noFilter();
		if (!query.isEmpty()) predicate = new RepositoryFilter(query);

		List<UIRepository> codelists = session.getOrderedRepositories(sortInfo.getName());
		return getDataWindow(codelists, sortInfo.isAscending(), range, predicate);
	}

	protected <T> DataWindow<T> getDataWindow(List<T> data, boolean ascending, Range range, Predicate<T>  predicate) {
	
		List<T> dataWindow = (ascending)?Ranges.subList(data, range, predicate):Ranges.subListReverseOrder(data, range, predicate);
		
		int size = Ranges.size(data, predicate);

		return new DataWindow<T>(dataWindow, size);
	}

	public RepositoryDetails getRepositoryDetails(UIQName id) throws ServiceException
	{
		logger.trace("getRepositoryDetails id: {} ", id);
		RepositoryService repository = session.getRepositoryService(id);
		return Repositories.convert(repository);

	}

}
