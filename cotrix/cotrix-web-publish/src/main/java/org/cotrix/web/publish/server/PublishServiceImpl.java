package org.cotrix.web.publish.server;

import static org.cotrix.repository.Queries.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.publish.server.util.PublishSession;
import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.publish.shared.ReportLog;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.Ranges;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;
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
	public DataWindow<org.cotrix.web.publish.shared.Codelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) {
		logger.trace("getCodelists range: {} sortInfo: {} force: {}", range, sortInfo, force);
		
		if (force) session.loadCodelists();
		
		List<org.cotrix.web.publish.shared.Codelist> codelists = session.getCodelists(sortInfo.getName());
		
		List<org.cotrix.web.publish.shared.Codelist> codelistWindow = (sortInfo.isAscending())?Ranges.subList(codelists, range):Ranges.subListReverseOrder(codelists, range);
		
		return new DataWindow<org.cotrix.web.publish.shared.Codelist>(codelistWindow, codelists.size());
	}
	
	@Override
	public CodelistMetadata getMetadata(String codelistId) throws PublishServiceException {
		logger.trace("getMetadata codelistId: {}", codelistId);
		Codelist codelist = repository.lookup(codelistId);
		return Codelists.toCodelistMetadata(codelist);
	}
	
	

	@Override
	public DataWindow<ReportLog> getReportLogs(Range range)
			throws PublishServiceException {
		return new DataWindow<ReportLog>(new ArrayList<ReportLog>());
	}

}
