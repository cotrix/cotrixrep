package org.cotrix.web.publish.server;

import static org.cotrix.repository.Queries.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.publish.client.PublishService;
import org.cotrix.web.share.server.util.CodelistLoader;
import org.cotrix.web.share.shared.ColumnSortInfo;
import org.cotrix.web.share.shared.DataWindow;
import org.cotrix.web.share.shared.UICodelist;
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
		int from = range.getStart();
		int to = range.getStart() + range.getLength();
		logger.trace("query range from: {} to: {}", from ,to);
		//query.setRange(new Range(from, to));
		
		//FIXME use ranges
		Iterator<org.cotrix.domain.Codelist> it = repository.queryFor(allLists()).iterator();
		
		List<UICodelist> codelists = new ArrayList<UICodelist>(range.getLength());
		
		int count = 0;
		while(it.hasNext() && count<to) {
			count++;
			if (count<from) continue;
			Codelist codelist = it.next();
			UICodelist uiCodelist = new UICodelist();
			uiCodelist.setId(codelist.id());
			uiCodelist.setName(codelist.name().getLocalPart());
			uiCodelist.setVersion(codelist.version());
			codelists.add(uiCodelist);
		}
		
		
		return new DataWindow<UICodelist>(codelists);
	}

}
