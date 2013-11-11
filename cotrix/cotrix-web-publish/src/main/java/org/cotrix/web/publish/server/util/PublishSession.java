/**
 * 
 */
package org.cotrix.web.publish.server.util;

import static org.cotrix.repository.Queries.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.web.publish.shared.Codelist;
import org.cotrix.web.share.server.util.FieldComparator.ValueProvider;
import org.cotrix.web.share.server.util.OrderedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
@SessionScoped
public class PublishSession implements Serializable {
	
	protected static final ValueProvider<Codelist> NAME = new ValueProvider<Codelist>() {

		@Override
		public String getValue(Codelist item) {
			return item.getName();
		}
	};
	
	protected static final ValueProvider<Codelist> VERSION = new ValueProvider<Codelist>() {

		@Override
		public String getValue(Codelist item) {
			return item.getVersion();
		}
	};
	
	protected static final ValueProvider<Codelist> STATE = new ValueProvider<Codelist>() {

		@Override
		public String getValue(Codelist item) {
			return item.getState();
		}
	};
	
	transient Logger logger = LoggerFactory.getLogger(PublishSession.class);
	
	@Inject
	transient CodelistRepository repository;
	
	@Inject
	transient LifecycleService lifecycleService;
	
	protected OrderedList<Codelist> codelists;
	
	public PublishSession() {
		codelists = new OrderedList<Codelist>();
		codelists.addField(Codelist.NAME_FIELD, NAME);
		codelists.addField(Codelist.VERSION_FIELD, VERSION);
		codelists.addField(Codelist.STATE_FIELD, STATE);
	}

	public void loadCodelists()
	{
		CodelistQuery<org.cotrix.domain.Codelist> query =	allLists();
		Iterator<org.cotrix.domain.Codelist> it = repository.queryFor(query).iterator();
		codelists.clear();

		while(it.hasNext()) {
			org.cotrix.domain.Codelist codelist = it.next();
			Codelist uiCodelist = new Codelist();
			uiCodelist.setId(codelist.id());
			uiCodelist.setName(codelist.name().getLocalPart());
			uiCodelist.setVersion(codelist.version());
			
			String state = lifecycleService.start(codelist.id()).state().toString();
			uiCodelist.setState(state);
			
			codelists.add(uiCodelist);
		}
		
		logger.trace("loaded {} codelists", codelists.size());
	}
	
	public List<Codelist> getCodelists(String sortingField) {
		return codelists.getSortedList(sortingField);
	}
}
