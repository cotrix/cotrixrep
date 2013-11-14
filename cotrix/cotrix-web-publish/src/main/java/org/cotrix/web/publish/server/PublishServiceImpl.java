package org.cotrix.web.publish.server;

import static org.cotrix.repository.Queries.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.publish.server.publish.PublishDestination;
import org.cotrix.web.publish.server.publish.PublishMapper;
import org.cotrix.web.publish.server.publish.PublishStatus;
import org.cotrix.web.publish.server.publish.Publisher;
import org.cotrix.web.publish.server.publish.SerializationDirectivesProducer;
import org.cotrix.web.publish.server.util.PublishSession;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.DestinationType;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.Encodings;
import org.cotrix.web.share.server.util.Ranges;
import org.cotrix.web.share.server.util.ValueUtils;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.ReportLog;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Table;

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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		codelistLoader.importAllCodelist();
		logger.trace("codelist in repository:");
		for (Codelist codelist:repository.queryFor(allLists())) logger.trace(codelist.name().toString());
		logger.trace("done");
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) {
		logger.trace("getCodelists range: {} sortInfo: {} force: {}", range, sortInfo, force);

		if (force) session.loadCodelists();

		List<UICodelist> codelists = session.getOrderedCodelists(sortInfo.getName());

		List<UICodelist> codelistWindow = (sortInfo.isAscending())?Ranges.subList(codelists, range):Ranges.subListReverseOrder(codelists, range);

		return new DataWindow<UICodelist>(codelistWindow, codelists.size());
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
	public List<AttributeMapping> getMappings(String codelistId, DestinationType type) throws PublishServiceException {
		logger.trace("getMappings codelistId{} type {}", codelistId, type);
		
		CodelistSummary summary = repository.summary(codelistId);
		
		switch (type) {
			case FILE: return getFileMappings(summary);
			case CHANNEL: return getChannelMappings(summary);
			default: return new ArrayList<AttributeMapping>();
		}

	}
	
	protected List<AttributeMapping> getChannelMappings(CodelistSummary summary) {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		
		return mappings;
	}
	
	protected List<AttributeMapping> getFileMappings(CodelistSummary summary) {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (QName attributeName:summary.codeNames()) {
			for (QName attributeType : summary.codeTypesFor(attributeName)) {
				Collection<String> languages = summary.codeLanguagesFor(attributeName, attributeType);
				if (languages.isEmpty()) mappings.add(getAttributeMapping(attributeName, attributeType, null));
				else for (String language:languages) mappings.add(getAttributeMapping(attributeName, attributeType, language));
			}
		}
		return mappings;
	}

	protected AttributeMapping getAttributeMapping(QName name, QName type, String language) {
		AttributeDefinition attr = new AttributeDefinition();
		attr.setName(ValueUtils.safeValue(name));
		attr.setType(ValueUtils.safeValue(type));
		attr.setLanguage(ValueUtils.safeValue(language));

		StringBuilder columnName = new StringBuilder(name.getLocalPart());
		if (language!=null) columnName.append('(').append(language).append(')');

		AttributeMapping attributeMapping = new AttributeMapping();
		attributeMapping.setAttributeDefinition(attr);
		attributeMapping.setColumnName(columnName.toString());
		attributeMapping.setMapped(true);
		return attributeMapping;
	}
	
	@Inject
	public PublishMapper.CsvMapper csvMapper;
	
	@Inject
	public SerializationDirectivesProducer.TableDesktopProducer tableDesktopProducer;
	
	@Inject
	public PublishDestination.DesktopDestination desktopDestination;

	@Override
	public void startPublish(PublishDirectives publishDirectives) throws PublishServiceException {
		logger.trace("startPublish publishDirectives: {}", publishDirectives);

		PublishStatus publishStatus = new PublishStatus();
		session.setPublishStatus(publishStatus);
		
		Codelist codelist = repository.lookup(publishDirectives.getCodelistId());
		publishStatus.setPublishedCodelist(codelist);
		
		Publisher<Table, File> publisher = new Publisher<Table, File>(publishDirectives, csvMapper, tableDesktopProducer, desktopDestination, publishStatus); 
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

}
