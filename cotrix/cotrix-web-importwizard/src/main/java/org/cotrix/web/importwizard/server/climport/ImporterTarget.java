/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import static org.cotrix.domain.dsl.Users.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.action.events.CodelistActionEvents;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.User;
import org.cotrix.lifecycle.LifecycleService;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.DefaultLifecycleStates;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImporterTarget {
	
	protected Logger logger = LoggerFactory.getLogger(ImporterTarget.class);
	
	@Inject
	private CodelistRepository repository;
	
	@Inject
	private LifecycleService lifecycleService;
	
	@Inject
	private Event<CodelistActionEvents.CodelistEvent> events;
	
	@Inject
	protected UserRepository userRepository;
	
	
	public void save(Codelist codelist, boolean sealed, String ownerId) {
		logger.trace("save codelist.id: {}, sealed: {}, ownerId: {}", codelist.id(), sealed, ownerId);
		repository.add(codelist);
		
		State startState = sealed?DefaultLifecycleStates.sealed:DefaultLifecycleStates.draft;
		lifecycleService.start(codelist.id(), startState);
		
		User owner = userRepository.lookup(ownerId);
		logger.trace("owner: {}", owner);
		User changeset = modifyUser(owner).is(Roles.OWNER.on(codelist.id())).build();
		userRepository.update(changeset);
		
		events.fire(new CodelistActionEvents.Import(codelist.id(), codelist.name(), codelist.version()));
	}

}
