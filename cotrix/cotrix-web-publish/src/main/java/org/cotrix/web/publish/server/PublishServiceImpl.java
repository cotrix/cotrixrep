package org.cotrix.web.publish.server;

import static org.cotrix.repository.Queries.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.publish.shared.PublishServiceException;
import org.cotrix.web.publish.shared.ReportLog;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.Ranges;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;
import org.cotrix.web.share.shared.codelist.UICodelist;
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

	@Override
	public DataWindow<UICodelist> getCodelists(Range range, ColumnSortInfo sortInfo, boolean force) {
		logger.trace("getCodelists range: {} sortInfo: {} force: {}", range, sortInfo, force);
		
		
		//CodelistQuery<Code> query = allCodes(codelistId);
		/*int from = range.getStart();
		int to = range.getStart() + range.getLength();
		logger.trace("query range from: {} to: {}", from ,to);*/
		//query.setRange(new Range(from, to));
		
		//FIXME use ranges
		CodelistQuery<Codelist> query =	allLists();
		Iterator<org.cotrix.domain.Codelist> it = repository.queryFor(query).iterator();
		
		List<Codelist> codelists = new ArrayList<Codelist>();
		while(it.hasNext()) codelists.add(it.next());
		
		Comparator<Codelist> comparator = Codelists.NAME_COMPARATOR;
		if (sortInfo.getName()!=null && sortInfo.getName().equals(UICodelist.VERSION_FIELD)) comparator = Codelists.VERSION_COMPARATOR;
		
		Collections.sort(codelists, comparator);
		
		List<Codelist> data = (sortInfo.isAscending())?Ranges.subList(codelists, range):Ranges.subListReverseOrder(codelists, range);
		
		List<UICodelist> uicodelists = toUICodelists(data);
		
		return new DataWindow<UICodelist>(uicodelists);
	}
	
	protected List<UICodelist> toUICodelists(List<Codelist> codelists)
	{
		List<UICodelist> uicodelists = new ArrayList<UICodelist>(codelists.size());
		for (Codelist codelist:codelists) {
			UICodelist uiCodelist = new UICodelist();
			uiCodelist.setId(codelist.id());
			uiCodelist.setName(codelist.name().getLocalPart());
			uiCodelist.setVersion(codelist.version());
			uicodelists.add(uiCodelist);
		}
		return uicodelists;
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
		// TODO Auto-generated method stub
		return null;
	}

}
