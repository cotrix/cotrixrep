/**
 * 
 */
package org.cotrix.web.publish.server.util;

import static org.cotrix.repository.CodelistQueries.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.action.CodelistAction;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.io.CloudService;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.Codelists;
import org.cotrix.web.common.server.util.OrderedLists;
import org.cotrix.web.common.server.util.OrderedLists.ValueProvider;
import org.cotrix.web.common.server.util.Repositories;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.publish.server.publish.PublishStatus;
import org.cotrix.web.publish.shared.UIRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.RepositoryService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
@SessionScoped
public class PublishSession implements Serializable {
	
	protected static final ValueProvider<UICodelist> CODELIST_NAME = new ValueProvider<UICodelist>() {

		@Override
		public String getValue(UICodelist item) {
			return ValueUtils.getLocalPart(item.getName());
		}
	};
	
	protected static final ValueProvider<UICodelist> CODELIST_VERSION = new ValueProvider<UICodelist>() {

		@Override
		public String getValue(UICodelist item) {
			return item.getVersion();
		}
	};
	
	protected static final ValueProvider<UICodelist> CODELIST_STATE = new ValueProvider<UICodelist>() {

		@Override
		public String getValue(UICodelist item) {
			return item.getState().toString();
		}
	};
	
	protected static final ValueProvider<UIRepository> REPOSITORY_NAME = new ValueProvider<UIRepository>() {

		@Override
		public String getValue(UIRepository item) {
			return item.getName().getLocalPart();
		}
	};
	
	protected static final ValueProvider<UIRepository> REPOSITORY_PUBLISH_TYPE = new ValueProvider<UIRepository>() {

		@Override
		public String getValue(UIRepository item) {
			return item.getPublishedTypes();
		}
	};
	
	transient Logger logger = LoggerFactory.getLogger(PublishSession.class);
	
	@Inject
	transient CodelistRepository repository;
	
	@Inject
	transient CloudService cloud;
	
	@Inject
	transient LifecycleService lifecycleService;
	
	@Current
	@Inject
	transient User currentUser;
	
	protected OrderedLists<UICodelist> orderedCodelists;
	protected Map<String, UICodelist> indexedCodelists;
	
	protected PublishStatus publishStatus;
	
	protected OrderedLists<UIRepository> orderedRepositories;
	protected Map<QName, RepositoryService> indexedRepositories;
	
	public PublishSession() {
		orderedCodelists = new OrderedLists<UICodelist>();
		orderedCodelists.addField(UICodelist.NAME_FIELD, CODELIST_NAME);
		orderedCodelists.addField(UICodelist.VERSION_FIELD, CODELIST_VERSION);
		orderedCodelists.addField(UICodelist.STATE_FIELD, CODELIST_STATE);
		indexedCodelists = new HashMap<String, UICodelist>();
		
		orderedRepositories = new OrderedLists<UIRepository>();
		orderedRepositories.addField(UIRepository.NAME_FIELD, REPOSITORY_NAME);
		orderedRepositories.addField(UIRepository.PUBLISHED_TYPES_FIELD, REPOSITORY_PUBLISH_TYPE);
		indexedRepositories = new HashMap<QName, RepositoryService>();
	}

	public void loadCodelists()
	{
		Iterator<Codelist> it = repository.get(allLists()).iterator();
		
		orderedCodelists.clear();
		indexedCodelists.clear();

		while(it.hasNext()) {
			Codelist codelist = it.next();
			boolean canPublish = currentUser.can(CodelistAction.PUBLISH.on(codelist.id()));
			if (!canPublish) continue;
			
			State state = lifecycleService.lifecycleOf(codelist.id()).state();
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
	
	public void loadRepositories() {
		orderedRepositories.clear();
		for (RepositoryService repository: cloud.repositories()) {
			if (repository.publishedTypes().isEmpty()) continue;
			UIRepository uiRepository = new UIRepository();
			uiRepository.setId(ValueUtils.safeValue(repository.name()));
			uiRepository.setName(ValueUtils.safeValue(repository.name()));
			uiRepository.setPublishedTypes(Repositories.toString(repository.publishedTypes()));
			uiRepository.setAvailableFormats(Repositories.convertTypes(repository.publishedTypes()));
			orderedRepositories.add(uiRepository);
			
			indexedRepositories.put(repository.name(), repository);
		}
	}
	
	public List<UIRepository> getOrderedRepositories(String sortingField) {
		return orderedRepositories.getSortedList(sortingField);
	}
	
	public RepositoryService getRepositoryService(UIQName id) {
		return indexedRepositories.get(ValueUtils.toQName(id));
	}

	/**
	 * @return the publishStatus
	 */
	public PublishStatus getPublishStatus() {
		return publishStatus;
	}

	/**
	 * @param publishStatus the publishStatus to set
	 */
	public void setPublishStatus(PublishStatus publishStatus) {
		this.publishStatus = publishStatus;
	}
	
}
