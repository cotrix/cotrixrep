package org.cotrix.web.publish.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Codelist;
import org.cotrix.io.CloudService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.publish.server.publish.PublishStatus;
import org.cotrix.web.publish.server.publish.Publisher;
import org.cotrix.web.publish.server.publish.Publishers;
import org.cotrix.web.publish.server.util.Mappings;
import org.cotrix.web.publish.server.util.Mappings.MappingProvider;
import org.cotrix.web.publish.server.util.PublishSession;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.Format;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.Encodings;
import org.cotrix.web.share.server.util.Ranges;
import org.cotrix.web.share.server.util.Repositories;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.ReportLog;
import org.cotrix.web.share.shared.codelist.RepositoryDetails;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIQName;
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
	protected CodelistLoader codelistLoader;

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
		/*codelistLoader.importAllCodelist();
		logger.trace("codelist in repository:");
		for (Codelist codelist:repository.queryFor(allLists())) logger.trace(codelist.name().toString());
		logger.trace("done");*/
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) {
		logger.trace("getCodelists range: {} sortInfo: {} force: {}", range, sortInfo, force);

		if (force) session.loadCodelists();

		List<UICodelist> codelists = session.getOrderedCodelists(sortInfo.getName());
		return getDataWindow(codelists, sortInfo.isAscending(), range);
	}

	@Override
	public UICodelistMetadata getMetadata(String codelistId) throws PublishServiceException {
		logger.trace("getMetadata codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		UICodelist uiCodelist = session.getUiCodelist(codelistId);

		return Codelists.toCodelistMetadata(codelist, uiCodelist.getState());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CsvConfiguration getCsvWriterConfiguration(String codelistid) throws PublishServiceException {
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
			throw new PublishServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public List<AttributeMapping> getMappings(String codelistId, Destination destination, Format type) throws PublishServiceException {
		logger.trace("getMappings codelistId{} destination {} type {}", codelistId, destination, type);

		CodelistSummary summary = repository.summary(codelistId);
		
		MappingProvider<?> provider = null;
		switch (type) {
			case CSV: provider = Mappings.COLUMN_PROVIDER; break;
			case SDMX: provider = Mappings.SDMX_PROVIDER; break;
			default: return new ArrayList<AttributeMapping>();
		}

		switch (destination) {
			case FILE: return Mappings.getFileMappings(summary, provider);
			case CHANNEL: return Mappings.getChannelMappings(summary, provider);
			default: return new ArrayList<AttributeMapping>();
		}
	}

	@Override
	public void startPublish(PublishDirectives publishDirectives) throws PublishServiceException {
		logger.trace("startPublish publishDirectives: {}", publishDirectives);

		PublishStatus publishStatus = new PublishStatus();
		session.setPublishStatus(publishStatus);

		Codelist codelist = repository.lookup(publishDirectives.getCodelistId());
		publishStatus.setPublishedCodelist(codelist);

		Publisher<?> publisher = publishers.createPublisher(publishDirectives, publishStatus);
		//FIXME use a service provider
		Thread th = new Thread(publisher);
		th.start();
	}


	@Override
	public Progress getPublishProgress() throws PublishServiceException {
		try {
			return session.getPublishStatus().getProgress();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new PublishServiceException("An error occurred on server side: "+e.getMessage());
		}
	}

	@Override
	public DataWindow<ReportLog> getReportLogs(Range range) throws PublishServiceException {
		logger.trace("getReportLogs range: {}",range);
		if (session.getPublishStatus() == null || session.getPublishStatus().getReportLogs() == null) return DataWindow.emptyWindow();
		return new DataWindow<ReportLog>(Ranges.subList(session.getPublishStatus().getReportLogs(), range), session.getPublishStatus().getReportLogs().size());
	}

	@Override
	public DataWindow<UIRepository> getRepositories(Range range, ColumnSortInfo sortInfo, boolean force)	throws PublishServiceException {
		logger.trace("getRepositories range: {} sortInfo: {} force: {}", range, sortInfo, force);

		if (force) session.loadRepositories();

		List<UIRepository> codelists = session.getOrderedRepositories(sortInfo.getName());
		return getDataWindow(codelists, sortInfo.isAscending(), range);
	}

	protected <T> DataWindow<T> getDataWindow(List<T> data, boolean ascending, Range range) {
		List<T> dataWindow = (ascending)?Ranges.subList(data, range):Ranges.subListReverseOrder(data, range);

		return new DataWindow<T>(dataWindow, data.size());
	}

	public RepositoryDetails getRepositoryDetails(UIQName id) throws PublishServiceException
	{
		logger.trace("getRepositoryDetails id: {} ", id);
		RepositoryService repository = session.getRepositoryService(id);
		return Repositories.convert(repository);

	}

}
