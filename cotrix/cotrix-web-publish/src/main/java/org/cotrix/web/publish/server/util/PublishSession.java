/**
 * 
 */
package org.cotrix.web.publish.server.util;

import static org.cotrix.repository.Queries.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.web.share.server.util.Codelists;
import org.cotrix.web.share.server.util.FieldComparator.ValueProvider;
import org.cotrix.web.share.server.util.OrderedList;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
@SessionScoped
public class PublishSession implements Serializable {
	
	protected static final ValueProvider<UICodelist> NAME = new ValueProvider<UICodelist>() {

		@Override
		public String getValue(UICodelist item) {
			return item.getName();
		}
	};
	
	protected static final ValueProvider<UICodelist> VERSION = new ValueProvider<UICodelist>() {

		@Override
		public String getValue(UICodelist item) {
			return item.getVersion();
		}
	};
	
	protected static final ValueProvider<UICodelist> STATE = new ValueProvider<UICodelist>() {

		@Override
		public String getValue(UICodelist item) {
			return item.getState();
		}
	};
	
	transient Logger logger = LoggerFactory.getLogger(PublishSession.class);
	
	@Inject
	transient CodelistRepository repository;
	
	@Inject
	transient LifecycleService lifecycleService;
	
	protected OrderedList<UICodelist> orderedCodelists;
	protected Map<String, UICodelist> indexedCodelists;
	
	public PublishSession() {
		orderedCodelists = new OrderedList<UICodelist>();
		orderedCodelists.addField(UICodelist.NAME_FIELD, NAME);
		orderedCodelists.addField(UICodelist.VERSION_FIELD, VERSION);
		orderedCodelists.addField(UICodelist.STATE_FIELD, STATE);
		indexedCodelists = new HashMap<String, UICodelist>();
	}

	public void loadCodelists()
	{
		CodelistQuery<Codelist> query =	allLists();
		Iterator<Codelist> it = repository.queryFor(query).iterator();
		
		orderedCodelists.clear();
		indexedCodelists.clear();

		while(it.hasNext()) {
			Codelist codelist = it.next();
			String state = lifecycleService.start(codelist.id()).state().toString();
			UICodelist uiCodelist = Codelists.toUICodelist(codelist, state);
			orderedCodelists.add(uiCodelist);
			indexedCodelists.put(uiCodelist.getId(), uiCodelist);
		}
		
		logger.trace("loaded {} codelists", orderedCodelists.size());
	}
	
	public List<UICodelist> getOrderedCodelists(String sortingField) {
		return orderedCodelists.getSortedList(sortingField);
	}
	
	public UICodelist getUiCodelist(String id)
	{
		return indexedCodelists.get(id);
	}
}
