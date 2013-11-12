package org.cotrix.web.publish.server;

import static org.cotrix.repository.Queries.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.publish.server.util.PublishSession;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.publish.shared.ReportLog;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.Encodings;
import org.cotrix.web.share.server.util.Ranges;
import org.cotrix.web.share.server.util.ValueUtils;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIQName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public List<AttributeMapping> getMappings() throws PublishServiceException {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		mappings.add(getAttributeMapping("code", "code", "", "code", true));
		mappings.add(getAttributeMapping("name", "description", "en", "name(en)", true));
		mappings.add(getAttributeMapping("name", "description", "fr", "name(fr)", false));
		mappings.add(getAttributeMapping("name", "description", "sp", "name(sp)", true));
		mappings.add(getAttributeMapping("author", "description", "", "author", true));
		return mappings;
	}

	protected AttributeMapping getAttributeMapping(String name, String type, String language, String colName, boolean mapped) {
		AttributeDefinition attr = new AttributeDefinition();
		attr.setName(new UIQName("", name));
		attr.setType(new UIQName("", type));
		attr.setLanguage(language);

		AttributeMapping attributeMapping = new AttributeMapping();
		attributeMapping.setAttributeDefinition(attr);
		attributeMapping.setColumnName(colName);
		attributeMapping.setMapped(mapped);
		return attributeMapping;
	}
	
	@Override
	public void startPublish(String codelistId, List<AttributeMapping> mappings, MappingMode mappingMode) throws PublishServiceException {
		logger.trace("startPublish codelistId: {}, mappings: {}, mappingMode: {}", codelistId, mappings, mappingMode);
		

		/*try {
			session.setImportedCodelistName(metadata.getName());
			Importer<?> importer = importerFactory.createImporter(session, metadata, mappings, mappingMode);
			session.setImporter(importer);
			
			//FIXME use a serialiser provider
			Thread th = new Thread(importer);
			th.start();
		} catch (IOException e) {
			logger.error("Error during import starting", e);
			throw new ImportServiceException("An error occurred starting import: "+e.getMessage());
		}*/
	}


	@Override
	public Progress getPublishProgress() throws PublishServiceException {
		try {
			return new Progress();
		} catch(Exception e)
		{
			logger.error("An error occurred on server side", e);
			throw new PublishServiceException("An error occurred on server side: "+e.getMessage());
		}
	}





	@Override
	public DataWindow<ReportLog> getReportLogs(Range range)
			throws PublishServiceException {
		return new DataWindow<ReportLog>(new ArrayList<ReportLog>());
	}

}
