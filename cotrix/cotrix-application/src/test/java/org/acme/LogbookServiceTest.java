package org.acme;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.application.logbook.LogbookEvent.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.lifecycle.impl.DefaultLifecycleStates.*;
import static org.junit.Assert.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.action.events.CodelistActionEvents.CodelistEvent;
import org.cotrix.action.events.CodelistActionEvents.Import;
import org.cotrix.action.events.CodelistActionEvents.Publish;
import org.cotrix.application.VersioningService;
import org.cotrix.application.logbook.Logbook;
import org.cotrix.application.logbook.LogbookService;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.lifecycle.LifecycleEvent;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.cotrix.test.CurrentUser;
import org.junit.Before;
import org.junit.Test;

public class LogbookServiceTest extends ApplicationTest {
	
	@Inject
	private LogbookService service;
	
	@Inject
	private CodelistRepository codelists;

	@Inject
	private UserRepository users;

	@Inject
	Event<CodelistEvent> events;
	
	@Inject
	Event<LifecycleEvent> lcEvents;
	
	@Inject
	VersioningService vservice;
	
	@Inject
	CurrentUser user;
	
	Codelist list = codelist().name("mylist").build();
		
	@Before
	public void before() {
		
		User fifi = user().name("fifi").fullName("fifi").noMail().build();
		users.add(fifi);
		user.set(fifi);
		
		codelists.add(list);
	}
	
	@Test
	public void retrieveUnknownLogbook() {

		assertNull(service.logbookOf("unknown"));
		
	}
	
	@Test
	public void logbooksAddedWhenCodelistsAre() {

		Logbook book = service.logbookOf(list.id());
	
		assertFalse(book.entries(CREATED).isEmpty());

	}
	
	@Test
	public void logbooksRemovedWhenCodelistsAre() {

		codelists.remove(list.id());
		
		assertNull(service.logbookOf(list.id()));

	}
	
	@Test
	public void logbooksUpdatedWhenCodelistAreImported() {

		events.fire(new Import("myrepo",list.id(), list.qname(), list.version(), null));
		
		Logbook book = service.logbookOf(list.id());

		assertFalse(book.entries(IMPORTED).isEmpty());

	}
	
	@Test
	public void logbooksUpdatedWhenCodelistArePublished() {

		events.fire(new Publish(list.id(), list.qname(), list.version(), new QName("myrepo"), null));

		Logbook book = service.logbookOf(list.id());

		assertFalse(book.entries(PUBLISHED).isEmpty());

	}
	
	@Test
	public void logbooksUpdatedWhenCodelistAreVersioned() {
		
		Codelist versioned = vservice.bump(list).to("2");
		
		codelists.add(versioned);
		
		Logbook book = service.logbookOf(list.id());
		
		assertFalse(book.entries(VERSIONED).isEmpty());

		book = service.logbookOf(versioned.id());
		
		assertFalse(book.entries(VERSIONED).isEmpty());


	}
	
	@Test
	public void logbooksUpdatedWhenCodelistChangeState() {
		
		Logbook book = service.logbookOf(list.id());

		lcEvents.fire(new LifecycleEvent(list.id(),draft,LOCK.on(list.id()),sealed));
		
		assertFalse(book.entries(LOCKED).isEmpty());

	}
}
